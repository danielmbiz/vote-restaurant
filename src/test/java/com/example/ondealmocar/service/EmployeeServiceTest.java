package com.example.ondealmocar.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import com.example.ondealmocar.dto.EmployeeDTO;
import com.example.ondealmocar.exception.DatabaseException;
import com.example.ondealmocar.exception.ResourceNotFoundException;
import com.example.ondealmocar.model.Employee;
import com.example.ondealmocar.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

	public static final Employee employee = new Employee(null, "nome", "email@email");
	public static final Employee invalidEmployee = new Employee(null, "", "");

	@InjectMocks
	private EmployeeService service;

	@Mock
	private EmployeeRepository repository;

	@Test
	public void findByIdEmployee_WithValidData_ReturnsEmployee() {
		when(repository.findById(anyLong())).thenReturn(Optional.of(employee));
		var dto = EmployeeDTO.of(employee);

		EmployeeDTO sut = service.findById(anyLong());

		assertEquals(EmployeeDTO.class, sut.getClass());
		assertNotNull(sut);
		assertEquals(dto.getId(), sut.getId());
		assertEquals(dto.getName(), sut.getName());
		assertEquals(dto.getEmail(), sut.getEmail());
	}

	@Test
	public void findByIdEmployee_WithInvalidData_ReturnsResourceNotFound() {
		when(repository.findById(anyLong())).thenThrow(new ResourceNotFoundException(""));

		try {
			service.findById(anyLong());
		} catch (ResourceNotFoundException e) {
			assertEquals(ResourceNotFoundException.class, e.getClass());
		}
	}

	@Test
	public void findByAllEmployee_WithValidData_ReturnsListEmployee() {
		List<Employee> list = new ArrayList<>(); 
		list.add(employee);
		EmployeeDTO dto = new EmployeeDTO(employee);
		when(repository.findAll()).thenReturn(list);

		List<EmployeeDTO> sut = service.findAll();

		assertThat(sut).asList().isNotEmpty();
		assertThat(sut).asList().hasSize(1);
		assertEquals(EmployeeDTO.class, sut.get(0).getClass());
		assertEquals(dto.getId(), sut.get(0).getId());
		assertEquals(dto.getName(), sut.get(0).getName());
		assertEquals(dto.getEmail(), sut.get(0).getEmail());
	}

	@Test
	public void createEmployee_WithValidData_ReturnsEmployee() {
		when(repository.save(any())).thenReturn(employee);
		var dto = EmployeeDTO.of(employee);

		EmployeeDTO sut = service.save(dto);

		assertNotNull(sut);
		assertEquals(EmployeeDTO.class, sut.getClass());
		assertEquals(dto.getId(), sut.getId());
		assertEquals(dto.getName(), sut.getName());
		assertEquals(dto.getEmail(), sut.getEmail());
	}

	@Test
	public void createEmployee_DataIntegraty_ViolationException() {
		when(repository.save(any())).thenThrow(RuntimeException.class);
		var dto = EmployeeDTO.of(invalidEmployee);

		assertThatThrownBy(() -> service.save(dto)).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void createEmployee_EmailDataIntegraty_DataIntegrityViolationException() {
		when(repository.save(any())).thenReturn(employee);

		try {
			employee.setId(anyLong());
			var dto = EmployeeDTO.of(employee);
			service.save(dto);
		} catch (DataIntegrityViolationException e) {
			assertEquals(DatabaseException.class, e.getClass());
		}
	}

	@Test
	public void updateEmployee_WithValidData_ReturnsEmployee() {
		when(repository.findById(anyLong())).thenReturn(Optional.of(employee));
		when(repository.save(any())).thenReturn(employee);

		var dto = EmployeeDTO.of(employee);

		EmployeeDTO sut = service.update(anyLong(), dto);
		assertNotNull(sut);
		assertEquals(EmployeeDTO.class, sut.getClass());
		assertEquals(dto.getId(), sut.getId());
		assertEquals(dto.getName(), sut.getName());
		assertEquals(dto.getEmail(), sut.getEmail());
	}

}
