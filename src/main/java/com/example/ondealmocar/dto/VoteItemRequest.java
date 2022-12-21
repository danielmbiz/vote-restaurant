package com.example.ondealmocar.dto;

public class VoteItemRequest {

	private Long id;
	private Long voteId;
	private Long employeeId;
	private Long restaurantId;

	VoteItemRequest() {

	}

	public VoteItemRequest(Long id, Long voteId, Long employeeId, Long restaurantId) {
		super();
		this.id = id;
		this.voteId = voteId;
		this.employeeId = employeeId;
		this.restaurantId = restaurantId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVoteId() {
		return voteId;
	}

	public void setVoteId(Long voteId) {
		this.voteId = voteId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Long getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(Long restaurantId) {
		this.restaurantId = restaurantId;
	}

}
