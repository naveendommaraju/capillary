package com.capillary.exception;

public class NodeNotFoundException extends Exception {
	
	public NodeNotFoundException(String path){
		super("Could not find the node with " + path);
	}
}
