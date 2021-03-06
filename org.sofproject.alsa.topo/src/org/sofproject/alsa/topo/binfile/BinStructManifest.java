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

import org.sofproject.core.binfile.BinByteArray;
import org.sofproject.core.binfile.BinInteger;
import org.sofproject.core.binfile.BinStruct;

public class BinStructManifest extends BinStruct {

	static final int RESERVED_SIZE = 20; // in dwords

	public BinStructManifest() {
		super("manifest");
		addChildItem(new BinInteger("size"));
		addChildItem(new BinInteger("control_elems"));
		addChildItem(new BinInteger("widget_elems"));
		addChildItem(new BinInteger("graph_elems"));
		addChildItem(new BinInteger("pcm_elems"));
		addChildItem(new BinInteger("dai_list_elems"));
		addChildItem(new BinInteger("dai_elems"));
		addChildItem(new BinByteArray("reserved", RESERVED_SIZE * 4));

		// private data in the manifest seems to be just an array of bytes
		// not formatted as tuples
		BinInteger privSize = new BinInteger("priv_size");
		addChildItem(privSize);
		addChildItem(new BinByteArray("priv_data", privSize));
	}
}
