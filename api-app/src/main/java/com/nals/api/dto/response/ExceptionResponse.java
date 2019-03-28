package com.nals.api.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ExceptionResponse {
	private String message;
	
	private LocalDateTime timestamp;
}
