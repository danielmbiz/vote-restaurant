package com.example.ondealmocar.dto;

import java.time.LocalDate;

import org.springframework.beans.BeanUtils;

import com.example.ondealmocar.dto.projection.IVoteWinWeek;
import com.example.ondealmocar.model.Restaurant;
import com.example.ondealmocar.model.Vote;

public class VoteWinWeek {
	
	private LocalDate dateVote;
	private Restaurant restaurantWin;
	
	VoteWinWeek() {
		
	}

	public VoteWinWeek(LocalDate dateVote, Restaurant restaurantWin) {
		super();
		this.dateVote = dateVote;
		this.restaurantWin = restaurantWin;
	}
	
	public VoteWinWeek(IVoteWinWeek voteWinWeek) {
		super();
		this.dateVote = voteWinWeek.getDateVote();
		this.restaurantWin = voteWinWeek.getRestaurant();
	}

	public LocalDate getDateVote() {
		return dateVote;
	}

	public void setDateVote(LocalDate dateVote) {
		this.dateVote = dateVote;
	}
	
	public Restaurant getRestaurantWin() {
		return restaurantWin;
	}

	public void setRestaurantWin(Restaurant restaurantWin) {
		this.restaurantWin = restaurantWin;
	}		

	
	public static VoteWinWeek of(Vote vote) {
		var response = new VoteWinWeek();
		BeanUtils.copyProperties(vote, response);
		return response;		
	}
}
