package com.biit.jointjs.diagram.builder.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;

public class DoubleClickNodeEvent extends GwtEvent<DoubleClickNodeHandler> {

	public static final Type<DoubleClickNodeHandler> TYPE = new Type<DoubleClickNodeHandler>();

	private String jsonString;

	public DoubleClickNodeEvent(String jsonString) {
		this.jsonString = jsonString;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<DoubleClickNodeHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DoubleClickNodeHandler handler) {
		handler.doubleClickNode(this);
	}

	public String getJsonString() {
		return jsonString;
	}

}