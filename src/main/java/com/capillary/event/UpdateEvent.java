package com.capillary.event;

import org.springframework.context.ApplicationEvent;

import com.capillary.domain.Node;

public class UpdateEvent extends ApplicationEvent {

	private Node node;
	
	public UpdateEvent(Object source, Node node) {
		super(source);
		this.node = node;
	}
	
	public Node getNode() {
		return node;
	}
}