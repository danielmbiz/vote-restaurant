package com.example.ondealmocar.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.beans.BeanUtils;

import com.example.ondealmocar.dto.VoteItemRequest;

@Entity
@Table
public class VoteItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JoinColumn(name = "id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "vote_id")
	private Vote vote;
	
	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;

	@ManyToOne
	@JoinColumn(name = "restaurant_id")
	private Restaurant restaurant;

	public VoteItem() {

	}

	public VoteItem(Long id, Vote vote, Employee employee, Restaurant restaurant) {
		super();
		this.id = id;
		this.vote = vote;
		this.employee = employee;
		this.restaurant = restaurant;
	}
	
	public VoteItem(VoteItemRequest request, Vote vote, Employee employee, Restaurant restaurant) {
		super();
		this.id = request.getId();
		this.vote = vote;
		this.employee = employee;
		this.restaurant = restaurant;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Vote getVote() {
		return vote;
	}

	public void setVote(Vote vote) {
		this.vote = vote;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}
	
	public static VoteItem of(VoteItemRequest request) {
		var response = new VoteItem();
		BeanUtils.copyProperties(request, response);
		return response;		
	}

}
