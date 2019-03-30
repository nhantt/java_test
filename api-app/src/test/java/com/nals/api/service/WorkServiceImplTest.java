package com.nals.api.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit4.SpringRunner;

import com.nals.api.dao.WorkDao;
import com.nals.api.dto.bs.Status;
import com.nals.api.dto.bs.Work;
import com.nals.api.service.impl.WorkServiceImpl;
import com.nals.api.util.IdGenerator;

@RunWith(SpringRunner.class)
public class WorkServiceImplTest {
	@Spy
	@InjectMocks
	private WorkServiceImpl service;
	
	@Mock
	private WorkDao mockDao;
	
	@Mock
    private IdGenerator mockIdGenerator;

	/**
	 * Confirm only update Id field
	 * @throws Exception
	 */
	@Test
    public void create_Test01() {
		Work inp = new Work();
		inp.setId("1234567890");
		inp.setWorkName("WorkName");
		inp.setStartingDate(LocalDate.parse("2019-01-01"));
		inp.setEndingDate(LocalDate.now());
		inp.setStatus(Status.COMPLETE);
		
		String newId = "123456789";
		when(mockDao.save(any(Work.class))).thenReturn(null);
		when(mockIdGenerator.newId()).thenReturn(newId);
		
		ArgumentCaptor<Work> argument  = ArgumentCaptor.forClass(Work.class);

        service.create(inp);
        verify(mockDao).save(argument.capture());
        
        Work verifiedinp = argument.getValue();
        
        assertEquals(newId, verifiedinp.getId());
        assertEquals(inp.getWorkName(), verifiedinp.getWorkName());
        assertEquals(inp.getStartingDate(), verifiedinp.getStartingDate());
        assertEquals(inp.getEndingDate(), verifiedinp.getEndingDate());
        assertEquals(inp.getStatus(), verifiedinp.getStatus());
    }
	
	/**
	 * Confirm service output equals with dao output
	 * @throws Exception
	 */
	@Test
    public void create_Test02(){
		String inp = "1234567890";
		
		when(mockDao.save(any(Work.class))).thenReturn(null);
		when(mockIdGenerator.newId()).thenReturn(inp);

		String confirminp = service.create(new Work());
        assertEquals(inp, confirminp);
    }
	
	/**
	 * Confirm passing param to dao equals with input
	 * @throws Exception
	 */
	@Test
    public void update_Test01(){
		Work inp = new Work();
		inp.setId("1234567890");
		inp.setWorkName("WorkName");
		inp.setStartingDate(LocalDate.parse("2019-01-01"));
		inp.setEndingDate(LocalDate.now());
		inp.setStatus(Status.COMPLETE);

		when(mockDao.save(any(Work.class))).thenReturn(null);
		
		ArgumentCaptor<Work> argument  = ArgumentCaptor.forClass(Work.class);

        service.update(inp);
        verify(mockDao).save(argument.capture());
        
        Work verifiedinp = argument.getValue();
        
        assertEquals(inp, verifiedinp);
    }
	
	/**
	 * Confirm passing param to dao equals with input
	 * @throws Exception
	 */
	@Test
    public void delete_Test01(){
		String inp = "1234567890";

		doNothing().when(mockDao).deleteById(any(String.class));
		ArgumentCaptor<String> argument  = ArgumentCaptor.forClass(String.class);

        service.deleteById(inp);
        verify(mockDao).deleteById(argument.capture());
        
        String verifiedinp = argument.getValue();
        
        assertEquals(inp, verifiedinp);
    }
	
	/**
	 * Confirm passing param to dao equals with input
	 * @throws Exception
	 */
	@Test
    public void findById_Test01(){
		Optional<Work> mockOpt = Optional.empty();
		
		String inp = "1234567890";
		when(mockDao.findById(any(String.class))).thenReturn(mockOpt);
		ArgumentCaptor<String> argument  = ArgumentCaptor.forClass(String.class);

        service.findById(inp);
        verify(mockDao).findById(argument.capture());
        
        String verifiedinp = argument.getValue();
        
        assertEquals(inp, verifiedinp);
    }
	
	/**
	 * Confirm return null if no data
	 * @throws Exception
	 */
	@Test
    public void findById_Test02(){
		Optional<Work> mockOpt = Optional.empty();
		String inp = "1234567890";
		
		when(mockDao.findById(any(String.class))).thenReturn(mockOpt);

        Work result = service.findById(inp);
        
        assertNull(result);
    }
	
	/**
	 * Confirm return correct data
	 * @throws Exception
	 */
	@Test
    public void findById_Test03(){
		Work item = new Work();
		item.setId("1234567890");
		item.setWorkName("WorkName");
		item.setStartingDate(LocalDate.parse("2019-01-01"));
		item.setEndingDate(LocalDate.now());
		item.setStatus(Status.COMPLETE);
		
		Optional<Work> mockOpt = Optional.of(item);
		String inp = "1234567890";
		
		when(mockDao.findById(any(String.class))).thenReturn(mockOpt);

        Work result = service.findById(inp);
        
        assertEquals(item, result);
    }
	
