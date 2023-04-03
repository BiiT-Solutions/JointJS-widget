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

import com.biit.jointjs.diagram.builder.client.ui.events.*;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;

public class VDiagramBuilder extends ResizeLayoutPanel implements HasHandlers {

    /**
     * Set the CSS class name to allow styling.
     */
    public static final String CLASSNAME = "vdiagram-builder";
    public static final String COMPONENT_ID = "diagram-builder-html";
    public static final String COMPONENT_CLASS = "paper";
    public static final String STENCIL_CONTAINER_CLASS = "stencil-container";
    public static final String STENCIL_CAPTION_ID = "stencil-caption";
    public static final String PAPER_CONTAINER_CLASS = "paper-container";
    public static final String STATUSBAR_CONTAINER_CLASS = "statusbar-container";

    private Element vaadinElement;

    /**
     * The constructor should first call super() to initialize the component and
     * then handle any initialization relevant to Vaadin.
     */
    public VDiagramBuilder() {
        // The superclass has a lot of relevant initialization
        super();
        setStyleName(CLASSNAME);

        vaadinElement = getElement();
    }

    private Element createDiagramBuilderElement() {
        Element diagramBuilder = createElement(COMPONENT_ID, null);

        Element stencilContainer = createStencilContainer();
        Element paperContainer = createElement(null, PAPER_CONTAINER_CLASS);
        // Element statusBarContainer = createElement(null,
        // STATUSBAR_CONTAINER_CLASS);

        diagramBuilder.appendChild(stencilContainer);
        diagramBuilder.appendChild(paperContainer);
        // diagramBuilder.appendChild(statusBarContainer);

        return diagramBuilder;
    }

    private Element createStencilContainer() {
        Element stencilContainer = createElement(null, STENCIL_CONTAINER_CLASS);
        Element stencilCaption = DOM.createDiv();
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
        vaadinElement.insertFirst(createDiagramBuilderElement());
        init();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onUnload() {
        stop();
        NodeList<Node> childNodes = vaadinElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            childNodes.getItem(i).removeFromParent();
        }
        super.onUnload();
    }

    public native void init()
	/*-{
	 	$wnd.app = new $wnd.Rappid;
	    $wnd.Backbone.history.start();
	    $wnd.javaInstance = this;
	    $wnd.enabledListeners = true;
	  	$wnd.firePickedNodeHandler = function(jsonString) {
	  		if($wnd.enabledListeners){
	  			$wnd.javaInstance.@com.biit.jointjs.diagram.builder.client.ui.VDiagramBuilder::firePickedNodeHandler(Ljava/lang/String;)(jsonString);
	  		}
	  	};
	  	$wnd.firePickedConnectionHandler = function(jsonString) {
	  		if($wnd.enabledListeners){
	  			$wnd.javaInstance.@com.biit.jointjs.diagram.builder.client.ui.VDiagramBuilder::firePickedConnectionHandler(Ljava/lang/String;)(jsonString);
	  		}
	  	};
	  	$wnd.fireDoubleClickNodeHandler = function(jsonString) {
	  		if($wnd.enabledListeners){
	  			$wnd.javaInstance.@com.biit.jointjs.diagram.builder.client.ui.VDiagramBuilder::fireDoubleClickNodeHandler(Ljava/lang/String;)(jsonString);
	  		}
	  	};
	  	$wnd.fireAddElementHandler = function(jsonString) {
	  		if($wnd.enabledListeners){
	  			$wnd.javaInstance.@com.biit.jointjs.diagram.builder.client.ui.VDiagramBuilder::fireAddElementHandler(Ljava/lang/String;)(jsonString);
	  		}
	  	};
	  	$wnd.fireUpdateElementHandler = function(jsonString) {
	  		if($wnd.enabledListeners){
	  			$wnd.javaInstance.@com.biit.jointjs.diagram.builder.client.ui.VDiagramBuilder::fireUpdateElementHandler(Ljava/lang/String;)(jsonString);
	  		}
	  	};
	  	$wnd.fireRemoveElementHandler = function(jsonString) {
	  		if($wnd.enabledListeners){
	  			$wnd.javaInstance.@com.biit.jointjs.diagram.builder.client.ui.VDiagramBuilder::fireRemoveElementHandler(Ljava/lang/String;)(jsonString);
	  		}
	  	};	
	}-*/
    ;

    public native void stop()
	/*-{
		$wnd.Backbone.history.stop();
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

    public native String toJson()
	/*-{
		return JSON.stringify($wnd.app.graph);
	}-*/
    ;

    public native void fromJson(String jsonString)
	/*-{
	 	$wnd.enabledListeners = false;
		$wnd.app.graph.fromJSON(JSON.parse(jsonString));
		$wnd.enabledListeners = true;
	}-*/
    ;

    public native void updateCellAttrs(String jsonString)
	/*-{
	 	$wnd.enabledListeners = false;
	 	$wnd.app.updateCellAttrs(JSON.parse(jsonString));
	 	$wnd.enabledListeners = true;
	}-*/
    ;

    public native void updateLinkAttrs(String jsonString)
	/*-{
		$wnd.enabledListeners = false;
	 	$wnd.app.updateLinkText(JSON.parse(jsonString));
	 	$wnd.enabledListeners = true;
	}-*/
    ;

    public native void clearSilently()
	/*-{
		$wnd.enabledListeners = false;
	 	$wnd.app.graph.clear();
	 	$wnd.enabledListeners = true;
	}-*/
    ;

    public HandlerRegistration addPickedNodeHandler(PickedNodeHandler handler) {
        return addHandler(handler, PickedNodeEvent.TYPE);
    }

    public HandlerRegistration addPickedConnectionHandler(PickedConnectionHandler handler) {
        return addHandler(handler, PickedConnectionEvent.TYPE);
    }

    public HandlerRegistration addDoubleClickNodeHandler(DoubleClickNodeHandler handler) {
        return addHandler(handler, DoubleClickNodeEvent.TYPE);
    }

    public HandlerRegistration addAddElementHandler(AddElementHandler handler) {
        return addHandler(handler, AddElementEvent.TYPE);
    }

    public HandlerRegistration addUpdateElementHandler(UpdateElementHandler handler) {
        return addHandler(handler, UpdateElementEvent.TYPE);
    }

    public HandlerRegistration addRemoveElementHandler(RemoveElementHandler handler) {
        return addHandler(handler, RemoveElementEvent.TYPE);
    }

    protected void firePickedNodeHandler(String jsonString) {
        fireEvent(new PickedNodeEvent(jsonString));
    }

    protected void firePickedConnectionHandler(String jsonString) {
        fireEvent(new PickedConnectionEvent(jsonString));
    }

    protected void fireDoubleClickNodeHandler(String jsonString) {
        fireEvent(new DoubleClickNodeEvent(jsonString));
    }

    protected void fireAddElementHandler(String jsonString) {
        fireEvent(new AddElementEvent(jsonString));
    }

    protected void fireUpdateElementHandler(String jsonString) {
        fireEvent(new UpdateElementEvent(jsonString));
    }

    protected void fireRemoveElementHandler(String jsonString) {
        fireEvent(new RemoveElementEvent(jsonString));
    }
}