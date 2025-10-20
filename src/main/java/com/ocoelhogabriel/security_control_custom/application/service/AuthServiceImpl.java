package com.ocoelhogabriel.security_control_custom.application.service;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import com.ocoelhogabriel.security_control_custom.application.dto.AuthModel;
import com.ocoelhogabriel.security_control_custom.application.dto.PerfilDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.ResponseAuthDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.TokenValidationResponseDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.GenerateTokenRecords;
import com.ocoelhogabriel.security_control_custom.application.usecase.IAuthService;
import com.ocoelhogabriel.security_control_custom.infrastructure.security.JWTUtil;
import com.ocoelhogabriel.security_control_custom.infrastructure.utils.message.MessageResponse;

@Service
public class AuthServiceImpl implements IAuthService {

	private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private UsuarioServiceImpl userService;

	@Autowired
	@Lazy
	protected AuthenticationManager authenticationManager;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.findLoginEntity(username);
		if (user == null) {
			log.info("Usuário não encontrado: " + username);
			throw new UsernameNotFoundException("Usuário não encontrado: " + username);
		}
		return user;
	}

	@Override
	public GenerateTokenRecords getToken(AuthModel authToken) throws IOException {
		Objects.requireNonNull(authToken.getLogin(), "Login está nulo.");
		Objects.requireNonNull(authToken.getSenha(), "Senha está nula.");

		User user = userService.findLoginEntity(authToken.getLogin());
		return jwtUtil.generateToken(user);
	}

	@Override
	public String validToken(String token) {
		try {
			return jwtUtil.validateToken(token);
		} catch (TokenExpiredException e) {
			throw new TokenExpiredException("Token expirado.", null);
		} catch (JWTVerificationException | JWTCreationException e) {
			log.error("Erro ao validar o token: {}", e.getMessage(), e);
			throw new AccessDeniedException("Token inválido.");
		}
	}

	@Override
	public Instant validateTimeToken(String token) {
		try {
			return jwtUtil.getExpirationDateFromToken(token);
		} catch (JWTVerificationException e) {
			log.error("Erro ao validar o tempo do token: {}", e.getMessage(), e);
			throw new JWTVerificationException("Erro ao validar o tempo do token.");
		}
	}

	@Override
	public ResponseEntity<ResponseAuthDTO> refreshToken(String token) {
		try {
			var refresh = jwtUtil.validateOrRefreshToken(token);
			User userCheck = userService.findLoginEntity(refresh.username());
			return MessageResponse.success(new ResponseAuthDTO(refresh.token(), refresh.date(), refresh.expiryIn(), userCheck.getId(), new PerfilDTO(userCheck.getPerfil())));
		} catch (JWTVerificationException e) {
			log.error("Erro ao atualizar o token: {}", e.getMessage(), e);
			throw new JWTVerificationException("Erro ao atualizar o token.");
		}
	}

	@Override
	public ResponseEntity<ResponseAuthDTO> authLogin(@NonNull AuthModel authReq) throws AuthenticationException, IOException {
		Objects.requireNonNull(authReq.getLogin(), "Login está nulo.");
		Objects.requireNonNull(authReq.getSenha(), "Senha está nula.");

		User userCheck = userService.findLoginEntity(authReq.getLogin());

		try {
			var userAuthenticationToken = new UsernamePasswordAuthenticationToken(authReq.getLogin(), authReq.getSenha());
			authenticationManager.authenticate(userAuthenticationToken);

			GenerateTokenRecords tokenGenerate = getToken(authReq);
			return MessageResponse.success(new ResponseAuthDTO(tokenGenerate.token(), tokenGenerate.date(), tokenGenerate.expiryIn(), userCheck.getId(), new PerfilDTO(userCheck.getPerfil())));
		} catch (IOException e) {
			log.error("Erro na autenticação: {}", e.getMessage(), e);
			throw new IOException("Erro na autenticação: " + e.getMessage());
		}
	}

	@Override
	public ResponseEntity<TokenValidationResponseDTO> validateAndParseToken(@NonNull String token) {
		Objects.requireNonNull(token, "Token está nulo.");

		String username = validToken(token);
		if (username == null) {
			log.error("Token inválido: {}", token);
			throw new JWTVerificationException("Token inválido.");
		}

		Instant expiration = validateTimeToken(token);
		long timeToExpiry = Duration.between(Instant.now(), expiration).toMillis();
		LocalDateTime expirationLocalDateTime = expiration.atZone(ZoneId.systemDefault()).toLocalDateTime();

		return MessageResponse.success(new TokenValidationResponseDTO(true, timeToExpiry, expirationLocalDateTime.toString()));
	}
}