	@Test
    public void getAllWork_Test01(){
		doReturn(null).when(service).getAllWork(anyInt(), anyInt(), nullable(String.class));
		
		ArgumentCaptor<Integer> argument1  = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Integer> argument2  = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<String> argument3  = ArgumentCaptor.forClass(String.class);
		
		service.getAllWork();
		verify(service).getAllWork(argument1.capture(),argument2.capture(),argument3.capture());
		
		int arg1= argument1.getValue();
		int arg2= argument2.getValue();
		String arg3= argument3.getValue();
		
		assertEquals(0, arg1);
		assertEquals(Integer.MAX_VALUE, arg2);
		assertEquals(null, arg3);
    }
	
	@Test
	public void getAllWork_Test02(){
		int inp1 = 0;
		int inp2 = 100;
		String inp3 = null;

		doReturn(null).when(service).getAllWork(anyInt(), anyInt(), nullable(String.class), anyBoolean());

		ArgumentCaptor<Integer> argument1 = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Integer> argument2 = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<String> argument3 = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Boolean> argument4 = ArgumentCaptor.forClass(Boolean.class);

		service.getAllWork(inp1, inp2, inp3);
		verify(service).getAllWork(argument1.capture(), argument2.capture(), argument3.capture(), argument4.capture());

		int arg1 = argument1.getValue();
		int arg2 = argument2.getValue();
		String arg3 = argument3.getValue();
		boolean arg4 = argument4.getValue();

		assertEquals(inp1, arg1);
		assertEquals(inp2, arg2);
		assertEquals(inp3, arg3);
		assertEquals(false, arg4);
	}
	
	@Test
	public void getAllWork_Test03(){
		int inp1 = 0;
		int inp2 = 100;
		String inp3 = null;
		boolean inp4 = false;

		when(mockDao.findAll(any(Pageable.class))).thenReturn(null);

		ArgumentCaptor<Pageable> argument = ArgumentCaptor.forClass(Pageable.class);

		service.getAllWork(inp1, inp2, inp3, inp4);
		verify(mockDao).findAll(argument.capture());

		Pageable arg = argument.getValue();

		assertEquals(inp1, arg.getPageNumber());
		assertEquals(inp2, arg.getPageSize());
		assertEquals(0, arg.getSort().get().count());
	}
	
	@Test
	public void getAllWork_Test04(){
		int inp1 = 0;
		int inp2 = 100;
		String inp3 = "";
		boolean inp4 = false;

		when(mockDao.findAll(any(Pageable.class))).thenReturn(null);

		ArgumentCaptor<Pageable> argument = ArgumentCaptor.forClass(Pageable.class);

		service.getAllWork(inp1, inp2, inp3, inp4);
		verify(mockDao).findAll(argument.capture());

		Pageable arg = argument.getValue();

		assertEquals(inp1, arg.getPageNumber());
		assertEquals(inp2, arg.getPageSize());
		assertEquals(0, arg.getSort().get().count());
	}
	
	@Test
	public void getAllWork_Test05(){
		int inp1 = -1;
		int inp2 = 100;
		String inp3 = "";
		boolean inp4 = false;

		when(mockDao.findAll(any(Pageable.class))).thenReturn(null);

		ArgumentCaptor<Pageable> argument = ArgumentCaptor.forClass(Pageable.class);

		service.getAllWork(inp1, inp2, inp3, inp4);
		verify(mockDao).findAll(argument.capture());

		Pageable arg = argument.getValue();

		assertEquals(0, arg.getPageNumber());
		assertEquals(inp2, arg.getPageSize());
		assertEquals(0, arg.getSort().get().count());
	}
	
	@Test
	public void getAllWork_Test06(){
		int inp1 = 0;
		int inp2 = -1;
		String inp3 = "";
		boolean inp4 = false;

		when(mockDao.findAll(any(Pageable.class))).thenReturn(null);

		ArgumentCaptor<Pageable> argument = ArgumentCaptor.forClass(Pageable.class);

		service.getAllWork(inp1, inp2, inp3, inp4);
		verify(mockDao).findAll(argument.capture());

		Pageable arg = argument.getValue();

		assertEquals(inp1, arg.getPageNumber());
		assertEquals(WorkServiceImpl.DEFAULT_PAGESIZE, arg.getPageSize());
		assertEquals(0, arg.getSort().get().count());
	}
	
