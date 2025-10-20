package com.ocoelhogabriel.security_control_custom.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "AuthenticationDTO")
public class AuthenticationDTO {

	@Schema(description = "Login", example = "user")
	private String login;
	@Schema(description = "Password", example = "password")
	private String password;

	public AuthenticationDTO() {
	}

	public AuthenticationDTO(String login, String password) {
		this.login = login;
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}
}
