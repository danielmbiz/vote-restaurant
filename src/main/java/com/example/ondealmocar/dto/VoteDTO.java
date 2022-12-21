package com.example.ondealmocar.dto;

import java.time.LocalDate;

import org.springframework.beans.BeanUtils;

import com.example.ondealmocar.model.Restaurant;
import com.example.ondealmocar.model.Vote;
import com.example.ondealmocar.model.enums.VoteStatus;

public class VoteDTO {
	
	private Long id;
	private LocalDate dateVote;
	private VoteStatus status;
	private Restaurant restaurantWin;
	
	VoteDTO() {
		
	}

	public VoteDTO(Long id, LocalDate dateVote, VoteStatus status, Restaurant restaurantWin) {
		super();
		this.id = id;
		this.dateVote = dateVote;
		this.status = status;
		this.restaurantWin = restaurantWin;
	}
	
	public VoteDTO(Vote vote) {
		super();
		this.id = vote.getId();
		this.dateVote = vote.getDateVote();
		this.status = vote.getStatus();
		this.restaurantWin = vote.getRestaurantWin();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDateVote() {
		return dateVote;
	}

	public void setDateVote(LocalDate dateVote) {
		this.dateVote = dateVote;
	}	
	
	public VoteStatus getStatus() {
		return status;
	}

	public void setStatus(VoteStatus status) {
		this.status = status;
	}

	public static VoteDTO of(Vote vote) {
		var response = new VoteDTO();
		BeanUtils.copyProperties(vote, response);
		return response;		
	}

	public Restaurant getRestaurantWin() {
		return restaurantWin;
	}

	public void setRestaurantWin(Restaurant restaurantWin) {
		this.restaurantWin = restaurantWin;
	}		

}
