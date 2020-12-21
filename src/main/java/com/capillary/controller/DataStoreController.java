package com.capillary.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.capillary.domain.Node;
import com.capillary.domain.NodeRequest;
import com.capillary.event.CreateEvent;
import com.capillary.event.DeleteEvent;
import com.capillary.event.UpdateEvent;
import com.capillary.exception.DuplicateNodeException;
import com.capillary.exception.InvalidInputException;
import com.capillary.exception.NodeNotFoundException;
import com.capillary.repository.DataStoreRepository;

@RestController
public class DataStoreController {

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@PostMapping(value = "/data", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public String createNode(@RequestBody NodeRequest node, @RequestParam(defaultValue = "false") boolean publish)
			throws NodeNotFoundException, InvalidInputException, DuplicateNodeException {
		DataStoreRepository store = DataStoreRepository.INSTANCE;
		Node newNode = store.addNode(node.getPath(), node.getData());
		
		// Default publisher event for all root node events
		boolean eventPublished = false;
		if (store.isRootNode(newNode.getPath())) {
			CreateEvent event = new CreateEvent(this, newNode);
			applicationEventPublisher.publishEvent(event);
			eventPublished = true;
		}

		// Publish create event when requested
		if (publish && !eventPublished) {
			CreateEvent event = new CreateEvent(this, newNode);
			applicationEventPublisher.publishEvent(event);
		}

		return node.getPath();
	}

	@PutMapping("/data")
	@ResponseStatus(HttpStatus.OK)
	public NodeRequest updateNode(@RequestBody NodeRequest node, @RequestParam(defaultValue = "false") boolean publish)
			throws NodeNotFoundException, InvalidInputException {
		DataStoreRepository store = DataStoreRepository.INSTANCE;
		Node updatedNode = store.updateNode(node.getPath(), node.getData());

		// Publish create event when requested
		if (publish) {
			UpdateEvent event = new UpdateEvent(this, updatedNode);
			applicationEventPublisher.publishEvent(event);
		}
		return node;
	}

	@DeleteMapping("/data")
	@ResponseStatus(HttpStatus.OK)
	public void deleteNode(@RequestParam String path, @RequestParam(defaultValue = "false") boolean publish)
			throws NodeNotFoundException, InvalidInputException {
		DataStoreRepository store = DataStoreRepository.INSTANCE;
		Node deletedNode = store.deleteNode(path);

		// Publish create event when requested
		if (publish) {
			DeleteEvent event = new DeleteEvent(this, deletedNode);
			applicationEventPublisher.publishEvent(event);
		}
	}

	@GetMapping("/data")
	public String getNodeData(@RequestParam String path) throws NodeNotFoundException, InvalidInputException {
		DataStoreRepository store = DataStoreRepository.INSTANCE;
		Node node = store.getNode(path);
		if (node == null)
			throw new NodeNotFoundException(path);
		return node.getData();
	}

	@GetMapping("/getChilds")
	public List<String> getNodeChildPaths(@RequestParam String path)
			throws NodeNotFoundException, InvalidInputException {
		DataStoreRepository store = DataStoreRepository.INSTANCE;
		Node node = store.getNode(path);
		if (node == null)
			throw new NodeNotFoundException(path);
		List<String> childPaths = new ArrayList<String>();
		if (node.getChildren() != null)
			node.getChildren().keySet().forEach(childPath -> childPaths.add(childPath));
		return childPaths;
	}
}
