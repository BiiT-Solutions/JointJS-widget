package com.biit.jointjs.diagram.builder.client;

import com.vaadin.shared.communication.ClientRpc;

public interface DiagramBuilderClientRpc extends ClientRpc {

	void undo();

	void redo();

	void clear();

	void openAsSvg();

	void openAsPng();

	void zoomIn();

	void zoomOut();

	void print();

	void toFront();

	void toBack();

}
