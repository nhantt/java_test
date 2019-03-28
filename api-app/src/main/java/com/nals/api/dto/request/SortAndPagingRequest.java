package com.nals.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SortAndPagingRequest {
	@JsonProperty("page_num")
	private int pageNum;
	
	@JsonProperty("page_size")
	private int pageSize;
	
	@JsonProperty("sort_column")
	private String sortColumn;
	
	@JsonProperty("descending")
	private boolean descending;
}
