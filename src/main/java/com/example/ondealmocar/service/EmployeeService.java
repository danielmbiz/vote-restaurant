package com.example.ondealmocar.service;

import com.example.ondealmocar.dto.EmployeeDTO;
import com.example.ondealmocar.exception.DatabaseException;
import com.example.ondealmocar.exception.ResourceNotFoundException;
import com.example.ondealmocar.exception.ValidationException;
import com.example.ondealmocar.model.Employee;
import com.example.ondealmocar.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repository;

    public EmployeeDTO findById(Long id) {
        var employee = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                "Profissional não encontrada Id: " + id + " (Err. Employee Service: 01)"));
        return new EmployeeDTO(employee);
    }

    public List<EmployeeDTO> findAll() {
        return repository.findAll().stream().map(EmployeeDTO::of)
                .collect(Collectors.toList());
    }

    public EmployeeDTO save(EmployeeDTO request) {
        try {
            var employee = repository.save(Employee.of(request));
            return EmployeeDTO.of(employee);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("(Err. Employee Service: 05) " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException("Erro não definido");
        }
    }

    public EmployeeDTO update(Long id, EmployeeDTO obj) {
        try {
            findById(id);
            var employee = Employee.of(obj);
            employee.setId(id);
            return EmployeeDTO.of(repository.save(employee));
        } catch (RuntimeException e) {
            throw new ValidationException("(Err. Employee Service: 02) " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException("(Err. Employee Service: 04) " + e.getMessage());
        }
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(
                    "Profissional não encontrada ID: " + id + " (Err. Employee Service: 03)");
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("(Err. Employee Service: 04) " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
