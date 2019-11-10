package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RefreshRequestDTO {
	@JsonProperty("refresh")
	private String refreshToken;
	
	
	
	public RefreshRequestDTO() {
		super();
	}

	public RefreshRequestDTO(String refreshToken) {
		super();
		this.refreshToken = refreshToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
}
