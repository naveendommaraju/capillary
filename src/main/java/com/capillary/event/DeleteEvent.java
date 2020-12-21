package com.capillary.event;

import org.springframework.context.ApplicationEvent;

import com.capillary.domain.Node;

public class DeleteEvent extends ApplicationEvent {

	private Node node;
	
	public DeleteEvent(Object source, Node node) {
		super(source);
		this.node = node;
	}
	
	public Node getNode() {
		return node;
	}
}
