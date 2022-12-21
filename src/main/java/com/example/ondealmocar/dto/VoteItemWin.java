package com.example.ondealmocar.dto;

import java.time.LocalDate;

import org.springframework.beans.BeanUtils;

import com.example.ondealmocar.model.Restaurant;
import com.example.ondealmocar.model.VoteItem;

public class VoteItemWin {

	private LocalDate dateVote;
	private String dayWeek;
	private Integer quantityVote;
	private Restaurant restaurant;

	public VoteItemWin() {

	}

	public VoteItemWin(LocalDate dateVote, Integer quantityVote, Restaurant restaurant) {
		super();
		this.dateVote = dateVote;
		this.dayWeek = setDayWeek(dateVote);
		this.quantityVote = quantityVote;
		this.restaurant = restaurant;
	}

	private String setDayWeek(LocalDate dateVote) {
		return dateVote.getDayOfWeek().name();
	}

	public LocalDate getDateVote() {
		return dateVote;
	}

	public void setDateVote(LocalDate dateVote) {
		this.dateVote = dateVote;
	}

	public String getDayWeek() {
		return dayWeek;
	}

	public void setDayWeek(String dayWeek) {
		this.dayWeek = dayWeek;
	}

	public Integer getQuantityVote() {
		return quantityVote;
	}

	public void setQuantityVote(Integer quantityVote) {
		this.quantityVote = quantityVote;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public static VoteItemWin of(VoteItem voteItem) {
		var response = new VoteItemWin();
		BeanUtils.copyProperties(voteItem, response);
		return response;
	}

}
