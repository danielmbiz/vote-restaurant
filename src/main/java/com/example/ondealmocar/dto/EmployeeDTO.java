package com.example.ondealmocar.dto;

import org.springframework.beans.BeanUtils;

import com.example.ondealmocar.model.Employee;

public class EmployeeDTO {
	
	private Long id;
	private String name;
	private String email;
	
	EmployeeDTO() {
		
	}	
	
	public EmployeeDTO(Long id, String name, String email) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
	}

	public EmployeeDTO(Employee employee) {
		super();
		this.id = employee.getId();
		this.name = employee.getName();
		this.email = employee.getEmail();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public static EmployeeDTO of(Employee employee) {
		var response = new EmployeeDTO();
		BeanUtils.copyProperties(employee, response);
		return response;		
	}

}
