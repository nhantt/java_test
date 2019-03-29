package com.nals.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nals.api.dto.bs.Work;
import com.nals.api.dto.request.SortAndPagingRequest;
import com.nals.api.dto.response.SortAndPagingResponse;
import com.nals.api.exception.EntityNotFoundException;
import com.nals.api.service.IWorkService;

@RestController
@RequestMapping("/works")
public class WorkController {	
	@Autowired
	IWorkService workService;

	@GetMapping("/")
	@ResponseBody
	public List<Work> getAll() {
		Page<Work> result = workService.getAllWork();
		
		return result.get().collect(Collectors.toList());
	}
	
	@GetMapping("/{id}")
	public Work retrieveItem(@PathVariable String id) throws EntityNotFoundException {
		Work work = workService.findById(id);

		if (work == null) {
			throw new EntityNotFoundException("Work does not exist");
		}

		return work;
	}

	@PostMapping("/list")
	@ResponseBody
	public SortAndPagingResponse getAll(@RequestBody SortAndPagingRequest req) {
		SortAndPagingResponse response = new SortAndPagingResponse();

		Page<Work> result = workService.getAllWork(req.getPageNum(), req.getPageSize(), req.getSortColumn(),
				req.isDescending());

		response.setNumberOfElements(result.getTotalElements());
		response.setTotalPages(result.getTotalPages());
		response.setItems(result.get().collect(Collectors.toList()));

		return response;
	}
	
	@PostMapping("/create")
	public String createItem(@RequestBody Work work) {
		return workService.create(work);
	}
	
	@PutMapping("/update")
	public void updateItem(@RequestBody Work work) throws EntityNotFoundException {
		Work updatedWork = workService.findById(work.getId());

		if (updatedWork == null) {
			throw new EntityNotFoundException("Work does not exist");
		}
		
		workService.update(work);
	}
	
	@DeleteMapping("/delete/{id}")
	public void deleteItem(@PathVariable String id) throws EntityNotFoundException {
		Work work = workService.findById(id);

		if (work == null) {
			throw new EntityNotFoundException("Work does not exist");
		}
		
		workService.deleteById(id);
	}
}
