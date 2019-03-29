package com.nals.api.service.impl;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
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
import com.nals.api.util.IdGenerator;

@Service
public class WorkServiceImpl implements IWorkService {
	@Autowired
	IdGenerator idGenerator;

	@Value("${app.api.pagesize}")
	public static int DEFAULT_PAGESIZE = 50;

	@Autowired
	private WorkDao dao;

	@Override
	public Page<Work> getAllWork() {
		return getAllWork(0, Integer.MAX_VALUE, null);
	}

	@Override
	public Page<Work> getAllWork(int pageNum, int pageSize, String sortColumn) {
		return getAllWork(pageNum, pageSize, sortColumn, false);
	}

	@Override
	public Page<Work> getAllWork(int pageNum, int pageSize, String sortColumn, boolean descending) {
		if (!StringUtils.isEmpty(sortColumn)) {
			// Prepare paging and sort order
			Sort sort = Sort.by(descending ? Direction.DESC : Direction.ASC, sortColumn);
			Pageable pageable = PageRequest.of(pageNum >= 0 ? pageNum : 0, pageSize > 0 ? pageSize : DEFAULT_PAGESIZE,
					sort);

			// Get data
			return dao.findAll(pageable);
		} else {
			// Prepare paging and sort order
			Pageable pageable = PageRequest.of(pageNum >= 0 ? pageNum : 0, pageSize > 0 ? pageSize : DEFAULT_PAGESIZE);

			// Get data
			return dao.findAll(pageable);
		}
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
	public String create(Work work) {
		//Init new id
		String id = idGenerator.newId();
		work.setId(id);
		dao.save(work);
		return id;
	}

	@Override
	public void update(Work work) {
		dao.save(work);
	}

	@Override
	public void deleteById(String id) {
		dao.deleteById(id);
	}
}
