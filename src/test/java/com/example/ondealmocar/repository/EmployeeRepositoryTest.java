package com.example.ondealmocar.repository;

import com.example.ondealmocar.model.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository repository;

    @Autowired
    TestEntityManager entity;

    public final static Employee EMPLOYEE = new Employee(null, "nome", "email@email");
    public final static Employee INVALID_EMPLOYEE = new Employee(null, "", "");
    public final static Employee EMPTY_EMPLOYEE = new Employee();

    @AfterEach
    public void afterEach() {
        EMPLOYEE.setId(null);
    }

    @Test
    public void createEmployee_WithValidData_ReturnsEmployee() {
        Employee employee = repository.save(EMPLOYEE);
        Employee sut = entity.find(Employee.class, employee.getId());

        assertThat(sut).isNotNull();
        assertThat(sut.getName()).isEqualTo(EMPLOYEE.getName());
        assertThat(sut.getName()).isEqualTo(EMPLOYEE.getName());
        assertThat(sut.getEmail()).isEqualTo(EMPLOYEE.getEmail());
    }

    @Test
    public void createEmployee_WithInvalidData_ThrowsException() {
        assertThatThrownBy(() -> repository.save(INVALID_EMPLOYEE)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> repository.save(EMPTY_EMPLOYEE)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void createEmployee_WithExistingName_ThrowsException() {
        Employee employee = entity.persistFlushFind(EMPLOYEE);
        entity.detach(employee);
        employee.setId(null);

        assertThatThrownBy(() -> repository.save(employee)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void findByIdEmployee_ByExistingId_ReturnsEmployee() {
        Employee employee = entity.persistFlushFind(EMPLOYEE);
        Optional<Employee> sut = repository.findById(employee.getId());

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(employee);
    }

    @Test
    public void findByIdEmployee_ByUnexistingId_ReturnsEmpty() {
        Optional<Employee> sut = repository.findById(999L);

        assertThat(sut).isEmpty();
    }

}
