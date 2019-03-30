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
	public void getAll_Test1() {
		when(mockService.getAllWork()).thenReturn(Page.empty());

		List<Work> result = controller.getAll();

		verify(mockService, times(1)).getAllWork();
		assertEquals(0, result.size());
	}

	@Test
	public void getAll_Test2() {
		Work work = new Work();
		work.setId("12345");

		List<Work> works = new ArrayList<Work>();
		works.add(work);

		when(mockService.getAllWork()).thenReturn(new PageImpl<Work>(works));

		List<Work> result = controller.getAll();

		verify(mockService, times(1)).getAllWork();
		assertEquals(1, result.size());
		assertEquals(work, result.get(0));
	}

	@Test(expected = EntityNotFoundException.class)
	public void retrieveItem_Test1() throws EntityNotFoundException {
		try {
			when(mockService.findById(anyString())).thenReturn(null);

			controller.retrieveItem("12345");
		} finally {
			verify(mockService, times(1)).findById(anyString());
		}
	}

	@Test
	public void retrieveItem_Test2() throws EntityNotFoundException {
		Work work = new Work();
		work.setId("12345");

		when(mockService.findById(anyString())).thenReturn(work);

		Work result = controller.retrieveItem("12345");

		verify(mockService, times(1)).findById(anyString());
		assertEquals(work, result);
	}

	@Test
	public void getList_Test1() {
		Page<Work> res = mock(Page.class);
		when(res.getTotalElements()).thenReturn(1000L);
		when(res.getTotalPages()).thenReturn(100);
		when(res.get()).thenReturn(Stream.empty());

		SortAndPagingRequest req = new SortAndPagingRequest();
		when(mockService.getAllWork(anyInt(), anyInt(), nullable(String.class), anyBoolean())).thenReturn(res);

		SortAndPagingResponse result = controller.getAll(req);

		verify(mockService, times(1)).getAllWork(anyInt(), anyInt(), nullable(String.class), anyBoolean());
		assertEquals(1000, result.getNumberOfElements());
		assertEquals(100, result.getTotalPages());
		assertEquals(0, result.getItems().size());
	}

	@Test
	public void createItem_Test1() {
		String res = "12345";

		when(mockService.create(any(Work.class))).thenReturn(res);

		String result = controller.createItem(new Work());

		verify(mockService, times(1)).create(any(Work.class));
		assertEquals("12345", result);
	}

	@Test(expected = EntityNotFoundException.class)
	public void updateItem_Test1() throws EntityNotFoundException {
		try {
			when(mockService.findById(nullable(String.class))).thenReturn(null);

			controller.updateItem(new Work());
		} finally {
			verify(mockService, times(1)).findById(nullable(String.class));
			verify(mockService, times(0)).update(any(Work.class));
		}
	}

	@Test
	public void updateItem_Test2() throws EntityNotFoundException {
		when(mockService.findById(nullable(String.class))).thenReturn(new Work());
		doNothing().when(mockService).update(any(Work.class));

		controller.updateItem(new Work());
		verify(mockService, times(1)).findById(nullable(String.class));
		verify(mockService, times(1)).update(any(Work.class));
	}

	@Test(expected = EntityNotFoundException.class)
	public void deleteItem_Test1() throws EntityNotFoundException {
		try {
			when(mockService.findById(nullable(String.class))).thenReturn(null);

			controller.deleteItem("12345");
		} finally {
			verify(mockService, times(1)).findById(nullable(String.class));
			verify(mockService, times(0)).deleteById(nullable(String.class));
		}
	}

	@Test
	public void deleteItem_Test2() throws EntityNotFoundException {
		when(mockService.findById(nullable(String.class))).thenReturn(new Work());
		doNothing().when(mockService).deleteById(nullable(String.class));

		controller.deleteItem("12345");
		verify(mockService, times(1)).findById(nullable(String.class));
		verify(mockService, times(1)).deleteById(nullable(String.class));
	}
}
