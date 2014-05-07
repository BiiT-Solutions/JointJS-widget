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

package com.biit.jointjs.diagram.builder.client.ui;

import java.util.logging.Logger;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;

public class VDiagramBuilder extends ResizeLayoutPanel {

	/** Set the CSS class name to allow styling. */
	public static final String CLASSNAME = "vdiagram-builder";
	public static final String COMPONENT_ID = "diagram-builder-html";
	public static final String COMPONENT_CLASS = "paper";
	public static final String STENCIL_CONTAINER_CLASS = "stencil-container";
	public static final String STENCIL_CAPTION_ID = "stencil-caption";
	public static final String PAPER_CONTAINER_CLASS = "paper-container";
	public static final String INSPECTOR_CONTAINER_CLASS = "inspector-container";
	public static final String STATUSBAR_CONTAINER_CLASS = "statusbar-container";

	private Element vaadinElement;
	
	private boolean initializated = false;

	/**
	 * The constructor should first call super() to initialize the component and
	 * then handle any initialization relevant to Vaadin.
	 */
	public VDiagramBuilder() {
		// The superclass has a lot of relevant initialization
		super();
		setStyleName(CLASSNAME);

		vaadinElement = getElement();
		vaadinElement.insertFirst(createDiagramBuilderElement());
		
		initializated = false;

		Logger.getLogger("kiwi").severe("create");
	}

	private Element createDiagramBuilderElement() {
		Element diagramBuilder = createElement(COMPONENT_ID,null);
		
		Element stencilContainer = createStencilContainer();
		Element paperContainer = createElement(null, PAPER_CONTAINER_CLASS);
		Element inspectorContainer = createElement(null, INSPECTOR_CONTAINER_CLASS);
		Element statusBarContainer = createElement(null, STATUSBAR_CONTAINER_CLASS);
		
		diagramBuilder.appendChild(stencilContainer);
		diagramBuilder.appendChild(paperContainer);
		diagramBuilder.appendChild(inspectorContainer);
		diagramBuilder.appendChild(statusBarContainer);
		
		return diagramBuilder;
	}
	
	private Element createStencilContainer(){
		Element stencilContainer = createElement(null, STENCIL_CONTAINER_CLASS);
		Element stencilCaption = DOM.createLabel();
		stencilCaption.setId(STENCIL_CAPTION_ID);
		stencilCaption.setInnerText("stencil-caption");
		stencilContainer.appendChild(stencilCaption);
		return stencilContainer;
	}

	private Element createElement(String id, String className) {
		Element element = DOM.createDiv();
		if (id != null) {
			element.setId(id);
		}
		if (className != null) {
			element.setClassName(className);
		}
		return element;
	}

	@Override
	public void onAttach() {
		super.onAttach();

		Logger.getLogger("kiwi").severe("attach");
		if(!initializated){
			initializated=true;
			init();			
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Logger.getLogger("kiwi").severe("detach");
	}

	@Override
	public void onLoad() {
		super.onLoad();

		Logger.getLogger("kiwi").severe("load");

	}

	@Override
	public void onUnload() {
		// NodeList<Node> childNodes = paperElement.getChildNodes();
		// for (int i = 0; i < childNodes.getLength(); i++) {
		// childNodes.getItem(i).removeFromParent();
		// }
		super.onUnload();
		Logger.getLogger("kiwi").severe("unload");
	}

	public native void init()
	/*-{	 	
	 	$wnd.console.error("init");	  
	 	$wnd.app = new $wnd.Rappid;
        $wnd.Backbone.history.start();
	}-*/
	;

	public native void undo()
	/*-{
		$wnd.app.commandManager.undo();
	}-*/
	;
	
	public native void redo()
	/*-{
		$wnd.app.commandManager.redo();
	}-*/
	;
	
	public native void clear()
	/*-{
		$wnd.app.graph.clear();
	}-*/
	;
	
	public native void openAsSvg()
	/*-{
		$wnd.app.paper.openAsSVG();
	}-*/
	;
	
	public native void openAsPng()
	/*-{
		$wnd.app.openAsPNG();
	}-*/
	;
	
	public native void zoomIn()
	/*-{
		$wnd.app.zoomIn();
	}-*/
	;
	
	public native void zoomOut()
	/*-{
		$wnd.app.zoomOut();
	}-*/
	;
	
	public native void print()
	/*-{
		$wnd.app.paper.print({detachBody : false});
	}-*/
	;
	
	public native void toFront()
	/*-{
		$wnd.app.selection.invoke('toFront');
	}-*/
	;
	
	public native void toBack()
	/*-{
		$wnd.app.selection.invoke('toBack');
	}-*/
	;
}