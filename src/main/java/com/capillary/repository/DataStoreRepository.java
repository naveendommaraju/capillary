package com.capillary.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.capillary.domain.Node;
import com.capillary.exception.DuplicateNodeException;
import com.capillary.exception.InvalidInputException;
import com.capillary.exception.NodeNotFoundException;

public enum DataStoreRepository {

	INSTANCE;

	private static List<Node> nodesData = Collections.synchronizedList(new ArrayList<Node>());

	public List<Node> getAllData(){
		return new ArrayList<Node>(nodesData);
	}
	public Node getNode(String path) throws InvalidInputException {
		if (path == null || path.isEmpty())
			return null;
		String[] paths = getPaths(path);
		Node node = null;
		if (!nodesData.isEmpty()) {
			for (Node n : nodesData) {
				node = getMatchingNode(n, paths[paths.length - 1]);
				if (node != null) {
					break;
				}
			}
		}
		return node;
	}

	public Node getMatchingNode(Node node, String path) {
		if (node == null || path == null)
			return null;
		Node matchingNode = null;
		Queue<Node> queue = new LinkedList<Node>();
		queue.add(node);
		while (!queue.isEmpty()) {
			Node tempNode = queue.poll();
			if (tempNode.getPath().equals(path)) {
				matchingNode = tempNode;
				break;
			}
			if (tempNode.getChildren() != null)
				tempNode.getChildren().values().stream().forEach(s -> queue.add(s));
		}
		return matchingNode;
	}

	public Node addNode(String path, String data)
			throws NodeNotFoundException, InvalidInputException, DuplicateNodeException {
		isValidData(path, data);
		Node newNode = null;
		String[] paths = getPaths(path);
		Node rootNode = getRooteNode(paths[0]);
		if (rootNode != null) {
			if (paths.length > 2) {
				Node leafNode = rootNode.getChildren() != null ? getLeafNode(rootNode.getChildren().get(paths[1]), paths, 1) : null;
				if (leafNode == null)
					throw new NodeNotFoundException(path);
				String currentPath = paths[paths.length - 1];
				newNode = leafNode.addChildren(currentPath, data);
			} else if (paths.length == 2) {
				String currentPath = paths[paths.length - 1];
				if (rootNode.getChildren() != null) {
					Node leafNode = rootNode.getChildren().get(currentPath);
					if (leafNode != null)
						throw new DuplicateNodeException(path);
				}
				newNode = rootNode.addChildren(currentPath, data);
			} else {
				throw new DuplicateNodeException("Node already exists with path - " + path);
			}
		} else {
			if (paths.length > 1) {
				throw new InvalidInputException("Create root node with path & data first");
			}
			newNode = new Node(paths[0], data);
			nodesData.add(newNode);
		}
		return newNode;
	}

	public Node updateNode(String path, String data) throws NodeNotFoundException, InvalidInputException {
		isValidData(path, data);
		Node node = null;
		String[] paths = getPaths(path);
		Node rootNode = getRooteNode(paths[0]);
		if (rootNode != null) {
			if (paths.length > 2) {
				Node leafNode = rootNode.getChildren() != null ? getLeafNode(rootNode.getChildren().get(paths[1]), paths, 1) : null;
				if (leafNode == null)
					throw new NodeNotFoundException(path);
				String currentPath = paths[paths.length - 1];
				node = leafNode.getChildren().get(currentPath);
				if (node == null)
					throw new NodeNotFoundException(path);
				node.setData(data);
			} else if (paths.length == 2) {
				String currentPath = paths[paths.length - 1];
				node = rootNode.getChildren().get(currentPath);
				if (node == null)
					throw new NodeNotFoundException(path);
				node.setData(data);
			} else {
				rootNode.setData(data);
				node = rootNode;
			}
		} else {
			throw new InvalidInputException("Create root node with path & data first");
		}
		return node;
	}

	public Node deleteNode(String path) throws NodeNotFoundException, InvalidInputException {
		String[] paths = getPaths(path);
		Node rootNode = getRooteNode(paths[0]);
		Node deletedNode = null;
		if (rootNode != null) {
			if (paths.length > 2) {
				Node leafNode = rootNode.getChildren() != null ? getLeafNode(rootNode.getChildren().get(paths[1]), paths, 1) : null;
				if (leafNode == null)
					throw new NodeNotFoundException(path);
				String currentPath = paths[paths.length - 1];
				Node node = leafNode.getChildren().get(currentPath);
				if (node == null)
					throw new NodeNotFoundException(path);
				deletedNode = leafNode.getChildren().remove(currentPath);
			} else if (paths.length == 2) {
				String currentPath = paths[paths.length - 1];
				Node node = rootNode.getChildren().get(currentPath);
				if (node == null)
					throw new NodeNotFoundException(path);
				deletedNode = rootNode.getChildren().remove(currentPath);
			} else {
				deletedNode = rootNode;
				nodesData.remove(rootNode);
			}
		} else {
			throw new NodeNotFoundException(path);
		}
		return deletedNode;
	}

	public boolean isRootNode(String path) throws InvalidInputException {
		String[] paths = getPaths(path);
		if (paths != null && paths.length > 0) {
			if (paths.length == 1) {
				for (Node node : nodesData) {
					if (node.getPath().equals(paths[0])) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public Node getRooteNode(String path) {
		if (path == null || path.isEmpty())
			return null;
		for (Node node : nodesData) {
			if (node.getPath().equals(path)) {
				return node;
			}
		}
		return null;
	}

	public Node getLeafNode(Node node, String[] paths, int currentPosition) {
		if (currentPosition == paths.length - 2 && node != null) {
			return node;
		}
		String currentPath = paths[currentPosition];
		Node currentNode = node.getChildren().get(currentPath);
		if (currentNode != null) {
			getLeafNode(currentNode, paths, currentPosition + 1);
		}
		return null;
	}

	public String[] getPaths(String path) throws InvalidInputException {
		String[] paths = null;
		if (path != null && !path.isEmpty()) {
			if (path.charAt(0) == '/') {
				paths = path.substring(1).split("/");
			} else {
				paths = path.split("/");
			}
		} else {
			throw new InvalidInputException("Invalid Path - " + path);
		}
		if (paths != null && paths.length > 0) {
			for (String str : paths) {
				if (str.isEmpty()) {
					throw new InvalidInputException("Invalid Path - " + path);
				}
			}
		}
		return paths;
	}

	public void isValidData(String path, String data) throws InvalidInputException {
		if (path == null || path.isEmpty() || data == null || data.isEmpty())
			throw new InvalidInputException("Path & Data is required to create/update node");
	}
}
