package com.capillary.event;

import java.util.Map;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.capillary.domain.Node;

@Component
public class DataStoreEventListener {
	
	@EventListener
	public void handleCreateEvent(CreateEvent event) {
		printEventStarted();
		System.out.println("Created node");
		System.out.println("node path - " + event.getNode().getPath());
		printEventEnded();
	}
	
	@EventListener
	public void handleUpdateEvent(UpdateEvent event) {
		printEventStarted();
		System.out.println("Updated node");
		System.out.println("node path - " + event.getNode().getPath());
		printChildren(event.getNode());
		printEventEnded();
	}
	
	@EventListener
	public void handleDeleteEvent(DeleteEvent event) {
		printEventStarted();
		System.out.println("Deleted node");
		System.out.println("node path - " + event.getNode().getPath());
		printChildren(event.getNode());
		printEventEnded();
	}
	
	public void printEventStarted() {
		System.out.println("***** EVENT STARTED *****");
	};
	
	public void printEventEnded() {
		System.out.println("***** EVENT ENDED *****");
	}
	
	public void printChildren(Node node) {
		if(node.getChildren() != null) {
			System.out.println("Child Paths");
			for(Map.Entry<String, Node>  e : node.getChildren().entrySet()) {
				System.out.println("/"+node.getPath()+"/"+e.getKey());
			}
		}
	}
}
