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

	@Test
    public void create_Test() {
		String newId = "123456789";
		
		Work inp = new Work();
		inp.setWorkName("WorkName");
		inp.setStartingDate(LocalDate.parse("2019-01-01"));
		inp.setEndingDate(LocalDate.now());
		inp.setStatus(Status.COMPLETE);
		
		//mock
		when(mockDao.save(any(Work.class))).thenReturn(null);
		when(mockIdGenerator.newId()).thenReturn(newId);

		//invoke method
		String confirmId = service.create(inp);
		
		//verify invoke dao
		ArgumentCaptor<Work> argument  = ArgumentCaptor.forClass(Work.class);
        verify(mockDao, times(1)).save(argument.capture());
        
        Work verifiedinp = argument.getValue();
        
        //confirm passing param after generating id
        assertEquals(newId, verifiedinp.getId());
        assertEquals(inp.getWorkName(), verifiedinp.getWorkName());
        assertEquals(inp.getStartingDate(), verifiedinp.getStartingDate());
        assertEquals(inp.getEndingDate(), verifiedinp.getEndingDate());
        assertEquals(inp.getStatus(), verifiedinp.getStatus());
        
        //confirm return value
        assertEquals(newId, confirmId);
    }
	
	@Test
    public void update_Test(){
		Work inp = new Work();
		inp.setId("1234567890");
		inp.setWorkName("WorkName");
		inp.setStartingDate(LocalDate.parse("2019-01-01"));
		inp.setEndingDate(LocalDate.now());
		inp.setStatus(Status.COMPLETE);

		//mock
		when(mockDao.save(any(Work.class))).thenReturn(null);

		//invoke method
        service.update(inp);
        
        //verify invoke dao
		ArgumentCaptor<Work> argument  = ArgumentCaptor.forClass(Work.class);
        verify(mockDao, times(1)).save(argument.capture());
        
        //confirm passing param to dao
        Work verifiedinp = argument.getValue();
        assertEquals(inp, verifiedinp);
    }
	
	@Test
    public void delete_Test(){
		String inp = "1234567890";

		//mock
		doNothing().when(mockDao).deleteById(any(String.class));

		//invoke method
        service.deleteById(inp);
        
        //verify invoke dao
        ArgumentCaptor<String> argument  = ArgumentCaptor.forClass(String.class);
        verify(mockDao, times(1)).deleteById(argument.capture());
        
       //confirm passing param to dao
        String verifiedinp = argument.getValue();
        assertEquals(inp, verifiedinp);
    }
	
	@Test
	public void findById_TestPassingParam() {
		Optional<Work> mockOpt = Optional.empty();

		String inp = "1234567890";
		
		//mock
		when(mockDao.findById(any(String.class))).thenReturn(mockOpt);

		//incoke method
		service.findById(inp);

		// verify invoke dao
		ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
		verify(mockDao, times(1)).findById(argument.capture());

		//confirm passing param to dao
		String verifiedinp = argument.getValue();
		assertEquals(inp, verifiedinp);
	}
	
	@Test
    public void findById_TestNoData(){
		Optional<Work> mockOpt = Optional.empty();
		String inp = "1234567890";
		
		//mock
		when(mockDao.findById(any(String.class))).thenReturn(mockOpt);

		//invoke method
        Work result = service.findById(inp);
		
		//confirm return value
        assertNull(result);
    }
	
	@Test
    public void findById_TestHasData(){
		String inpId = "1234567890";
		
		Work item = new Work();
		item.setId(inpId);
		item.setWorkName("WorkName");
		item.setStartingDate(LocalDate.parse("2019-01-01"));
		item.setEndingDate(LocalDate.now());
		item.setStatus(Status.COMPLETE);
		
		Optional<Work> mockOpt = Optional.of(item);
		
		//mock
		when(mockDao.findById(any(String.class))).thenReturn(mockOpt);

		//invoke method
        Work result = service.findById(inpId);
        
		//confirm return value
        assertEquals(item, result);
    }
	
	@Test
    public void getAllWork_NoArgTest(){
		Page<Work> page = Page.empty();
		
		//mock
		doReturn(page).when(service).getAllWork(anyInt(), anyInt(), nullable(String.class));
		
		//invoke method
		Page<Work> result = service.getAllWork();
		
		//verify invoke other method
		ArgumentCaptor<Integer> argument1  = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Integer> argument2  = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<String> argument3  = ArgumentCaptor.forClass(String.class);
		verify(service, times(1)).getAllWork(argument1.capture(),argument2.capture(),argument3.capture());
		
		//confirm passing param to dao
		int arg1= argument1.getValue();
		int arg2= argument2.getValue();
		String arg3= argument3.getValue();
		
		assertEquals(0, arg1);
		assertEquals(Integer.MAX_VALUE, arg2);
		assertEquals(null, arg3);
		
		//confirm return value
		assertEquals(page, result);
    }
	
	@Test
	public void getAllWork_ThreeArgsTest(){
		Page<Work> page = Page.empty();
		
		int inp1 = 0;
		int inp2 = 100;
		String inp3 = null;

		//mock
		doReturn(page).when(service).getAllWork(anyInt(), anyInt(), nullable(String.class), anyBoolean());

		//invoke method
		Page<Work> result = service.getAllWork(inp1, inp2, inp3);
		
		//verify invoke other method
		ArgumentCaptor<Integer> argument1 = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Integer> argument2 = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<String> argument3 = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Boolean> argument4 = ArgumentCaptor.forClass(Boolean.class);
		verify(service, times(1)).getAllWork(argument1.capture(), argument2.capture(), argument3.capture(), argument4.capture());

		//confirm passing param
		int arg1 = argument1.getValue();
		int arg2 = argument2.getValue();
		String arg3 = argument3.getValue();
		boolean arg4 = argument4.getValue();

		assertEquals(inp1, arg1);
		assertEquals(inp2, arg2);
		assertEquals(inp3, arg3);
		assertEquals(false, arg4);
		
		//confirm return value
		assertEquals(page, result);
	}
	
	@Test
	public void getAllWork_FourArgsTestPassingParam_NoSortColumn(){
		int inp1 = 0;
		int inp2 = 100;
		String inp3 = null;
		boolean inp4 = false;

		//mock
		when(mockDao.findAll(any(Pageable.class))).thenReturn(null);
		
		//invoke methos
		service.getAllWork(inp1, inp2, inp3, inp4);
		
		//verify invoke dao
		ArgumentCaptor<Pageable> argument = ArgumentCaptor.forClass(Pageable.class);
		verify(mockDao, times(1)).findAll(argument.capture());

		//confirm passing param
		Pageable arg = argument.getValue();

		assertEquals(inp1, arg.getPageNumber());
		assertEquals(inp2, arg.getPageSize());
		assertEquals(0, arg.getSort().get().count());
	}
	
	@Test
	public void getAllWork_FourArgsTestPassingParam_EmptySortColumn(){
		int inp1 = 0;
		int inp2 = 100;
		String inp3 = "";
		boolean inp4 = false;

		//mock
		when(mockDao.findAll(any(Pageable.class))).thenReturn(null);

		//invoke method
		service.getAllWork(inp1, inp2, inp3, inp4);
		
		//verify invoke dao
		ArgumentCaptor<Pageable> argument = ArgumentCaptor.forClass(Pageable.class);
		verify(mockDao, times(1)).findAll(argument.capture());

		//confirm passing param
		Pageable arg = argument.getValue();

		assertEquals(inp1, arg.getPageNumber());
		assertEquals(inp2, arg.getPageSize());
		assertEquals(0, arg.getSort().get().count());
	}
	
	@Test
	public void getAllWork_FourArgsTestPassingParam_PageNumLessThanZeroAndNoSortColumn(){
		int inp1 = -1;
		int inp2 = 100;
		String inp3 = "";
		boolean inp4 = false;

		//mock
		when(mockDao.findAll(any(Pageable.class))).thenReturn(null);

		//invoke method
		service.getAllWork(inp1, inp2, inp3, inp4);
		
		//verify invoke dao
		ArgumentCaptor<Pageable> argument = ArgumentCaptor.forClass(Pageable.class);
		verify(mockDao, times(1)).findAll(argument.capture());

		//confirm passing param
		Pageable arg = argument.getValue();

		assertEquals(0, arg.getPageNumber());
		assertEquals(inp2, arg.getPageSize());
		assertEquals(0, arg.getSort().get().count());
	}
	
	@Test
	public void getAllWork_FourArgsTestPassingParam_PageSizeLessThanOneAndNoSortColumn(){
		int inp1 = 0;
		int inp2 = 0;
		String inp3 = "";
		boolean inp4 = false;

		//mock
		when(mockDao.findAll(any(Pageable.class))).thenReturn(null);

		//invoke method
		service.getAllWork(inp1, inp2, inp3, inp4);
		
		//verify invoke dao
		ArgumentCaptor<Pageable> argument = ArgumentCaptor.forClass(Pageable.class);
		verify(mockDao, times(1)).findAll(argument.capture());

		//confirm passing param
		Pageable arg = argument.getValue();

		assertEquals(inp1, arg.getPageNumber());
		assertEquals(WorkServiceImpl.DEFAULT_PAGESIZE, arg.getPageSize());
		assertEquals(0, arg.getSort().get().count());
	}
	
	@Test
	public void getAllWork_FourArgsTestPassingParam_SortAscending(){
		int inp1 = 0;
		int inp2 = 100;
		String inp3 = "ColumnName";
		boolean inp4 = false;

		//mock
		when(mockDao.findAll(any(Pageable.class))).thenReturn(null);

		//invoke method
		service.getAllWork(inp1, inp2, inp3, inp4);
		
		//verify invoke dao
		ArgumentCaptor<Pageable> argument = ArgumentCaptor.forClass(Pageable.class);
		verify(mockDao, times(1)).findAll(argument.capture());

		//confirm passing param
		Pageable arg = argument.getValue();
		Sort.Order order = arg.getSort().getOrderFor(inp3);

		assertEquals(inp1, arg.getPageNumber());
		assertEquals(inp2, arg.getPageSize());
		assertEquals(1, arg.getSort().get().count());

		assertNotNull(order);
		assertEquals(Direction.ASC, order.getDirection());
	}
	
	@Test
	public void getAllWork_FourArgsTestPassingParam_SortDescending(){
		int inp1 = 0;
		int inp2 = 100;
		String inp3 = "ColumnName";
		boolean inp4 = true;

		//mock
		when(mockDao.findAll(any(Pageable.class))).thenReturn(null);

		//invoke method
		service.getAllWork(inp1, inp2, inp3, inp4);
		
		//verify invoke dao
		ArgumentCaptor<Pageable> argument = ArgumentCaptor.forClass(Pageable.class);
		verify(mockDao, times(1)).findAll(argument.capture());

		//confirm passing param
		Pageable arg = argument.getValue();
		Sort.Order order = arg.getSort().getOrderFor(inp3);

		assertEquals(inp1, arg.getPageNumber());
		assertEquals(inp2, arg.getPageSize());
		assertEquals(1, arg.getSort().get().count());
		
		assertNotNull(order);
		assertEquals(Direction.DESC, order.getDirection());
	}
	
	@Test
	public void getAllWork_FourArgsTestPassingParam_PageNumLessThanZeroAndHaveSortColumn(){
		int inp1 = -1;
		int inp2 = 100;
		String inp3 = "ColumnName";
		boolean inp4 = true;

		//mock
		when(mockDao.findAll(any(Pageable.class))).thenReturn(null);

		//invoke method
		service.getAllWork(inp1, inp2, inp3, inp4);
		
		//verify invoke dao
		ArgumentCaptor<Pageable> argument = ArgumentCaptor.forClass(Pageable.class);
		verify(mockDao, times(1)).findAll(argument.capture());

		//confirm passing param
		Pageable arg = argument.getValue();
		Sort.Order order = arg.getSort().getOrderFor(inp3);
		
		assertEquals(0, arg.getPageNumber());
		assertEquals(inp2, arg.getPageSize());
		assertEquals(1, arg.getSort().get().count());
		
		assertNotNull(order);
		assertEquals(Direction.DESC, order.getDirection());
	}
	
	@Test
	public void getAllWork_FourArgsTestPassingParam_PageSizeLessThanOneAndHaveSortColumn(){
		int inp1 = 0;
		int inp2 = 1;
		String inp3 = "ColumnName";
		boolean inp4 = true;

		//mock
		when(mockDao.findAll(any(Pageable.class))).thenReturn(null);

		//invoke method
		service.getAllWork(inp1, inp2, inp3, inp4);
		
		//verify invoke dao
				ArgumentCaptor<Pageable> argument = ArgumentCaptor.forClass(Pageable.class);
		verify(mockDao, times(1)).findAll(argument.capture());

		//confirm passing param
		Pageable arg = argument.getValue();
		Sort.Order order = arg.getSort().getOrderFor(inp3);
		
		assertEquals(inp1, arg.getPageNumber());
		assertEquals(WorkServiceImpl.DEFAULT_PAGESIZE, arg.getPageSize());
		assertEquals(1, arg.getSort().get().count());
		
		assertNotNull(order);
		assertEquals(Direction.DESC, order.getDirection());
	}
	
	@Test
	public void getAllWork_FourArgsTest_NoSort(){
		Page<Work> page = Page.empty();
		int inp1 = 0;
		int inp2 = 100;
		String inp3 = "ColumnName";
		boolean inp4 = false;

		//mock
		when(mockDao.findAll(any(Pageable.class))).thenReturn(page);

		//invoke method
		Page<Work> result = service.getAllWork(inp1, inp2, inp3, inp4);

		//confirm return value
		assertEquals(page, result);
	}
	
	@Test
	public void getAllWork_FourArgsTest_HaveSort(){
		Page<Work> page = Page.empty();
		int inp1 = 0;
		int inp2 = 100;
		String inp3 = null;
		boolean inp4 = false;

		//mock
		when(mockDao.findAll(any(Pageable.class))).thenReturn(page);

		//invoke method
		Page<Work> result = service.getAllWork(inp1, inp2, inp3, inp4);

		//confirm return value
		assertEquals(page, result);
	}
}
