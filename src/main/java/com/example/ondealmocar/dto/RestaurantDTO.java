package com.example.ondealmocar.dto;

import org.springframework.beans.BeanUtils;

import com.example.ondealmocar.model.Restaurant;

public class RestaurantDTO {
	
	private Long id;
	private String name;
	
	RestaurantDTO() {
		
	}

	public RestaurantDTO(Restaurant restaurant) {
		super();
		this.id = restaurant.getId();
		this.name = restaurant.getName();
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
	
	public static RestaurantDTO of(Restaurant restaurant) {
		var response = new RestaurantDTO();
		BeanUtils.copyProperties(restaurant, response);
		return response;		
	}

}
