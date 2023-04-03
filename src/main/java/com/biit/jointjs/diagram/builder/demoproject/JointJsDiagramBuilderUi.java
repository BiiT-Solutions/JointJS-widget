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

package com.biit.jointjs.diagram.builder.demoproject;

import com.biit.jointjs.diagram.builder.server.DiagramBuilder;
import com.biit.jointjs.diagram.builder.server.DiagramBuilder.DiagramBuilderJsonGenerationListener;
import com.biit.jointjs.diagram.builder.server.listeners.DoubleClickListener;
import com.biit.jointjs.diagram.builder.server.listeners.ElementActionListener;
import com.biit.jointjs.diagram.builder.server.listeners.ElementPickedListener;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Title("JointJs DiagramBuilder Add-on Demo")
@Widgetset("com.biit.jointjs.diagram.builder.JointJsWidgetBuilder")
public class JointJsDiagramBuilderUi extends UI {
    private static final long serialVersionUID = 294546208568012664L;

    private DiagramBuilder diagramBuilder = new DiagramBuilder();
    private String jsonStringData;

    @Override
    protected void init(VaadinRequest request) {
        jsonStringData = null;

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSizeFull();
        setContent(layout);

        diagramBuilder.setSizeFull();

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setHeight(null);
        Button undoButton = new Button("Undo", new ClickListener() {
            private static final long serialVersionUID = 4788920350294413743L;

            public void buttonClick(ClickEvent event) {
                diagramBuilder.undo();
            }
        });
        buttons.addComponent(undoButton);
        Button redoButton = new Button("Redo", new ClickListener() {
            private static final long serialVersionUID = 4788920350294413743L;

            public void buttonClick(ClickEvent event) {
                diagramBuilder.redo();
            }
        });
        buttons.addComponent(redoButton);
        Button clearButton = new Button("Clear", new ClickListener() {
            private static final long serialVersionUID = 4788920350294413743L;

            public void buttonClick(ClickEvent event) {
                diagramBuilder.clear();
            }
        });
        buttons.addComponent(clearButton);
        Button svgButton = new Button("Svg", new ClickListener() {
            private static final long serialVersionUID = 4788920350294413743L;

            public void buttonClick(ClickEvent event) {
                diagramBuilder.openAsSvg();
            }
        });
        buttons.addComponent(svgButton);
        Button pngButton = new Button("Png", new ClickListener() {
            private static final long serialVersionUID = 4788920350294413743L;

            public void buttonClick(ClickEvent event) {
                diagramBuilder.openAsPng();
            }
        });
        buttons.addComponent(pngButton);
        Button zoomInButton = new Button("zoomIn", new ClickListener() {
            private static final long serialVersionUID = 4788920350294413743L;

            public void buttonClick(ClickEvent event) {
                diagramBuilder.zoomIn();
            }
        });
        buttons.addComponent(zoomInButton);
        Button zoomOutButton = new Button("zoomOut", new ClickListener() {
            private static final long serialVersionUID = 4788920350294413743L;

            public void buttonClick(ClickEvent event) {
                diagramBuilder.zoomOut();
            }
        });
        buttons.addComponent(zoomOutButton);
        Button printButton = new Button("print", new ClickListener() {
            private static final long serialVersionUID = 4788920350294413743L;

            public void buttonClick(ClickEvent event) {
                diagramBuilder.print();
            }
        });
        buttons.addComponent(printButton);
        Button toFrontButton = new Button("toFront", new ClickListener() {
            private static final long serialVersionUID = 4788920350294413743L;

            public void buttonClick(ClickEvent event) {
                diagramBuilder.toFront();
            }
        });
        buttons.addComponent(toFrontButton);
        Button toBackButton = new Button("toBack", new ClickListener() {
            private static final long serialVersionUID = 4788920350294413743L;

            public void buttonClick(ClickEvent event) {
                diagramBuilder.toBack();
            }
        });
        buttons.addComponent(toBackButton);
        Button saveButton = new Button("save", new ClickListener() {
            private static final long serialVersionUID = 4788920350294413743L;

            public void buttonClick(ClickEvent event) {
                diagramBuilder.toJson(new DiagramBuilderJsonGenerationListener() {

                    public void generatedJsonString(String jsonString) {
                        jsonStringData = jsonString;
                        System.out.println(jsonStringData);
                    }
                });
            }
        });
        buttons.addComponent(saveButton);
        Button loadButton = new Button("load", new ClickListener() {
            private static final long serialVersionUID = -6266207671920614850L;

            public void buttonClick(ClickEvent event) {
                if (jsonStringData != null) {
                    diagramBuilder.fromJson(jsonStringData);
                } else {
                    System.out.println("No data to load");
                }
            }
        });
        buttons.addComponent(loadButton);
        Button enableButton = new Button("Enable", new ClickListener() {
            private static final long serialVersionUID = -6266207671920614850L;

            public void buttonClick(ClickEvent event) {
                diagramBuilder.setEnabled(!diagramBuilder.isEnabled());
            }
        });
        buttons.addComponent(enableButton);

        diagramBuilder.addElementPickedListener(new ElementPickedListener() {

            public void nodePickedListener(String jsonString) {
                System.out.println("SingleClickNode: " + jsonString);
                if (jsonString == null || jsonString.contains(".biitText")) {
                    return;
                }

                int indexAttrs = jsonString.indexOf("\"attrs\":{");
                String beg = jsonString.substring(0, indexAttrs + 9);
                String end = jsonString.substring(indexAttrs + 9);
                String newJsonString = beg
                        + "\".biitText\":{ \"text\":\"AVeeeeeeeee\", \"fill\":\"#000000\", \"font-size\":\"16\", \"stroke\":\"#000000\", \"stroke-width\":\"0\" }"
                        + end;
                diagramBuilder.updateCellJson(newJsonString);
            }

            public void connectionPickedListener(String jsonString) {
                System.out.println("connection: " + jsonString);
                if (jsonString == null) {
                    return;
                }

                int indexAttrs = jsonString.indexOf("\"labels\":[");
                String newJsonString;
                if (indexAttrs == -1) {
                    indexAttrs = jsonString.indexOf("\"attrs\":{");
                    String beg = jsonString.substring(0, indexAttrs);
                    String end = jsonString.substring(indexAttrs);
                    newJsonString = beg
                            + " \"labels\":[{\"attrs\":{\"text\":{\"text\":\"Kiwiiiiiiiiii\"}},\"position\":\"0.5\"}], "
                            + end;
                } else {
                    String beg = jsonString.substring(0, indexAttrs + 10);
                    String end = jsonString.substring(indexAttrs + 10);
                    newJsonString = beg
                            + "{\"attrs\":{\"text\":{\"text\":\"Kiwiiiiiiiiii\"}},\"position\":\"0.5\"}"
                            + end;
                }
                diagramBuilder.updateLinkJson(newJsonString);
            }
        });
        diagramBuilder.addDoubleClickListener(new DoubleClickListener() {
            public void doubleClick(String jsonString) {
                System.out.println("Double click! " + jsonString);
            }
        });

        diagramBuilder.addElementActionListener(new ElementActionListener() {
            public void updateElement(String jsonString) {
                System.out.println("Element Update: " + jsonString);
            }

            public void removeElement(String jsonString) {
                System.out.println("Element Remove: " + jsonString);
            }

            public void addElement(String jsonString) {
                System.out.println("Element Add: " + jsonString);
            }
        });

        layout.addComponent(buttons);
        layout.setExpandRatio(buttons, 0.0f);
        layout.addComponent(diagramBuilder);
        layout.setExpandRatio(diagramBuilder, 1.0f);

    }
}
