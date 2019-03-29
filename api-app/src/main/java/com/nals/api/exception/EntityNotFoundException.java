package com.nals.api.exception;

@SuppressWarnings("serial")
public class EntityNotFoundException extends Exception{

	public EntityNotFoundException(String message) {
		super(message);
	}
}
