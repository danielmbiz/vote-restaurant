package com.example.ondealmocar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ondealmocar.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
