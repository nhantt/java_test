package com.nals.api.dto.request;

import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SortAndPagingRequest {
	@Min(value = 0, message = "Page number must be greater then 0")
	@JsonProperty("page_num")
	private int pageNum;

	@Min(value = 1, message = "Page size must be greater then 1")
	@JsonProperty("page_size")
	private int pageSize;

	@JsonProperty("sort_column")
	private String sortColumn;

	@JsonProperty("descending")
	private boolean descending;
}
