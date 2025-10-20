package com.ocoelhogabriel.security_control_custom.application.dto;

/**
 * ResponseRequestDTO
 */
public class ResponseRequestDTO {

	private String user;
	private String message;
	private String status;

	public ResponseRequestDTO() {
	}

	public ResponseRequestDTO(String user, String message, String status) {
		this.user = user;
		this.message = message;
		this.status = status;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ResponseRequestDTO [user=" + user + ", message=" + message + ", status=" + status + "]";
	}

}