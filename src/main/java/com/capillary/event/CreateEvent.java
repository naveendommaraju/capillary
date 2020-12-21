package com.capillary.event;

import org.springframework.context.ApplicationEvent;

import com.capillary.domain.Node;

public class CreateEvent extends ApplicationEvent {

	private Node node;
	
	public CreateEvent(Object source, Node node) {
		super(source);
		this.node = node;
	}
	
	public Node getNode() {
		return node;
	}
}
