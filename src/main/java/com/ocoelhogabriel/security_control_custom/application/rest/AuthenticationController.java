package com.ocoelhogabriel.security_control_custom.application.rest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.exceptions.JWTVerificationException;

import com.ocoelhogabriel.security_control_custom.application.dto.AuthModel;
import com.ocoelhogabriel.security_control_custom.application.dto.ResponseAuthDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.TokenValidationResponseDTO;
import com.ocoelhogabriel.security_control_custom.domain.service.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/autenticacao")
@Tag(name = "Autenticação", description = "API para Controle de Autenticação e gerenciamento de tokens")
public class AuthenticationController {

	@Autowired
	private IAuthService userServImpl;

	@PostMapping("/v1/auth")
	@Operation(description = "Realizar autenticação de usuário. Recebe credenciais e retorna um token de acesso.")
	public ResponseEntity<ResponseAuthDTO> postAuth(@Valid @RequestBody @NonNull AuthModel auth) throws AuthenticationException, IOException {
		return userServImpl.authLogin(auth);
	}

	@GetMapping("/v1/validate")
	@Operation(description = "Validar token de acesso. Verifica se o token é válido e retorna o status.")
	public ResponseEntity<TokenValidationResponseDTO> validateToken(@RequestParam("token") @NonNull String token) {
		try {
			return userServImpl.validateAndParseToken(token);
		} catch (JWTVerificationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenValidationResponseDTO(false, 0L, "Unauthorized token."));
		}
	}

	@GetMapping("/v1/refresh")
	@Operation(description = "Gerar novo token. Verifica a validade do token e, se expirado, gera um novo token.")
	public ResponseEntity<ResponseAuthDTO> refreshToken(@RequestParam("token") String token) {
		return userServImpl.refreshToken(token);
	}
}