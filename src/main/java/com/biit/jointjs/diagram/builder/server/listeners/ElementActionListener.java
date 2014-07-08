package com.biit.jointjs.diagram.builder.server.listeners;

public interface ElementActionListener {

	public abstract void addElement(String jsonString);
	
	public abstract void updateElement(String jsonString);
	
	public abstract void removeElement(String jsonString);
	
}
