package com.example.ondealmocar.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.BeanUtils;

import com.example.ondealmocar.dto.EmployeeDTO;

@Entity
@Table
public class Employee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	@NotEmpty
	private String name;
	
	@Column(nullable = false, unique = true)
	@NotEmpty
	private String email;
	
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
	private List<VoteItem> voteItems;
		
	public Employee() {
		
	}	
	
	public Employee(EmployeeDTO response) {
		super();
		this.id = response.getId();
		this.name = response.getName();
		this.email = response.getEmail();
	}	
	
	public Employee(Long id, @NotEmpty String name, String email, VoteItem voteItem) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
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

	public static Employee of(EmployeeDTO request) {
		var employee = new Employee();
		BeanUtils.copyProperties(request, employee);
		return employee;
	}

}
