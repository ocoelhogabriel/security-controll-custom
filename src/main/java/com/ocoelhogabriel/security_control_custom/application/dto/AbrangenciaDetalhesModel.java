package com.ocoelhogabriel.security_control_custom.application.dto;

import com.fasterxml.jackson.databind.JsonNode;

import com.ocoelhogabriel.security_control_custom.domain.model.enums.RecursoMapEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AbrangenciaDetalhesModel {

	@NotBlank(message = "O recurso é obrigatório e não pode estar em branco.")
	@Schema(name = "recurso", description = "Lista dos níveis de permissão.", example = "EMPRESA")
	private RecursoMapEnum recurso;

	@NotNull(message = "A hierarquia é obrigatória e não pode ser nula. Defina o nível de hierarquia como 0 (não) ou 1 (sim).")
	@Schema(name = "hierarquia", description = "Define o nível de hierarquia que a abrangência atinge. (0 = não, 1 = sim)", example = "0")
	private Integer hierarquia;

	@Schema(name = "dados", description = "Detalhes adicionais da abrangência em formato JSON.", example = "[1,2,3,4]")
	private JsonNode dados;

	public RecursoMapEnum getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoMapEnum recurso) {
		this.recurso = recurso;
	}

	public Integer getHierarquia() {
		return hierarquia;
	}

	public void setHierarquia(Integer hierarquia) {
		this.hierarquia = hierarquia;
	}

	public JsonNode getDados() {
		return dados;
	}

	public void setDados(JsonNode dados) {
		this.dados = dados;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AbrangenciaDetalhesModel [");
		if (recurso != null) {
			builder.append("recurso=").append(recurso).append(", ");
		}
		if (hierarquia != null) {
			builder.append("hierarquia=").append(hierarquia).append(", ");
		}
		if (dados != null) {
			builder.append("dados=").append(dados);
		}
		builder.append("]");
		return builder.toString();
	}

	public AbrangenciaDetalhesModel(RecursoMapEnum recurso, Integer hierarquia, JsonNode dados) {
		super();
		this.recurso = recurso;
		this.hierarquia = hierarquia;
		this.dados = dados;
	}

	public AbrangenciaDetalhesModel() {
		super();

	}

}
