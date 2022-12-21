package com.example.ondealmocar.dto.projection;

import java.time.LocalDate;

import com.example.ondealmocar.model.Restaurant;

public interface IVoteWinWeek {
	LocalDate getDateVote();
	Restaurant getRestaurant();
}
