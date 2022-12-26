package com.example.ondealmocar.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.beans.BeanUtils;

import com.example.ondealmocar.dto.VoteDTO;
import com.example.ondealmocar.model.enums.VoteStatus;

@Entity
@Table
public class Vote {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private LocalDate dateVote;
	
	@Column(nullable = false)
	private VoteStatus status;
	
	@ManyToOne
    @JoinColumn(name = "restaurant_id")
	private Restaurant restaurantWin;
	
	public Vote() {
		
	}

	public Vote(Long id, LocalDate dateVote, VoteStatus status, Restaurant restaurantWin) {
		super();
		this.id = id;
		this.dateVote = dateVote;
		this.status = status;
		this.restaurantWin = restaurantWin;
	}
	
	public Vote(VoteDTO dto) {
		super();
		this.id = dto.getId();
		this.dateVote = dto.getDateVote();
		this.status = dto.getStatus();
		this.restaurantWin = dto.getRestaurantWin();
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

	public Restaurant getRestaurantWin() {
		return restaurantWin;
	}

	public void setRestaurantWin(Restaurant restaurantWin) {
		this.restaurantWin = restaurantWin;
	}
	
	public static Vote of(VoteDTO dto) {
		var response = new Vote();
		BeanUtils.copyProperties(dto, response);
		return response;		
	}

}
