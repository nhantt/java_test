package com.nals.api.util;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class IdGenerator {
	public String newId(){
		UUID id = UUID.randomUUID();
		
		return id.toString();
	}
}
