package com.nals.api.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.nals.api.dto.bs.Work;

public interface IWorkService {
	List<Work> getAllWork();
	
	Page<Work> getAllWork(int pageNum, int pageSize, String sortColumn, boolean descending);
	
	Work findById(String id);
	
	void create(Work work);
	
	void create(List<Work> works);
	
	void update(Work work);
	
	void update(List<Work> works);
	
	void deleteById(String id);
	
	void deleteById(List<String> ids);
	
	void delete(Work work);
	
	void delete(List<Work> works);
}
