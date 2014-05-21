/* 
 * Copyright 2009 IT Mill Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.biit.jointjs.diagram.builder.server;

import java.util.ArrayList;
import java.util.List;

import com.biit.jointjs.diagram.builder.client.DiagramBuilderClientRpc;
import com.biit.jointjs.diagram.builder.client.DiagramBuilderServerRpc;
import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractComponent;

/**
 * Diagram builder component based on JointJs javascript library and RapidJs
 * framework. Commercial license only.
 * 
 */
@JavaScript({ "vaadin://dist/joint.js",
		"vaadin://dist/joint.shapes.devs.js",
		"vaadin://dist/joint.shapes.fsa.js",
		"vaadin://dist/joint.shapes.pn.js",
		"vaadin://dist/joint.shapes.erd.js",
		"vaadin://dist/joint.shapes.uml.js",
		"vaadin://dist/joint.shapes.org.js",
		"vaadin://dist/joint.ui.halo.js",
		"vaadin://dist/joint.dia.freeTransform.js",
		"vaadin://dist/joint.ui.freeTransform.js",
		"vaadin://dist/joint.ui.inspector.js",
		"vaadin://dist/joint.ui.selectionView.js",
		"vaadin://dist/joint.ui.clipboard.js",
		"vaadin://dist/joint.ui.stencil.js",
		"vaadin://dist/joint.ui.paperScroller.js",
		"vaadin://dist/joint.ui.tooltip.js",
		"vaadin://dist/joint.format.svg.js",
		"vaadin://dist/joint.format.raster.js",
		"vaadin://dist/joint.format.print.js",
		"vaadin://dist/joint.dia.command.js",
		"vaadin://dist/joint.dia.validator.js",
		"vaadin://dist/joint.layout.ForceDirected.js",
		"vaadin://dist/joint.layout.GridLayout.js",
		"vaadin://dist/joint.layout.DirectedGraph.js",
		"vaadin://lib/keyboard.js",
		"vaadin://src/dataTypes.js",
		"vaadin://src/main.js", })
public class DiagramBuilder extends AbstractComponent {
	private static final long serialVersionUID = -6846503871084162514L;
	private DiagramBuilderServerRpc serverRpc;
	private List<DiagramBuilderJsonGenerationListener> jsonGenerationListeners;
	private List<ElementPickedListener> elementPickedListeners;

	private class DiagramBuilderServerRpcImp implements DiagramBuilderServerRpc {
		private static final long serialVersionUID = 6985050604356040816L;

		public void toJsonAnswer(String jsonString) {
			fireAndCleanListeners(jsonString);
		}

		public void pickedNode(String jsonString) {
			fireElementPickedListenersPickedNode(jsonString);
		}

		public void pickedConnection(String jsonString) {
			fireElementPickedListenersPickedConnection(jsonString);
		}		
	};

	public DiagramBuilder() {
		super();
		jsonGenerationListeners = new ArrayList<DiagramBuilderJsonGenerationListener>();
		elementPickedListeners = new ArrayList<ElementPickedListener>();
		serverRpc = new DiagramBuilderServerRpcImp();
		registerRpc(serverRpc);
	}

	private DiagramBuilderClientRpc getClientRpc() {
		return getRpcProxy(DiagramBuilderClientRpc.class);
	}

	public void undo() {
		getClientRpc().undo();
	}

	public void redo() {
		getClientRpc().redo();
	}

	public void clear() {
		getClientRpc().clear();
	}

	public void openAsSvg() {
		getClientRpc().openAsSvg();
	}

	public void openAsPng() {
		getClientRpc().openAsPng();
	}

	public void zoomIn() {
		getClientRpc().zoomIn();
	}

	public void zoomOut() {
		getClientRpc().zoomOut();
	}

	public void print() {
		getClientRpc().print();
	}

	public void toFront() {
		getClientRpc().toFront();
	}

	public void toBack() {
		getClientRpc().toBack();
	}

	public void toJson(DiagramBuilderJsonGenerationListener listener) {
		jsonGenerationListeners.add(listener);
		getClientRpc().toJsonQuery();
	}

	public void fromJson(String jsonString) {
		getRpcProxy(DiagramBuilderClientRpc.class).fromJson(jsonString);
	}

	public interface DiagramBuilderJsonGenerationListener {
		public void generatedJsonString(String jsonString);
	}

	private synchronized void fireAndCleanListeners(String jsonString) {
		for (DiagramBuilderJsonGenerationListener listener : jsonGenerationListeners) {
			listener.generatedJsonString(jsonString);
		}
		jsonGenerationListeners.clear();
	}
	
	public void addElementPickedListener(ElementPickedListener listener){
		elementPickedListeners.add(listener);
	}
	
	public void removeElementPickedListener(ElementPickedListener listener){
		elementPickedListeners.remove(listener);
	}
	
	private void fireElementPickedListenersPickedNode(String jsonString){
		for(ElementPickedListener listener: elementPickedListeners){
			listener.nodePickedListener(jsonString);
		}
	}
	
	private void fireElementPickedListenersPickedConnection(String jsonString){
		for(ElementPickedListener listener: elementPickedListeners){
			listener.connectionPickedListener(jsonString);
		}
	}
}
