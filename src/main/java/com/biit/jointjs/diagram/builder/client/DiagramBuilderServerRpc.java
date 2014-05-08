package com.biit.jointjs.diagram.builder.client;

import com.vaadin.shared.communication.ServerRpc;

public interface DiagramBuilderServerRpc extends ServerRpc{

	void toJsonAnswer(String jsonString);

}
