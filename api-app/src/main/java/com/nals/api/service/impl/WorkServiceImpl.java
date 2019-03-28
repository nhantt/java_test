package com.nals.api.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nals.api.dao.WorkDao;
import com.nals.api.dto.bs.Work;
import com.nals.api.service.IWorkService;

@Service
public class WorkServiceImpl implements IWorkService {
	@Value("${app.api.pagesize}")
    public static int DEFAULT_PAGESIZE = 50;
	
	@Autowired
	private WorkDao dao;

	@Override
	public List<Work> getAllWork() {
		return (List<Work>) dao.findAll();
	}

	@Override
	public Page<Work> getAllWork(int pageNum, int pageSize, String sortColumn, boolean descending) {
		// Prepare paging and sort order
		Sort sort = Sort.by(descending ? Direction.DESC : Direction.ASC, sortColumn);
		Pageable pageable = PageRequest.of(pageNum >= 0 ? pageNum : 0, pageSize > 0 ? pageSize : DEFAULT_PAGESIZE, sort);

		// Get data
		return dao.findAll(pageable);
	}

	@Override
	public Work findById(String id) {
		// Get data
		Optional<Work> result = dao.findById(id);

		// Check return
		if (!result.isPresent()) {
			return null;
		}

		return result.get();
	}

	@Override
	public void create(Work work) {
		dao.save(work);
	}
	
	@Override
	public void create(List<Work> works) {
		dao.saveAll(works);
	}

	@Override
	public void update(Work work) {
		dao.save(work);
	}
	
	@Override
	public void update(List<Work> works) {
		dao.saveAll(works);
	}

	@Override
	public void deleteById(String id) {
		dao.deleteById(id);
	}

	@Override
	public void deleteById(List<String> ids) {
		Iterable<Work> works = dao.findAllById(ids);
		
		dao.deleteAll(works);
	}

	@Override
	public void delete(Work work) {
		dao.delete(work);
	}
	
	@Override
	public void delete(List<Work> works) {
		dao.deleteAll(works);
	}

}
