package com.ocoelhogabriel.security_control_custom.application.usecase;

import java.io.IOException;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.ocoelhogabriel.security_control_custom.application.dto.AuthModel;

public interface IAutentication extends UserDetailsService {
	public String generateToken(AuthModel authToken) throws IOException;

	public String validateToken(String token);

}
