package com.biit.jointjs.diagram.builder.client.ui.events;

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
