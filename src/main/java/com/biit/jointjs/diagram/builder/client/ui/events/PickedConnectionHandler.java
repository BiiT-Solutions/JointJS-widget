package com.biit.jointjs.diagram.builder.client.ui.events;

import com.google.gwt.event.shared.EventHandler;

public interface PickedConnectionHandler extends EventHandler{

	void pickedConnection(PickedConnectionEvent event);
	
}
