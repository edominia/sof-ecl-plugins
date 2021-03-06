/*
 * Copyright (c) 2018, Intel Corporation
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

package org.sofproject.alsa.topo.binfile;

import org.sofproject.core.binfile.BinArray;
import org.sofproject.core.binfile.BinInteger;
import org.sofproject.core.binfile.BinString;
import org.sofproject.core.binfile.BinStruct;

public class BinStructLinkConfig extends BinStruct {

	public BinStructLinkConfig() {
		super("link_config");
		addChildItem(new BinInteger("size"));
		addChildItem(new BinInteger("id"));
		addChildItem(new BinString("name", BinTopoCommons.ELEM_ID_NAME_MAXLEN));
		addChildItem(new BinString("stream_name", BinTopoCommons.ELEM_ID_NAME_MAXLEN));
		addChildItem(new BinArray<BinStructStream>(BinStructStream.class, "stream",
				BinTopoCommons.STREAM_CONFIG_MAX));
		addChildItem(new BinInteger("num_streams"));
		addChildItem(new BinArray<BinStructHwConfig>(BinStructHwConfig.class, "hw_config",
				BinTopoCommons.HW_CONFIG_MAX));
		addChildItem(new BinInteger("num_hw_configs"));
		addChildItem(new BinInteger("default_hw_config_id"));
		addChildItem(new BinInteger("flag_mask"));
		addChildItem(new BinInteger("flags"));
		addChildItem(new BinStructPrivate("priv"));
	}

	@Override
	public String getValueString() {
		return (String) getChildValue("name");
	}

}
