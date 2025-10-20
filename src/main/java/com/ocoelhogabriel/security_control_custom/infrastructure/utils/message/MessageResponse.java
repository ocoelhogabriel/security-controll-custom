package com.ocoelhogabriel.security_control_custom.infrastructure.utils.message;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ocoelhogabriel.security_control_custom.infrastructure.exception.ResponseGlobalModel;
import com.ocoelhogabriel.security_control_custom.infrastructure.utils.Utils;

@Component
public class MessageResponse {

	private MessageResponse() {
//		throw new IllegalStateException("MessageResponse class");
	}

	// Método para páginas
	public static <T> ResponseEntity<Page<T>> page(Page<T> page) {
		return new ResponseEntity<>(page, HttpStatus.OK);
	}

	// Método para sucesso
	public static <T> ResponseEntity<T> success(T object) {
		return new ResponseEntity<>(object, HttpStatus.OK);
	}

	// Método para criação
	public static <T> ResponseEntity<T> create(T object) {
		return new ResponseEntity<>(object, HttpStatus.CREATED);
	}
	
	// Método para resposta sem conteudo
	public static ResponseEntity<Void> noContent() {
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// Método para bad request
	public static <T> ResponseEntity<T> badRequest(T object) {
		return new ResponseEntity<>(object, HttpStatus.BAD_REQUEST);
	}

	// Método para não autorizado
	public static <T> ResponseEntity<T> notAuthorize(T object) {
		return new ResponseEntity<>(object, HttpStatus.UNAUTHORIZED);
	}

	// Método para não encontrado
	public static <T> ResponseEntity<T> notFound(T object) {
		return new ResponseEntity<>(object, HttpStatus.NOT_FOUND);
	}

	public static ResponseGlobalModel responseGlobalModelSucess(String message) {
		return new ResponseGlobalModel(true, message, Utils.newDateString());
	}

	public static ResponseGlobalModel responseGlobalModelError(String message) {
		return new ResponseGlobalModel(true, message, Utils.newDateString());
	}

}
