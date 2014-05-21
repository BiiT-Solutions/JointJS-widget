package com.biit.jointjs.diagram.builder.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;

public class PickedConnectionEvent extends GwtEvent<PickedConnectionHandler>{

	public static Type<PickedConnectionHandler> TYPE = new Type<PickedConnectionHandler>();
	
	private String jsonString;
	
	public PickedConnectionEvent(String jsonString) {
		this.jsonString = jsonString;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<PickedConnectionHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PickedConnectionHandler handler) {
		handler.pickedConnection(this);
	}
	
	public String getJsonString(){
		return jsonString;
	}

}
