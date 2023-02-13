package com.biit.jointjs.diagram.builder.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;

public class PickedNodeEvent extends GwtEvent<PickedNodeHandler>{
	
	public static final Type<PickedNodeHandler> TYPE = new Type<>();

	private String jsonString;
	
	public PickedNodeEvent(String jsonString) {
		this.jsonString = jsonString;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<PickedNodeHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PickedNodeHandler handler) {
		handler.pickedNode(this);
	}

	public String getJsonString(){
		return jsonString;
	}
}
