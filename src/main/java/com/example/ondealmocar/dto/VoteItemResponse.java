package com.example.ondealmocar.dto;

import org.springframework.beans.BeanUtils;

import com.example.ondealmocar.model.Employee;
import com.example.ondealmocar.model.Restaurant;
import com.example.ondealmocar.model.Vote;
import com.example.ondealmocar.model.VoteItem;

public class VoteItemResponse {
	
	private Long id;
	private Vote vote;
	private Employee employee;
	private Restaurant restaurant;
	
	VoteItemResponse() {
		
	}

	public VoteItemResponse(Long id, Vote vote, Employee employee, Restaurant restaurant) {
		super();
		this.id = id;
		this.vote = vote;
		this.employee = employee;
		this.restaurant = restaurant;
	}
	
	public VoteItemResponse(VoteItem vote) {
		super();
		this.id = vote.getId();
		this.vote = vote.getVote();
		this.employee = vote.getEmployee();
		this.restaurant = vote.getRestaurant();
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
	
	public static VoteItemResponse of(VoteItem voteItem) {
		var response = new VoteItemResponse();
		BeanUtils.copyProperties(voteItem, response);
		return response;		
	}

}
