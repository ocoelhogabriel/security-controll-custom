package com.ocoelhogabriel.security_control_custom.infrastructure.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

import com.ocoelhogabriel.security_control_custom.domain.entity.ProfileDomain;
import com.ocoelhogabriel.security_control_custom.domain.entity.UserDomain;
import com.ocoelhogabriel.security_control_custom.infrastructure.utils.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import com.ocoelhogabriel.security_control_custom.application.dto.GenerateTokenRecords;

@Component
public class JWTUtil {

	private static final DateTimeFormatter dtfEditado = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

	@Value("${api.security.token.secret}")
	private String secret;

	@Value("${api.security.expiration.time.minutes}")
	private long expirationTime;

	public String getSecret() {
		return secret;
	}

	public GenerateTokenRecords generateToken(UserDomain userDomain) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			Instant expirationInstant = genExpirationDate();
			String token = JWT.create().withIssuer("auth-api").withSubject(userDomain.getLogin()).withExpiresAt(Date.from(expirationInstant)).withClaim("role", userDomain.getProfileDomain().getName()).sign(algorithm);

			return new GenerateTokenRecords(userDomain.getLogin(), token, Utils.newDateString(), dtfEditado.format(expirationInstant.atZone(ZoneId.systemDefault())));
		} catch (JWTCreationException | IllegalArgumentException e) {
			throw new JWTCreationException("Error while generating token", e);
		}
	}

	public String validateToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			JWTVerifier verifier = JWT.require(algorithm).withIssuer("auth-api").build();
			return verifier.verify(token).getSubject();
		} catch (TokenExpiredException e) {
			throw new AccessDeniedException("Token has expired", e);
		} catch (JWTVerificationException e) {
			throw new AccessDeniedException("Token verification failed", e);
		}
	}

	public GenerateTokenRecords validateOrRefreshToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			JWTVerifier verifier = JWT.require(algorithm).withIssuer("auth-api").build();
			DecodedJWT jwt = verifier.verify(token);

			return new GenerateTokenRecords(jwt.getSubject(), token, Utils.newDateString(), dtfEditado.format(jwt.getExpiresAt().toInstant().atZone(ZoneId.systemDefault())));
		} catch (TokenExpiredException exception) {
			return refreshToken(token);
		} catch (JWTVerificationException exception) {
			throw new AccessDeniedException("Token verification failed");
		}
	}

	public GenerateTokenRecords refreshToken(String refreshToken) {
		try {
			Algorithm refreshAlgorithm = Algorithm.HMAC256(secret);
			JWTVerifier verifier = JWT.require(refreshAlgorithm).withIssuer("auth-api").build();
			DecodedJWT decodedToken = verifier.verify(refreshToken);

			String username = decodedToken.getSubject();
			UserDomain userDomain = new UserDomain();
			userDomain.setLogin(username);
			// Note: ProfileDomain is needed for token generation, but not available here.
			// This might require fetching the user from the database or passing more info.
			// For now, setting a dummy ProfileDomain to avoid null pointer.
			userDomain.setProfileDomain(new ProfileDomain(null, "DEFAULT", "Default Profile"));
			return generateToken(userDomain);
		} catch (TokenExpiredException e) {
			throw new AccessDeniedException("Refresh token has expired");
		} catch (JWTVerificationException e) {
			throw new AccessDeniedException("Refresh token verification failed");		}
	}

	private Instant genExpirationDate() {
		return Instant.now().plusSeconds(expirationTime * 60);
	}

	public Instant getExpirationDateFromToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			JWTVerifier verifier = JWT.require(algorithm).withIssuer("auth-api").build();
			DecodedJWT jwt = verifier.verify(token);
			return jwt.getExpiresAt().toInstant();
		} catch (JWTVerificationException exception) {
			throw new JWTVerificationException(exception.getMessage());
		}
	}

	public static String convertDateToString(Date date) {
		Objects.requireNonNull(date, "A Data de entrada para conversão de Date para String está nula.");
		LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
		return dtfEditado.format(localDateTime);
	}

}
