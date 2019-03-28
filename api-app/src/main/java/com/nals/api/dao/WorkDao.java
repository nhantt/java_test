package com.nals.api.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.nals.api.dto.bs.Work;

public interface WorkDao extends PagingAndSortingRepository<Work, String>{
	
}
