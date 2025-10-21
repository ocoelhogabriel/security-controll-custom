package com.ocoelhogabriel.security_control_custom.domain.service;

import java.io.IOException;
import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.ocoelhogabriel.security_control_custom.application.dto.AuthModel;
import com.ocoelhogabriel.security_control_custom.application.dto.ResponseAuthDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.TokenValidationResponseDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.GenerateTokenRecords;

public interface IAuthService extends UserDetailsService {
	GenerateTokenRecords getToken(AuthModel authToken) throws IOException;

	String validToken(String token);

	Instant validateTimeToken(String token);

	ResponseEntity<ResponseAuthDTO> refreshToken(String token);

	ResponseEntity<TokenValidationResponseDTO> validateAndParseToken(@NonNull String token);

	ResponseEntity<ResponseAuthDTO> authLogin(@NonNull AuthModel authReq) throws AuthenticationException, IOException;
}
