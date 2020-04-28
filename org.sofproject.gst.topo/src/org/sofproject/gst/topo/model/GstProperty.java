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

package org.sofproject.gst.topo.model;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.sofproject.topo.ui.graph.ITopoNodeAttribute;

public abstract class GstProperty implements ITopoNodeAttribute, Cloneable {

	private String name;
	private String category;
	private String description;
	private boolean readOnly;
	protected GstTopoNode owner;

	public GstProperty(String category, String name, String description, boolean readOnly) {
		this.category = category;
		this.name = name;
		this.description = description;
		this.readOnly = readOnly;
	}

	@Override
	public GstProperty clone() throws CloneNotSupportedException {
		return (GstProperty) super.clone();
	}

	public void setOwner(GstTopoNode owner) {
		this.owner = owner;
	}

	@Override
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	// name is category
	@Override
	public String getCategory() {
		return category;
	}

	public String getPropertyString(String nodeName) {
		return "";
	}

	@Override
	public String getStringValue() {
		Object value = getValue();
		return value != null ? value.toString() : "(null)";
	}

	public abstract Object getDefaultValue();

	@Override
	public List<String> getEnumValues() {
		return new ArrayList<String>();
	}

	@Override
	public boolean isChanged() {
		return !getValue().equals(getDefaultValue());
	}

	@Override
	public boolean isReadOnly() {
		return readOnly;
	}

	public void serialize(Writer writer) throws IOException {
		if (name.equals("value")) {
			writer.write(" ");
			writer.write(category + "=" + getStringValue());
		}
	}

	public void serializePipelineProperties(Writer writer, GstProperty previousProperty, String nodeName)
			throws IOException {
		if (name.equals("pipeline properties") && getStringValue().equals("true")) {
			if (previousProperty != null)
				writer.write(previousProperty.getPropertyString(nodeName));

		}

	}
}
