package com.biit.jointjs.diagram.builder.demoproject;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;

@WebServlet(value = "/*", asyncSupported = true)
@VaadinServletConfiguration(productionMode = false, ui = JointJsDiagramBuilderUi.class)
public class JointJsDiagramBuilderProject extends VaadinServlet {

	private static final long serialVersionUID = -888532724472137292L;

}