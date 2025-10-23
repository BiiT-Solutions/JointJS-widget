package com.biit.jointjs.diagram.builder.client.ui.events;

/*-
 * #%L
 * JointJs Diagram Builder for Vaadin
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.google.gwt.event.shared.GwtEvent;

public class RemoveElementEvent extends GwtEvent<RemoveElementHandler> {

	public static final Type<RemoveElementHandler> TYPE = new Type<RemoveElementHandler>();

	private String jsonString;

	public RemoveElementEvent(String jsonString) {
		this.jsonString = jsonString;
	}

	@Override
	public Type<RemoveElementHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(RemoveElementHandler handler) {
		handler.removeElement(this);
	}

	public String getJsonString() {
		return jsonString;
	}

}
