package com.biit.jointjs.diagram.builder.client;

import com.biit.jointjs.diagram.builder.client.ui.VDiagramBuilder;
import com.biit.jointjs.diagram.builder.client.ui.events.PickedConnectionEvent;
import com.biit.jointjs.diagram.builder.client.ui.events.PickedConnectionHandler;
import com.biit.jointjs.diagram.builder.client.ui.events.PickedNodeEvent;
import com.biit.jointjs.diagram.builder.client.ui.events.PickedNodeHandler;
import com.biit.jointjs.diagram.builder.server.DiagramBuilder;
import com.google.gwt.core.client.GWT;
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
	}

	public DiagramBuilderConnector() {
		super();
		serverRpc = RpcProxy.create(DiagramBuilderServerRpc.class, this);
		registerRpc(DiagramBuilderClientRpc.class, new DiagramBuilderServerRpcImpl());
		getWidget().addPickedNodeHandler(new PickedNodeHandler() {
			public void pickedNode(PickedNodeEvent event) {
				serverRpc.pickedNode(event.getJsonString());
			}
		});
		getWidget().addPickedConnectionHandler(new PickedConnectionHandler() {
			public void pickedConnection(PickedConnectionEvent event) {
				serverRpc.pickedConnection(event.getJsonString());
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