	@Test
	public void getAllWork_Test07(){
		int inp1 = 0;
		int inp2 = 100;
		String inp3 = "ColumnName";
		boolean inp4 = false;

		when(mockDao.findAll(any(Pageable.class))).thenReturn(null);

		ArgumentCaptor<Pageable> argument = ArgumentCaptor.forClass(Pageable.class);

		service.getAllWork(inp1, inp2, inp3, inp4);
		verify(mockDao).findAll(argument.capture());

		Pageable arg = argument.getValue();

		assertEquals(inp1, arg.getPageNumber());
		assertEquals(inp2, arg.getPageSize());
		assertEquals(1, arg.getSort().get().count());
		
		Sort.Order order = arg.getSort().getOrderFor(inp3);
		assertNotNull(order);
		assertEquals(Direction.ASC, order.getDirection());
	}
	
	@Test
	public void getAllWork_Test08(){
		int inp1 = 0;
		int inp2 = 100;
		String inp3 = "ColumnName";
		boolean inp4 = true;

		when(mockDao.findAll(any(Pageable.class))).thenReturn(null);

		ArgumentCaptor<Pageable> argument = ArgumentCaptor.forClass(Pageable.class);

		service.getAllWork(inp1, inp2, inp3, inp4);
		verify(mockDao).findAll(argument.capture());

		Pageable arg = argument.getValue();

		assertEquals(inp1, arg.getPageNumber());
		assertEquals(inp2, arg.getPageSize());
		assertEquals(1, arg.getSort().get().count());
		
		Sort.Order order = arg.getSort().getOrderFor(inp3);
		assertNotNull(order);
		assertEquals(Direction.DESC, order.getDirection());
	}
	
	@Test
	public void getAllWork_Test09(){
		int inp1 = -1;
		int inp2 = 100;
		String inp3 = "ColumnName";
		boolean inp4 = true;

		when(mockDao.findAll(any(Pageable.class))).thenReturn(null);

		ArgumentCaptor<Pageable> argument = ArgumentCaptor.forClass(Pageable.class);

		service.getAllWork(inp1, inp2, inp3, inp4);
		verify(mockDao).findAll(argument.capture());

		Pageable arg = argument.getValue();

		assertEquals(0, arg.getPageNumber());
		assertEquals(inp2, arg.getPageSize());
		assertEquals(1, arg.getSort().get().count());
		
		Sort.Order order = arg.getSort().getOrderFor(inp3);
		assertNotNull(order);
		assertEquals(Direction.DESC, order.getDirection());
	}
	
	@Test
	public void getAllWork_Test10(){
		int inp1 = 0;
		int inp2 = -1;
		String inp3 = "ColumnName";
		boolean inp4 = true;

		when(mockDao.findAll(any(Pageable.class))).thenReturn(null);

		ArgumentCaptor<Pageable> argument = ArgumentCaptor.forClass(Pageable.class);

		service.getAllWork(inp1, inp2, inp3, inp4);
		verify(mockDao).findAll(argument.capture());

		Pageable arg = argument.getValue();

		assertEquals(inp1, arg.getPageNumber());
		assertEquals(WorkServiceImpl.DEFAULT_PAGESIZE, arg.getPageSize());
		assertEquals(1, arg.getSort().get().count());
		
		Sort.Order order = arg.getSort().getOrderFor(inp3);
		assertNotNull(order);
		assertEquals(Direction.DESC, order.getDirection());
	}
	
	@Test
	public void getAllWork_Test11(){
		Page<Work> page = Page.empty();
		int inp1 = 0;
		int inp2 = 100;
		String inp3 = "ColumnName";
		boolean inp4 = false;

		when(mockDao.findAll(any(Pageable.class))).thenReturn(page);

		Page<Work> result = service.getAllWork(inp1, inp2, inp3, inp4);

		assertEquals(page, result);
	}
	
	@Test
	public void getAllWork_Test12(){
		Page<Work> page = Page.empty();
		int inp1 = 0;
		int inp2 = 100;
		String inp3 = null;
		boolean inp4 = false;

		when(mockDao.findAll(any(Pageable.class))).thenReturn(page);

		Page<Work> result = service.getAllWork(inp1, inp2, inp3, inp4);

		assertEquals(page, result);
	}
	
	@Test
	public void getAllWork_Test13(){
		Page<Work> page = Page.empty();
		int inp1 = 0;
		int inp2 = 100;
		String inp3 = null;

		doReturn(page).when(service).getAllWork(anyInt(), anyInt(), nullable(String.class), anyBoolean());

		Page<Work> result = service.getAllWork(inp1, inp2, inp3);

		assertEquals(page, result);
	}
	
	@Test
	public void getAllWork_Test14(){
		Page<Work> page = Page.empty();

		doReturn(page).when(service).getAllWork(anyInt(), anyInt(), nullable(String.class));

		Page<Work> result = service.getAllWork();

		assertEquals(page, result);
	}
}
