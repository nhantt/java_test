package com.nals.api.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ExceptionResponse {
	private String message;
	
	private LocalDateTime timestamp;
	
	private List<String> errors;
}
