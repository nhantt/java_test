package com.nals.api.dto.response;

import java.util.Collection;

import com.nals.api.dto.bs.Work;

import lombok.Data;

@Data
public class SortAndPagingResponse {
	private int totalPages;
	
	private long numberOfElements;
	
	Collection<Work> items;
}
