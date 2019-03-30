package com.nals.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nals.api.dao.WorkDao;
import com.nals.api.dto.bs.Status;
import com.nals.api.dto.bs.Work;
import com.nals.api.dto.request.SortAndPagingRequest;
import com.nals.api.dto.response.SortAndPagingResponse;

@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = ApiApplication.class)
@AutoConfigureMockMvc

@Transactional
public class ApiApplicationIntegrationTest {
	@Autowired
	private MockMvc mvc;

	@Autowired
	ObjectMapper mapper;

	@Autowired
	WorkDao dao;

	@Test
	public void getAll_NoData() throws Exception {
		//prepare data 
		dao.deleteAll();
		
		//call api
		MvcResult result = mvc.perform(get("/works/"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		//confirm response
		List<Work> items = mapper.readValue(result.getResponse().getContentAsString(), List.class);
		assertEquals(0, items.size());
	}

	@Test
	public void getAll_HasData() throws Exception {
		long countBefore = dao.count();
		
		//call api
		MvcResult result = mvc.perform(get("/works/"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		//confirm response
		List<Work> items = mapper.readValue(result.getResponse().getContentAsString(), List.class);
		assertEquals(countBefore, items.size());
	}

	@Test
	public void retrieveItem_NoData() throws Exception {
		//prepare data
		dao.deleteAll();

		//call api
		String id = "12345";
		MvcResult result = mvc.perform(get("/works/" + id))
				.andExpect(status().isNotFound())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andReturn();
	}

	@Test
	public void retrieveItem_HasData() throws Exception {
		String id = "12345";

		//prepare data
		Work newWork = new Work();
		newWork.setId(id);
		newWork.setWorkName("Dummy work");
		newWork.setStartingDate(LocalDate.now());
		newWork.setEndingDate(LocalDate.now().plusDays(-10));
		newWork.setStatus(Status.COMPLETE);

		dao.save(newWork);

		//call api
		MvcResult result = mvc.perform(get("/works/" + id))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		//confirm response
		Work item = mapper.readValue(result.getResponse().getContentAsString(), Work.class);

		assertNotNull(item);
		assertEquals(newWork, item);
	}
	
	@Test
	public void  getList_Normal() throws Exception{
		//call api
		SortAndPagingRequest req = new SortAndPagingRequest();
		req.setDescending(false);
		req.setPageNum(0);
		req.setPageSize(4);
		req.setSortColumn("workName");

		MvcResult result = mvc.perform(post("/works/list")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		//confirm response
		SortAndPagingResponse item = mapper.readValue(result.getResponse().getContentAsString(), SortAndPagingResponse.class);
		assertEquals(3, item.getTotalPages());
		assertEquals(4, item.getItems().size());
		assertEquals(9, item.getNumberOfElements());
	}
	
	@Test
	public void  getList_LastPage() throws Exception{
		//call api
		SortAndPagingRequest req = new SortAndPagingRequest();
		req.setDescending(false);
		req.setPageNum(2);
		req.setPageSize(4);
		req.setSortColumn("workName");

		MvcResult result = mvc.perform(post("/works/list")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		//confirm response
		SortAndPagingResponse item = mapper.readValue(result.getResponse().getContentAsString(), SortAndPagingResponse.class);
		assertEquals(3, item.getTotalPages());
		assertEquals(1, item.getItems().size());
		assertEquals(9, item.getNumberOfElements());
	}
	
	@Test
	public void  getList_OutOfRange() throws Exception{
		//call api
		SortAndPagingRequest req = new SortAndPagingRequest();
		req.setDescending(false);
		req.setPageNum(3);
		req.setPageSize(4);
		req.setSortColumn("workName");

		MvcResult result = mvc.perform(post("/works/list")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		//confirm response
		SortAndPagingResponse item = mapper.readValue(result.getResponse().getContentAsString(), SortAndPagingResponse.class);
		assertEquals(3, item.getTotalPages());
		assertEquals(0, item.getItems().size());
		assertEquals(9, item.getNumberOfElements());
	}
	
	@Test
	public void  getList_InvalidPageNumber() throws Exception{
		//call api
		SortAndPagingRequest req = new SortAndPagingRequest();
		req.setDescending(false);
		req.setPageNum(-1);
		req.setPageSize(4);
		req.setSortColumn("workName");

		MvcResult result = mvc.perform(post("/works/list")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(req)))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();
	}
	
	@Test
	public void  getList_InvalidPageSize() throws Exception{
		//call api
		SortAndPagingRequest req = new SortAndPagingRequest();
		req.setDescending(false);
		req.setPageNum(0);
		req.setPageSize(0);
		req.setSortColumn("workName");

		MvcResult result = mvc.perform(post("/works/list")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(req)))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();
	}
	
	@Test
	public void createItem_Success() throws Exception{
		long countBefore = dao.count(); 
		
		//call api
		Work newWork = new Work();
		newWork.setWorkName("Dummy work");
		newWork.setStartingDate(LocalDate.now());
		newWork.setEndingDate(LocalDate.now().plusDays(-10));
		newWork.setStatus(Status.COMPLETE);
		
		MvcResult result = mvc.perform(post("/works/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(newWork)))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
				.andReturn();
		
		//confirm response
		long countAfter = dao.count();
		String item = result.getResponse().getContentAsString();
		
		Work verWork = dao.findById(item).get();
		assertNotNull(verWork);
		assertEquals(newWork.getWorkName(), verWork.getWorkName());
		assertEquals(newWork.getStartingDate(), verWork.getStartingDate());
		assertEquals(newWork.getEndingDate(), verWork.getEndingDate());
		assertEquals(newWork.getStatus(), verWork.getStatus());	
		assertEquals(countBefore + 1, countAfter);
	}
	
	@Test
	public void updateItem_NoData() throws Exception{
		//prepare data
		String id = "DUMMY_ID_12345";
		if(dao.existsById(id)) {
			dao.deleteById(id);
		}
	
		//call api
		Work newWork = new Work();
		newWork.setId(id);
		
		MvcResult result = mvc.perform(put("/works/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(newWork)))
				.andExpect(status().isNotFound())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();
	}
	
	@Test
	public void updateItem_HasData() throws Exception{
		//prepare data
		String id = "DUMMY_ID_12345";
		
		Work newWork = new Work();
		newWork.setId(id);
		dao.save(newWork);
		
		//call api
		newWork.setWorkName("Dummy work");
		newWork.setStartingDate(LocalDate.now());
		newWork.setEndingDate(LocalDate.now().plusDays(-10));
		newWork.setStatus(Status.COMPLETE);
		
		MvcResult result = mvc.perform(put("/works/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(newWork)))
				.andExpect(status().isOk())
				.andReturn();
		
		//confirm response
		assertEquals(0, result.getResponse().getContentLength());
		
		Work verWork = dao.findById(id).get();
		assertEquals(newWork, verWork);
	}
	
	@Test
	public void deleteItem_NoData() throws Exception{
		//prepare data
		String id = "DUMMY_ID_12345";
		if(dao.existsById(id)) {
			dao.deleteById(id);
		}
		
		//call api
		MvcResult result = mvc.perform(delete("/works/delete/" + id))
				.andExpect(status().isNotFound())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();
	}
	
	@Test
	public void deleteItem_HasData() throws Exception{
		//prepare data
		String id = "DUMMY_ID_12345";
		
		Work newWork = new Work();
		newWork.setId(id);
		dao.save(newWork);
		
		//call api
		MvcResult result = mvc.perform(delete("/works/delete/" + id))
				.andExpect(status().isOk())
				.andReturn();
		
		assertEquals(0, result.getResponse().getContentLength());
		assertFalse(dao.existsById(id));
	}
}
