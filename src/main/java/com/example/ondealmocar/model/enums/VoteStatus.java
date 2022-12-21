package com.example.ondealmocar.model.enums;

public enum VoteStatus {

	OPEN(0), 
	CLOSE(1);

	private int code;

	private VoteStatus(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static VoteStatus valueOf(int code) {
		for (VoteStatus value : VoteStatus.values()) {
			if (value.getCode() == code) {
				return value;
			}
		}
		throw new IllegalArgumentException("Código invalido");
	}

}
