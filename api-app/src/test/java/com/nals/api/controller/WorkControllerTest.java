package com.nals.api.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit4.SpringRunner;

import com.nals.api.dto.bs.Work;
import com.nals.api.dto.request.SortAndPagingRequest;
import com.nals.api.dto.response.SortAndPagingResponse;
import com.nals.api.exception.EntityNotFoundException;
import com.nals.api.service.IWorkService;

@RunWith(SpringRunner.class)
public class WorkControllerTest {
	@InjectMocks
	WorkController controller;

	@Mock
	private IWorkService mockService;

	@Test
	public void getAll_NoData() {
		//mock
		when(mockService.getAllWork()).thenReturn(Page.empty());

		//invoke method
		List<Work> result = controller.getAll();

		//verify invoke service
		verify(mockService, times(1)).getAllWork();
		
		//confirm return value
		assertEquals(0, result.size());
	}

	@Test
	public void getAll_HaveData() {
		Work work = new Work();
		work.setId("12345");

		List<Work> works = new ArrayList<Work>();
		works.add(work);

		//mock
		when(mockService.getAllWork()).thenReturn(new PageImpl<Work>(works));

		//invoke method
		List<Work> result = controller.getAll();

		//verify invoke service
		verify(mockService, times(1)).getAllWork();
		
		//confirm return value
		assertEquals(1, result.size());
		assertEquals(work, result.get(0));
	}

	@Test(expected = EntityNotFoundException.class)
	public void retrieveItem_NoData() throws EntityNotFoundException {
		try {
			//mock
			when(mockService.findById(anyString())).thenReturn(null);

			//invoke method
			controller.retrieveItem("12345");
		} finally {
			//verify invoke service
			verify(mockService, times(1)).findById(anyString());
		}
	}

	@Test
	public void retrieveItem_HaveData() throws EntityNotFoundException {
		Work work = new Work();
		work.setId("12345");

		//mock
		when(mockService.findById(anyString())).thenReturn(work);

		//invoke method
		Work result = controller.retrieveItem("12345");

		//verify invoke service
		verify(mockService, times(1)).findById(anyString());
		
		//confirm return value
		assertEquals(work, result);
	}
	
	@Test
	public void getList_NoData() {
		Page<Work> res = Page.empty();

		SortAndPagingRequest req = new SortAndPagingRequest();
		
		//mock
		when(mockService.getAllWork(anyInt(), anyInt(), nullable(String.class), anyBoolean())).thenReturn(res);

		//invoke method
		SortAndPagingResponse result = controller.getAll(req);

		//verify invoke service
		verify(mockService, times(1)).getAllWork(anyInt(), anyInt(), nullable(String.class), anyBoolean());
		
		//confirm return value
		assertEquals(0, result.getNumberOfElements());
		assertEquals(1, result.getTotalPages());
		assertEquals(0, result.getItems().size());
	}

	@Test
	public void getList_HaveData() {
		Page<Work> res = mock(Page.class);
		SortAndPagingRequest req = new SortAndPagingRequest();
		
		//mock
		when(res.getTotalElements()).thenReturn(1000L);
		when(res.getTotalPages()).thenReturn(100);
		when(res.get()).thenReturn(Stream.of(new Work()));
		when(mockService.getAllWork(anyInt(), anyInt(), nullable(String.class), anyBoolean())).thenReturn(res);

		//invoke method
		SortAndPagingResponse result = controller.getAll(req);

		//verify invoke service
		verify(mockService, times(1)).getAllWork(anyInt(), anyInt(), nullable(String.class), anyBoolean());
		
		//confirm return value
		assertEquals(1000, result.getNumberOfElements());
		assertEquals(100, result.getTotalPages());
		assertEquals(1, result.getItems().size());
	}

	@Test
	public void createItem_Test() {
		String res = "12345";

		//mock
		when(mockService.create(any(Work.class))).thenReturn(res);

		//invoke method
		String result = controller.createItem(new Work());

		//verify invoke service
		verify(mockService, times(1)).create(any(Work.class));
		
		//confirm return value
		assertEquals("12345", result);
	}

	@Test(expected = EntityNotFoundException.class)
	public void updateItem_NoData() throws EntityNotFoundException {
		try {
			//mock
			when(mockService.findById(nullable(String.class))).thenReturn(null);

			//invoke method
			controller.updateItem(new Work());
		} finally {
			//verify invoke service
			verify(mockService, times(1)).findById(nullable(String.class));
			verify(mockService, times(0)).update(any(Work.class));
		}
	}

	@Test
	public void updateItem_HaveData() throws EntityNotFoundException {
		//mock
		when(mockService.findById(nullable(String.class))).thenReturn(new Work());
		doNothing().when(mockService).update(any(Work.class));

		//invoke method
		controller.updateItem(new Work());
		
		//verify invoke service
		verify(mockService, times(1)).findById(nullable(String.class));
		verify(mockService, times(1)).update(any(Work.class));
	}

	@Test(expected = EntityNotFoundException.class)
	public void deleteItem_NoData() throws EntityNotFoundException {
		try {
			//mock
			when(mockService.findById(nullable(String.class))).thenReturn(null);

			//invoke method
			controller.deleteItem("12345");
		} finally {
			//verify invoke service
			verify(mockService, times(1)).findById(nullable(String.class));
			verify(mockService, times(0)).deleteById(nullable(String.class));
		}
	}

	@Test
	public void deleteItem_HaveData() throws EntityNotFoundException {
		//mock
		when(mockService.findById(nullable(String.class))).thenReturn(new Work());
		doNothing().when(mockService).deleteById(nullable(String.class));

		//invoke method
		controller.deleteItem("12345");
		
		//verify invoke service
		verify(mockService, times(1)).findById(nullable(String.class));
		verify(mockService, times(1)).deleteById(nullable(String.class));
	}
}
