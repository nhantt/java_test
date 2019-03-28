package com.nals.api.dto.request;

import lombok.Data;

@Data
public class SortAndPagingRequest {
	private int pageNum;
	
	private int pageSize;
	
	private String sortColumn;
	
	private boolean descending;
}
