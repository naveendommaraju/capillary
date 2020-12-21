package com.capillary.advicer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.capillary.exception.DuplicateNodeException;
import com.capillary.exception.InvalidInputException;
import com.capillary.exception.NodeNotFoundException;

@ControllerAdvice
public class ExceptionsAdvicer {

	@ResponseBody
	@ExceptionHandler(NodeNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String ndoeNotFoundHandler(NodeNotFoundException ex) {
		return ex.getMessage();
	}
	
	@ResponseBody
	@ExceptionHandler(InvalidInputException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String invalidInputHandler(InvalidInputException ex) {
		return ex.getMessage();
	}
	
	@ResponseBody
	@ExceptionHandler(DuplicateNodeException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	String duplicateNodeHandler(DuplicateNodeException ex) {
		return ex.getMessage();
	}
}
