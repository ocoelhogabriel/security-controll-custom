package com.ocoelhogabriel.security_control_custom.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Objeto de transferência de dados de Planta")
public class PlantaModel {

	@NotNull(message = "O campo 'empresa' é obrigatório e não pode estar nulo.")
	@Schema(description = "Código da empresa", example = "1", nullable = false)
	private Long empresa;

	@NotBlank(message = "O campo 'nome' é obrigatório e não pode estar em branco.")
	@Schema(description = "Nome da planta", example = "Planta 1", nullable = false)
	private String nome;

	public PlantaModel(Long empresa, String nome) {
		this.empresa = empresa;
		this.nome = nome;
	}

	public PlantaModel() {
		super();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PlantaModel [");
		if (empresa != null)
			builder.append("empresa=").append(empresa).append(", ");
		if (nome != null)
			builder.append("nome=").append(nome);
		builder.append("]");
		return builder.toString();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Long empresa) {
		this.empresa = empresa;
	}

}
