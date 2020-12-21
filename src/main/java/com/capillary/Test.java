package com.capillary;

import java.util.Collection;
import java.util.Map;

import com.capillary.domain.Node;
import com.capillary.exception.DuplicateNodeException;
import com.capillary.exception.InvalidInputException;
import com.capillary.exception.NodeNotFoundException;
import com.capillary.repository.DataStoreRepository;

public class Test {

	public static void main(String[] args) throws NodeNotFoundException, InvalidInputException, DuplicateNodeException {
		DataStoreRepository store = DataStoreRepository.INSTANCE;
		// 1. creating 'root' node with data 'nothing'
		store.addNode("/root", "nothing");
		// 2. creating two childs for root - 'child1' with data 'childdata1' and
		// 'child2' with data 'childdata2'
		store.addNode("/root/child1", "childdata1");
		store.addNode("/root/child2", "childdata2");
		// 3. creating sub child to 'child1' with path 'subchild1' and data
		// 'subchilddata1'
		store.addNode("/root/child1/subchild1", "subchilddata1");
		// 4. Get and print the data all nodes
		printNodeData(store.getAllData());
		// 5. List all child nodes for root
		Node rootNode = store.getRooteNode("root");
		System.out.println("Printing child nodes for path - /" + rootNode.getPath());
		for (Map.Entry<String, Node> e : rootNode.getChildren().entrySet()) {
			System.out.println("/" + rootNode.getPath() + "/" + e.getKey());
		}
		// 6. Delete the node with path '/root/child2'
		store.deleteNode("/root/child2");
		// 7. Printing all nodes
		printNodeData(store.getAllData());
	}

	public static void printNodeData(Collection<Node> nodes) {
		if (nodes != null) {
			for (Node node : nodes) {
				System.out.println("Node path - " + node.getPath() + " and node data - " + node.getPath());
				if(node.getChildren() != null)
					printNodeData(node.getChildren().values());
			}
		}
	}

}
