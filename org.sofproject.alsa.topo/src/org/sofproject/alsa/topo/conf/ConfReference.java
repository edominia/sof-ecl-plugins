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

package org.sofproject.alsa.topo.conf;

import java.io.IOException;
import java.io.Writer;

/**
 * Attribute that is a reference to another top level element (serialized as a
 * uniquely named section). Value of the attribute is the unique name of
 * referenced element.
 */
public class ConfReference extends ConfAttribute {

	/**
	 * Reference to another element.
	 */
	private ConfElement value;

	public ConfReference(String name) {
		super(name);
	}

	public ConfReference(String name, ConfElement value) {
		super(name);
		this.value = value;
	}

	public void setRefValue(ConfElement value) {
		this.value = value;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public boolean isChanged() {
		return value != null;
	}

	@Override
	public void setValue(Object value) {
		if (!(value instanceof ConfElement))
			throw new RuntimeException("Expected ConfElement value");
		setRefValue((ConfElement) value);

	}

	@Override
	public Type getNodeAtrributeType() {
		return Type.NODE_A_REFERENCE;
	}

	@Override
	public void serialize(Writer writer, String indent) throws IOException {
		if (isChanged()) {
			writer.write(indent);
			writer.write(getName());
			writer.write(" \"");
			writer.write(value.getName()); // default getStringValue() incudes category name
			writer.write("\"\n");
		}
	}

}
