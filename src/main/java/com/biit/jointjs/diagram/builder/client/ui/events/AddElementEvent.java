package com.biit.jointjs.diagram.builder.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;

public class AddElementEvent extends GwtEvent<AddElementHandler> {

	public static final Type<AddElementHandler> TYPE = new Type<>();

	private String jsonString;

	public AddElementEvent(String jsonString) {
		this.jsonString = jsonString;
	}

	@Override
	public Type<AddElementHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AddElementHandler handler) {
		handler.addElement(this);
	}

	public String getJsonString() {
		return jsonString;
	}

}
