package com.ocoelhogabriel.security_control_custom.application.dto;

import java.util.Date;

import com.ocoelhogabriel.security_control_custom.infrastructure.utils.Utils;

public class ResponseAuthenticationDTO {
	private String token;
	private String role;
	private String dateRequest;

	public ResponseAuthenticationDTO() {
	}

	public ResponseAuthenticationDTO(String token, String role, Date date) {
		this.token = token;
		this.role = role;
		this.dateRequest = date == null ? Utils.sdfBaseDateforString() : Utils.sdfDateforString(date);
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDateRequest() {
		return dateRequest;
	}

	public void setDateRequest(String dateRequest) {
		this.dateRequest = dateRequest;
	}

	@Override
	public String toString() {
		return "ResponseAuthenticationDTO [token=" + token + ", role=" + role + ", dateRequest=" + dateRequest + "]";
	}
}
