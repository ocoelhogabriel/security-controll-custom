package com.ocoelhogabriel.security_control_custom.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Modelo de Autorização")
public class AuthModel {

	@NotBlank(message = "O campo 'login' é obrigatório e não pode estar em branco.")
	@Schema(name = "login", description = "Login", example = "admin")
	private String login;

	@NotBlank(message = "O campo 'senha' é obrigatório e não pode estar em branco.")
	@Schema(name = "senha", description = "Senha", example = "admin")
	private String senha;

	public AuthModel() {
	}

	public AuthModel(String login, String senha) {
		this.login = login;
		this.senha = senha;
	}

	public String getLogin() {
		return login;
	}

	public String getSenha() {
		return senha;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AuthModel [");
		if (login != null) {
			builder.append("login=").append(login).append(", ");
		}
		if (senha != null) {
			builder.append("senha=").append(senha);
		}
		builder.append("]");
		return builder.toString();
	}

}
