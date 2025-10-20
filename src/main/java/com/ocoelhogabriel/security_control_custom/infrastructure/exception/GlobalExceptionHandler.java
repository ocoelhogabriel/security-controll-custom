package com.ocoelhogabriel.security_control_custom.infrastructure.exception;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import com.ocoelhogabriel.security_control_custom.infrastructure.utils.message.MessageResponse;
import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ResponseGlobalModel> handleAccessDeniedException(AccessDeniedException ex) {
		log.error("AccessDeniedException: " + ex);
		return MessageResponse.notAuthorize(MessageResponse.responseGlobalModelError(ex.getMessage()));
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ResponseGlobalModel> handleAuthenticationException(AuthenticationException ex) {
		log.error("AuthenticationException: " + ex);
		return MessageResponse.notAuthorize(MessageResponse.responseGlobalModelError(ex.getMessage()));
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ResponseGlobalModel> handleEntityNotFoundException(EntityNotFoundException ex) {
		log.error("EntityNotFoundException: " + ex);
		return MessageResponse.badRequest(MessageResponse.responseGlobalModelError(ex.getMessage()));
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<ResponseGlobalModel> handleIOException(IOException ex) {
		log.error("IOException: " + ex);
		return MessageResponse.badRequest(MessageResponse.responseGlobalModelError(ex.getMessage()));
	}

	@ExceptionHandler(NoSuchAlgorithmException.class)
	public ResponseEntity<ResponseGlobalModel> handleNoSuchAlgorithmException(NoSuchAlgorithmException ex) {
		log.error("NoSuchAlgorithmException: " + ex);
		return MessageResponse.badRequest(MessageResponse.responseGlobalModelError(ex.getMessage()));
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ResponseGlobalModel> handleNullPointerException(NullPointerException ex) {
		log.error("NullPointerException: " + ex);
		return MessageResponse.badRequest(MessageResponse.responseGlobalModelError(ex.getMessage()));
	}

	@ExceptionHandler(TokenExpiredException.class)
	public ResponseEntity<ResponseGlobalModel> handleTokenExpiredException(TokenExpiredException ex) {
		log.error("TokenExpiredException: " + ex);
		return MessageResponse.notAuthorize(MessageResponse.responseGlobalModelError(ex.getMessage()));
	}

	@ExceptionHandler(JWTVerificationException.class)
	public ResponseEntity<ResponseGlobalModel> handleJWTVerificationException(JWTVerificationException ex) {
		log.error("JWTVerificationException: " + ex);
		return MessageResponse.notAuthorize(MessageResponse.responseGlobalModelError(ex.getMessage()));
	}

	@ExceptionHandler(JWTCreationException.class)
	public ResponseEntity<ResponseGlobalModel> handleJWTCreationException(JWTCreationException ex) {
		log.error("JWTCreationException: " + ex);
		return MessageResponse.badRequest(MessageResponse.responseGlobalModelError(ex.getMessage()));
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ResponseGlobalModel> handleRuntimeException(RuntimeException ex) {
		log.error("RuntimeException: " + ex);
		return MessageResponse.badRequest(MessageResponse.responseGlobalModelError(ex.getMessage()));
	}

	@ExceptionHandler(ParseException.class)
	public ResponseEntity<ResponseGlobalModel> handleParseException(ParseException ex) {
		log.error("ParseException: " + ex.getMessage());
		return MessageResponse.badRequest(MessageResponse.responseGlobalModelError(ex.getMessage()));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public void handleIllegalArgumentException(IllegalArgumentException ex) {
		log.error("IllegalArgumentException: " + ex.getMessage());
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public void handleUsernameNotFoundException(UsernameNotFoundException ex) {
		log.error("UsernameNotFoundException: " + ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public void handleException(Exception ex) {
		log.error("Exception: ", ex);
	}

	@ExceptionHandler(SignatureException.class)
	public void handleSignatureException(SignatureException ex) {
		log.error("SignatureException: " + ex.getMessage());
	}

	@ExceptionHandler(AssertionError.class)
	public void handleAssertionError(AssertionError ex) {
		log.error("AssertionError: " + ex.getMessage());
	}

	@ExceptionHandler(JsonMappingException.class)
	public void handleJsonMappingException(JsonMappingException ex) {
		log.error("JsonMappingException: " + ex.getMessage());
	}

	@ExceptionHandler(JsonProcessingException.class)
	public void handleJsonProcessingException(JsonProcessingException ex) {
		log.error("JsonProcessingException: " + ex.getMessage());
	}
}
