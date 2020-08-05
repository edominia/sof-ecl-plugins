/*
 * Copyright (c) 2019, Intel Corporation
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *   * Neither the name of the Intel Corporation nor the
 *     names of its contributors may be used to endorse or promote products
 *     derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */

package org.sofproject.gst.topo.ops;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.sofproject.core.AudioDevNodeProject;
import org.sofproject.core.connection.AudioDevNodeConnection;
import org.sofproject.core.ops.SimpleRemoteOp;
import org.sofproject.topo.ui.json.PipelineJsonProperty;
import org.sofproject.topo.ui.json.JsonUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class GstDockerOperation extends SimpleRemoteOp {

	private JsonUtils jsonUtils;

	public GstDockerOperation(AudioDevNodeConnection conn, JsonUtils jsonUtils) {
		super(conn);
		this.jsonUtils = jsonUtils;
	}

	@Override
	public boolean isCancelable() {
		return true;
	}

	@Override
	public void run(IProgressMonitor monitor) {
		int workload = 0;

		monitor.beginTask("Sending json to docker", workload);

		try {
			if (!conn.isConnected()) {
				monitor.done();
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						MessageDialog.openError(null, "Exception occured", "Node not connected");
					}
				});
				throw new InvocationTargetException(new IllegalStateException("Node not connected"));
			}

			PipelineJsonProperty jsonProperty = jsonUtils.getJsonProperty();

			AudioDevNodeProject proj = conn.getProject();
			Session session = conn.getSession();
			ChannelExec channel = (ChannelExec) session.openChannel("exec");
			channel.setInputStream(null);

			monitor.worked(1);

			String jsonFileName = "pipeline.json";
			String projectPath = proj.getProject().getLocation().toString();
			Path partPathToJson = Paths.get(jsonProperty.getType().toLowerCase(), jsonProperty.getName(),
					jsonProperty.getVersion());
			Path fullPathToJson = Paths.get(projectPath, partPathToJson.toString(), jsonFileName);
			String linuxPartPathToJson = partPathToJson.toString();
			linuxPartPathToJson = linuxPartPathToJson.replace("\\", "/");

			monitor.worked(1);

			BufferedReader reader = new BufferedReader(new FileReader(fullPathToJson.toString()));
			String currentLine = reader.readLine();
			reader.close();

			channel.setPty(true); // for ctrl+c sending
			String command = String.format(
					"cd /home/video-analytics/pipelines; mkdir -p %s; cd %s; touch %s; echo \'%s\' > %s",
					linuxPartPathToJson, linuxPartPathToJson, jsonFileName, currentLine, jsonFileName);
			channel.setCommand(command);

			channel.connect();

			while (!channel.isEOF())
				;

			channel.disconnect();

			Channel channel2 = session.openChannel("sftp");
			channel2.connect();
			ChannelSftp channelSftp = (ChannelSftp) channel2;

			if (exists(channelSftp,
					String.format("/home/video-analytics/pipelines/%s/%s", linuxPartPathToJson, jsonFileName))) {
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						MessageDialog.openInformation(null, "Success", "File saved and sent succesfully");
					}
				});
			}

			channelSftp.disconnect();
			channel2.disconnect();

			monitor.done();

		} catch (Exception e) {
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					MessageDialog.openError(null, "Exception occured", e.getMessage());
				}
			});
			e.printStackTrace();
		}
	}

	private static boolean exists(ChannelSftp channelSftp, String path) {
		Vector res = null;
		try {
			res = channelSftp.ls(path);
		} catch (SftpException e) {
			if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
				return false;
			}

			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					MessageDialog.openError(null, "Exception occured", String
							.format("Unexpected exception during ls files on sftp: [{%s}:{%s}]", e.id, e.getMessage()));
				}
			});
			e.printStackTrace();
		}
		return res != null && !res.isEmpty();
	}

}
