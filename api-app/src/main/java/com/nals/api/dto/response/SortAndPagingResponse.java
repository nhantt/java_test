package com.nals.api.dto.response;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nals.api.dto.bs.Work;

import lombok.Data;

@Data
public class SortAndPagingResponse {
	@JsonProperty("tatal_page")
	private int totalPages;
	
	@JsonProperty("count")
	private long numberOfElements;
	
	@JsonProperty("items")
	Collection<Work> items;
}
