package com.ocoelhogabriel.security_control_custom.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Modelo de Autorização de módulo")
public class AuthDeviceModel {

	@NotBlank(message = "O campo 'nse' é obrigatório e não pode estar em branco.")
	@Schema(name = "nse", description = "Número de Série do módulo", example = "N123124")
	private String nse;

	@NotBlank(message = "O campo 'senha' é obrigatório e não pode estar em branco. Utilize o formato 'nse@yyyy-mm-ddTHH:MM:ss.mmmZ'.")
	@Schema(name = "senha", description = "Senha no formato 'nse@yyyy-mm-ddTHH:MM:ss.mmmZ'", example = "N123124@2024-06-04T10:22:33.375Z")
	private String senha;

	public AuthDeviceModel() {
	}

	public AuthDeviceModel(String nse, String senha) {
		this.nse = nse;
		this.senha = senha;
	}

	public String getNse() {
		return nse;
	}

	public String getSenha() {
		return senha;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AuthDeviceModel [");
		if (nse != null) {
			builder.append("nse=").append(nse).append(", ");
		}
		if (senha != null) {
			builder.append("senha=").append(senha);
		}
		builder.append("]");
		return builder.toString();
	}

}
