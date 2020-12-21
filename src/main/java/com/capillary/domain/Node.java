package com.capillary.domain;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Node {
	private String path;
	private String data;
	private Map<String, Node> children;

	public Node(String path, String data) {
		this.path = path;
		this.data = data;
	}

	public Node(String path, String data, Map<String, Node> children) {
		this.path = path;
		this.data = data;
		this.children = children;
	}
	
	public Node addChildren(String path, String data) {
		if(this.children == null) {
			this.children = new HashMap<String, Node>();
		}
		Node childNode = new Node(path, data);
		this.children.put(path, childNode);
		return childNode;
	}
}
