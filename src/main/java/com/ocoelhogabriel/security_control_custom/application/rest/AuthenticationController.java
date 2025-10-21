package com.ocoelhogabriel.security_control_custom.application.rest;

import com.ocoelhogabriel.security_control_custom.application.dto.AuthModel;
import com.ocoelhogabriel.security_control_custom.application.dto.ResponseAuthDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.TokenValidationResponseDTO;
import com.ocoelhogabriel.security_control_custom.application.exception.AuthenticationFailedException;
import com.ocoelhogabriel.security_control_custom.application.exception.InvalidTokenException;
import com.ocoelhogabriel.security_control_custom.application.exception.UserNotFoundException;
import com.ocoelhogabriel.security_control_custom.application.port.in.IAuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/autenticacao")
@Tag(name = "Autenticação", description = "API para Controle de Autenticação e gerenciamento de tokens")
public class AuthenticationController {

	@Autowired
	private IAuthenticationService authenticationService;

	@PostMapping("/v1/auth")
	@Operation(description = "Realizar autenticação de usuário. Recebe credenciais e retorna um token de acesso.")
	public ResponseEntity<ResponseAuthDTO> postAuth(@Valid @RequestBody @NonNull AuthModel auth) {
		try {
			ResponseAuthDTO response = authenticationService.authenticateAndGenerateToken(auth);
			return ResponseEntity.ok(response);
		} catch (AuthenticationFailedException | UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		} catch (Exception e) {
			// Log the exception for debugging purposes
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/v1/validate")
	@Operation(description = "Validar token de acesso. Verifica se o token é válido e retorna o status.")
	public ResponseEntity<TokenValidationResponseDTO> validateToken(@RequestParam("token") @NonNull String token) {
		try {
			TokenValidationResponseDTO response = authenticationService.validateAndParseToken(token);
			return ResponseEntity.ok(response);
		} catch (InvalidTokenException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenValidationResponseDTO(false, 0L, e.getMessage()));
		}
	}

	@GetMapping("/v1/refresh")
	@Operation(description = "Gerar novo token. Verifica a validade do token e, se expirado, gera um novo token.")
	public ResponseEntity<ResponseAuthDTO> refreshToken(@RequestParam("token") String token) {
		try {
			ResponseAuthDTO response = authenticationService.refreshToken(token);
			return ResponseEntity.ok(response);
		} catch (InvalidTokenException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
}
