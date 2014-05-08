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

@JavaScript({"vaadin://widgetsets/com.biit.jointjs.diagram.builder.JointJsWidgetBuilder/dist/joint.js",
	"vaadin://widgetsets/com.biit.jointjs.diagram.builder.JointJsWidgetBuilder/dist/joint.shapes.devs.js",
	"vaadin://widgetsets/com.biit.jointjs.diagram.builder.JointJsWidgetBuilder/dist/joint.shapes.fsa.js",
	"vaadin://widgetsets/com.biit.jointjs.diagram.builder.JointJsWidgetBuilder/dist/joint.shapes.pn.js",
	"vaadin://widgetsets/com.biit.jointjs.diagram.builder.JointJsWidgetBuilder/dist/joint.shapes.erd.js",
	"vaadin://widgetsets/com.biit.jointjs.diagram.builder.JointJsWidgetBuilder/dist/joint.shapes.uml.js",
	"vaadin://widgetsets/com.biit.jointjs.diagram.builder.JointJsWidgetBuilder/dist/joint.shapes.org.js",
	"vaadin://widgetsets/com.biit.jointjs.diagram.builder.JointJsWidgetBuilder/dist/joint.ui.halo.js",
	"vaadin://widgetsets/com.biit.jointjs.diagram.builder.JointJsWidgetBuilder/dist/joint.dia.freeTransform.js",
	"vaadin://widgetsets/com.biit.jointjs.diagram.builder.JointJsWidgetBuilder/dist/joint.ui.freeTransform.js",
	"vaadin://widgetsets/com.biit.jointjs.diagram.builder.JointJsWidgetBuilder/dist/joint.ui.inspector.js",
	"vaadin://widgetsets/com.biit.jointjs.diagram.builder.JointJsWidgetBuilder/dist/joint.ui.selectionView.js",
	"vaadin://widgetsets/com.biit.jointjs.diagram.builder.JointJsWidgetBuilder/dist/joint.ui.clipboard.js",
	"vaadin://widgetsets/com.biit.jointjs.diagram.builder.JointJsWidgetBuilder/dist/joint.ui.stencil.js",
	"vaadin://widgetsets/com.biit.jointjs.diagram.builder.JointJsWidgetBuilder/dist/joint.ui.paperScroller.js",
	"vaadin://widgetsets/com.biit.jointjs.diagram.builder.JointJsWidgetBuilder/dist/joint.ui.tooltip.js",
	"vaadin://widgetsets/com.biit.jointjs.diagram.builder.JointJsWidgetBuilder/dist/joint.format.svg.js",
	"vaadin://widgetsets/com.biit.jointjs.diagram.builder.JointJsWidgetBuilder/dist/joint.format.raster.js",
	"vaadin://widgetsets/com.biit.jointjs.diagram.builder.JointJsWidgetBuilder/dist/joint.format.print.js",
	"vaadin://widgetsets/com.biit.jointjs.diagram.builder.JointJsWidgetBuilder/dist/joint.dia.command.js",
	"vaadin://widgetsets/com.biit.jointjs.diagram.builder.JointJsWidgetBuilder/dist/joint.dia.validator.js",
	"vaadin://widgetsets/com.biit.jointjs.diagram.builder.JointJsWidgetBuilder/dist/joint.layout.ForceDirected.js",
	"vaadin://widgetsets/com.biit.jointjs.diagram.builder.JointJsWidgetBuilder/dist/joint.layout.GridLayout.js",
	"vaadin://widgetsets/com.biit.jointjs.diagram.builder.JointJsWidgetBuilder/dist/joint.layout.DirectedGraph.js",
	"vaadin://widgetsets/com.biit.jointjs.diagram.builder.JointJsWidgetBuilder/lib/keyboard.js",
	"vaadin://widgetsets/com.biit.jointjs.diagram.builder.JointJsWidgetBuilder/src/inspector.js",
	"vaadin://widgetsets/com.biit.jointjs.diagram.builder.JointJsWidgetBuilder/src/dataTypes.js",
	"vaadin://widgetsets/com.biit.jointjs.diagram.builder.JointJsWidgetBuilder/src/main.js",
	})
public class DiagramBuilder extends AbstractComponent {
	private static final long serialVersionUID = -6846503871084162514L;
	private DiagramBuilderServerRpc serverRpc;
	private List<DiagramBuilderJsonGenerationListener> listeners;

	private class DiagramBuilderServerRpcImp implements DiagramBuilderServerRpc {
		private static final long serialVersionUID = 6985050604356040816L;

		public void toJsonAnswer(String jsonString) {
			fireAndCleanListeners(jsonString);
		}
	};

	public DiagramBuilder() {
		super();
		listeners = new ArrayList<DiagramBuilderJsonGenerationListener>();
		serverRpc = new DiagramBuilderServerRpcImp();
		registerRpc(serverRpc);
	}

	private DiagramBuilderClientRpc getClientRpc() {
		return getRpcProxy(DiagramBuilderClientRpc.class);
	}

	public void undo(){
		getClientRpc().undo();
	}
	
	public void redo(){
		getClientRpc().redo();
	}
	
	public void clear(){
		getClientRpc().clear();
	}
	
	public void openAsSvg(){
		getClientRpc().openAsSvg();
	}
	
	public void openAsPng(){
		getClientRpc().openAsPng();
	}
	
	public void zoomIn(){
		getClientRpc().zoomIn();
	}
	
	public void zoomOut(){
		getClientRpc().zoomOut();
	}
	
	public void print(){
		getClientRpc().print();
	}
	
	public void toFront(){
		getClientRpc().toFront();
	}
	
	public void toBack(){
		getClientRpc().toBack();
	}
	
	public void toJson(DiagramBuilderJsonGenerationListener listener){
		listeners.add(listener);
		getClientRpc().toJsonQuery();
	}
	
	public void fromJson(String jsonString){
		getRpcProxy(DiagramBuilderClientRpc.class).fromJson(jsonString);
	}
	
	public interface DiagramBuilderJsonGenerationListener {
		public void generatedJsonString(String jsonString);		
	}
	
	private synchronized void fireAndCleanListeners(String jsonString){
		for(DiagramBuilderJsonGenerationListener listener: listeners){
			listener.generatedJsonString(jsonString);
		}
		listeners.clear();
	}
}
