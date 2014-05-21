package com.biit.jointjs.diagram.builder.client.ui.events;

import com.google.gwt.event.shared.EventHandler;

public interface PickedNodeHandler extends EventHandler {

	void pickedNode(PickedNodeEvent event);
	
}
