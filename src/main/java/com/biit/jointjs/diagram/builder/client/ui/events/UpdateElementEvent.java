package com.biit.jointjs.diagram.builder.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;

public class UpdateElementEvent extends GwtEvent<UpdateElementHandler> {

	public static final Type<UpdateElementHandler> TYPE = new Type<UpdateElementHandler>();

	private String jsonString;

	public UpdateElementEvent(String jsonString) {
		this.jsonString = jsonString;
	}

	@Override
	public Type<UpdateElementHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(UpdateElementHandler handler) {
		handler.updateElement(this);
	}

	public String getJsonString() {
		return jsonString;
	}

}
