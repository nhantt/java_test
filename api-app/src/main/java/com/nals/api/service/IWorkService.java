package com.nals.api.service;

import org.springframework.data.domain.Page;

import com.nals.api.dto.bs.Work;

public interface IWorkService {
	Page<Work> getAllWork();
	
	Page<Work> getAllWork(int pageNum, int pageSize, String sortColumn);
	
	Page<Work> getAllWork(int pageNum, int pageSize, String sortColumn, boolean descending);
	
	Work findById(String id);
	
	String create(Work work);
	
	void update(Work work);
	
	void deleteById(String id);
}
