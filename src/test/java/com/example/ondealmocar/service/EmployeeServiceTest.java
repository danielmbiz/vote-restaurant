package com.example.ondealmocar.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.ondealmocar.repository.EmployeeRepository;
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
import com.example.ondealmocar.repository.EmployeeRepositoryTest;
import org.springframework.dao.EmptyResultDataAccessException;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    public final static Employee EMPLOYEE = new Employee(null, "nome", "email@email");
    public final static Employee INVALID_EMPLOYEE = new Employee(null, "", "");

    @InjectMocks
    private EmployeeService service;

    @Mock
    private EmployeeRepository repository;

    @Test
    public void findByIdEmployee_WithValidData_ReturnsEmployee() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(EMPLOYEE));
        var dto = EmployeeDTO.of(EMPLOYEE);

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
        list.add(EMPLOYEE);
        EmployeeDTO dto = new EmployeeDTO(EMPLOYEE);
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
        when(repository.save(any())).thenReturn(EMPLOYEE);
        var dto = EmployeeDTO.of(EMPLOYEE);

        EmployeeDTO sut = service.save(dto);

        assertNotNull(sut);
        assertEquals(EmployeeDTO.class, sut.getClass());
        assertEquals(dto.getId(), sut.getId());
        assertEquals(dto.getName(), sut.getName());
        assertEquals(dto.getEmail(), sut.getEmail());
    }

    @Test
    public void createEmployee_DataIntegraty_RuntimeException() {
        when(repository.save(any())).thenThrow(RuntimeException.class);
        var dto = EmployeeDTO.of(INVALID_EMPLOYEE);

        assertThatThrownBy(() -> service.save(dto)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void createEmployee_DataIntegraty_DataIntegrityViolation() {
        when(repository.save(any())).thenThrow(DataIntegrityViolationException.class);
        var dto = EmployeeDTO.of(INVALID_EMPLOYEE);

        assertThatThrownBy(() -> service.save(dto)).isInstanceOf(DatabaseException.class);
    }

    @Test
    public void createEmployee_EmailDataIntegraty_DataIntegrityViolationExceptionReturnDatabaseException() {
        when(repository.save(any())).thenReturn(EMPLOYEE);

        try {
            EMPLOYEE.setId(1L);
            var dto = EmployeeDTO.of(EMPLOYEE);
            service.save(dto);
        } catch (DataIntegrityViolationException e) {
            assertEquals(DatabaseException.class, e.getClass());
        }
    }

    @Test
    public void updateEmployee_WithValidData_ReturnsEmployee() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(EMPLOYEE));
        when(repository.save(any())).thenReturn(EMPLOYEE);

        var dto = EmployeeDTO.of(EMPLOYEE);

        EmployeeDTO sut = service.update(1L, dto);
        assertNotNull(sut);
        assertEquals(EmployeeDTO.class, sut.getClass());
        assertEquals(dto.getId(), sut.getId());
        assertEquals(dto.getName(), sut.getName());
        assertEquals(dto.getEmail(), sut.getEmail());
    }

    @Test
    public void updateEmployee_DataIntegraty_RuntimeException() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(EMPLOYEE));
        when(repository.save(any())).thenThrow(RuntimeException.class);
        var dto = EmployeeDTO.of(EMPLOYEE);

        assertThatThrownBy(() -> service.update(1L, dto)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void deleteEmployee_WithValidData_doesNotThrowsException() {
        assertThatCode(() -> service.delete(anyLong())).doesNotThrowAnyException();
    }

    @Test
    public void deleteEmployee_DataIntegraty_DatabaseException() {
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(anyLong());
        assertThatThrownBy(() -> service.delete(anyLong())).isInstanceOf(DatabaseException.class);
    }

    @Test
    public void deleteEmployee_DataIntegraty_EmptyResultDataAccessException() {
        doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(anyLong());
        assertThatThrownBy(() -> service.delete(1L)).isInstanceOf(ResourceNotFoundException.class);
    }

}
