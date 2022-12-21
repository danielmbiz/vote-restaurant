package com.example.ondealmocar.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.BeanUtils;

import com.example.ondealmocar.dto.RestaurantDTO;

@Entity
@Table
public class Restaurant {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	@NotEmpty
	private String name;
	
	@OneToMany(mappedBy = "restaurant")
	private List<VoteItem> voteItems;
	
	@OneToMany(mappedBy = "restaurantWin")
	private List<Vote> votes;
	
	public Restaurant() {
		
	}

	public Restaurant(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Restaurant(RestaurantDTO response) {
		super();
		this.id = response.getId();
		this.name = response.getName();
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
	
	public static Restaurant of(RestaurantDTO request) {
		var restaurant = new Restaurant();
		BeanUtils.copyProperties(request, restaurant);
		return restaurant;
	}

}
