package com.ocoelhogabriel.security_control_custom.application.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class AbrangenciaModel {

	@NotBlank(message = "O nome é obrigatório e não pode estar em branco. Informe um nome para a propriedade.")
	@Schema(name = "nome", description = "Nome da propriedade.", example = "nome", format = "string")
	private String nome;

	@Schema(name = "descricao", description = "Descrição opcional para detalhes adicionais.", example = "Descrição", format = "string")
	private String descricao;

	@Schema(name = "recursos", description = "Lista de abrangência por recurso.")
	private List<AbrangenciaDetalhesModel> recursos;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<AbrangenciaDetalhesModel> getRecursos() {
		return recursos;
	}

	public void setRecursos(List<AbrangenciaDetalhesModel> recursos) {
		this.recursos = recursos;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AbrangenciaModel [");
		if (nome != null) {
			builder.append("nome=").append(nome).append(", ");
		}
		if (descricao != null) {
			builder.append("descricao=").append(descricao).append(", ");
		}
		if (recursos != null) {
			builder.append("recursos=").append(recursos);
		}
		builder.append("]");
		return builder.toString();
	}

	public AbrangenciaModel(@NotBlank String nome, String descricao, @NotBlank List<AbrangenciaDetalhesModel> recursos) {
		super();
		this.nome = nome;
		this.descricao = descricao;
		this.recursos = recursos;
	}

	public AbrangenciaModel() {
	}
}