package com.example.ondealmocar.dto.projection;

import java.time.LocalDate;

import com.example.ondealmocar.model.Restaurant;

public interface IVoteWin {
	LocalDate getDateVote();
	Integer getQuantityVote();
	Restaurant getRestaurant();
}
