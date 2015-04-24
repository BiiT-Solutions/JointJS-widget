package com.biit.jointjs.diagram.builder.client;

import com.biit.jointjs.diagram.builder.client.ui.VDiagramBuilder;
import com.biit.jointjs.diagram.builder.client.ui.events.AddElementEvent;
import com.biit.jointjs.diagram.builder.client.ui.events.AddElementHandler;
import com.biit.jointjs.diagram.builder.client.ui.events.DoubleClickNodeEvent;
import com.biit.jointjs.diagram.builder.client.ui.events.DoubleClickNodeHandler;
import com.biit.jointjs.diagram.builder.client.ui.events.PickedConnectionEvent;
import com.biit.jointjs.diagram.builder.client.ui.events.PickedConnectionHandler;
import com.biit.jointjs.diagram.builder.client.ui.events.PickedNodeEvent;
import com.biit.jointjs.diagram.builder.client.ui.events.PickedNodeHandler;
import com.biit.jointjs.diagram.builder.client.ui.events.RemoveElementEvent;
import com.biit.jointjs.diagram.builder.client.ui.events.RemoveElementHandler;
import com.biit.jointjs.diagram.builder.client.ui.events.UpdateElementEvent;
import com.biit.jointjs.diagram.builder.client.ui.events.UpdateElementHandler;
import com.biit.jointjs.diagram.builder.server.DiagramBuilder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

@Connect(DiagramBuilder.class)
public class DiagramBuilderConnector extends AbstractComponentConnector {
	private static final long serialVersionUID = 5813808256282702315L;

	private DiagramBuilderServerRpc serverRpc;

	private class DiagramBuilderServerRpcImpl implements DiagramBuilderClientRpc {
		private static final long serialVersionUID = -3927060112921863445L;

		public void undo() {
			getWidget().undo();
		}

		public void redo() {
			getWidget().redo();
		}

		public void clear() {
			getWidget().clear();
		}

		public void openAsSvg() {
			getWidget().openAsSvg();
		}

		public void openAsPng() {
			getWidget().openAsPng();
		}

		public void zoomIn() {
			getWidget().zoomIn();
		}

		public void zoomOut() {
			getWidget().zoomOut();
		}

		public void print() {
			getWidget().print();
		}

		public void toFront() {
			getWidget().toFront();
		}

		public void toBack() {
			getWidget().toBack();
		}

		public void toJsonQuery() {
			String jsonString = getWidget().toJson();
			serverRpc.toJsonAnswer(jsonString);
		}

		public void fromJson(String jsonString) {
			getWidget().fromJson(jsonString);
		}

		public void updateCellJson(String jsonString) {
			getWidget().updateCellAttrs(jsonString);
		}

		public void updateLinkJson(String jsonString) {
			getWidget().updateLinkAttrs(jsonString);
		}

		public void clearSilently() {
			getWidget().clearSilently();
		}
	}

	public DiagramBuilderConnector() {
		super();
		serverRpc = RpcProxy.create(DiagramBuilderServerRpc.class, this);
		registerRpc(DiagramBuilderClientRpc.class, new DiagramBuilderServerRpcImpl());
		getWidget().addPickedNodeHandler(new PickedNodeHandler() {
			public void pickedNode(PickedNodeEvent event) {
				if (isEnabled()) {
					serverRpc.getFocus();
					serverRpc.pickedNode(event.getJsonString());
				}
			}
		});
		getWidget().sinkEvents(Event.ONCLICK);
		getWidget().addDomHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (isEnabled()) {
					serverRpc.getFocus();
				}
			}
		}, ClickEvent.getType());
		getWidget().addPickedConnectionHandler(new PickedConnectionHandler() {
			public void pickedConnection(PickedConnectionEvent event) {
				if (isEnabled()) {
					serverRpc.getFocus();
					serverRpc.pickedConnection(event.getJsonString());
				}
			}
		});
		getWidget().addDoubleClickNodeHandler(new DoubleClickNodeHandler() {
			public void doubleClickNode(DoubleClickNodeEvent event) {
				if (isEnabled()) {
					serverRpc.getFocus();
					serverRpc.doubleClickNode(event.getJsonString());
				}
			}
		});
		getWidget().addAddElementHandler(new AddElementHandler() {
			public void addElement(AddElementEvent event) {
				if (isEnabled()) {
					serverRpc.getFocus();
					serverRpc.addElement(event.getJsonString());
				}else{
					getWidget().clear();
				}
			}
		});
		getWidget().addUpdateElementHandler(new UpdateElementHandler() {
			public void updateElement(UpdateElementEvent event) {
				if (isEnabled()) {
					serverRpc.getFocus();
					serverRpc.updateElement(event.getJsonString());
				}else{
					getWidget().clear();
				}
			}
		});
		getWidget().addRemoveElementHandler(new RemoveElementHandler() {
			public void removeElement(RemoveElementEvent event) {
				if (isEnabled()) {
					serverRpc.getFocus();
					serverRpc.removeElement(event.getJsonString());
				}else{
					getWidget().clear();
				}
			}
		});
	}

	@Override
	protected Widget createWidget() {
		return GWT.create(VDiagramBuilder.class);
	}

	@Override
	public VDiagramBuilder getWidget() {
		return (VDiagramBuilder) super.getWidget();
	}

}
