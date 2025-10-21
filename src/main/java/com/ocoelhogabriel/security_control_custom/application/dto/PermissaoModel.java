package com.ocoelhogabriel.security_control_custom.application.dto;

import com.ocoelhogabriel.security_control_custom.domain.model.enums.RecursoMapEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Modelo de Permissão")
public class PermissaoModel {

	@NotBlank(message = "O campo 'recurso' é obrigatório e não pode estar em branco.")
	@Schema(name = "recurso", description = "Lista dos níveis de permissão. Permissões: BARRAGEM, CANAL, EMPRESA, PENDENCIA, FIRMWARE, LOGGER, MEDICAO, AUDIO, SIRENE, MODULO, USUARIO", example = "BARRAGEM")
	private RecursoMapEnum recurso;

	@NotNull(message = "O campo 'listar' é obrigatório e não pode estar nulo.")
	@Schema(name = "listar", description = "Acessível - 1 / Não Acessível - 0")
	private Boolean listar;

	@NotNull(message = "O campo 'buscar' é obrigatório e não pode estar nulo.")
	@Schema(name = "buscar", description = "Acessível - 1 / Não Acessível - 0")
	private Boolean buscar;

	@NotNull(message = "O campo 'criar' é obrigatório e não pode estar nulo.")
	@Schema(name = "criar", description = "Acessível - 1 / Não Acessível - 0")
	private Boolean criar;

	@NotNull(message = "O campo 'editar' é obrigatório e não pode estar nulo.")
	@Schema(name = "editar", description = "Acessível - 1 / Não Acessível - 0")
	private Boolean editar;

	@NotNull(message = "O campo 'deletar' é obrigatório e não pode estar nulo.")
	@Schema(name = "deletar", description = "Acessível - 1 / Não Acessível - 0")
	private Boolean deletar;

	public RecursoMapEnum getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoMapEnum recurso) {
		this.recurso = recurso;
	}

	public Boolean getListar() {
		return listar;
	}

	public void setListar(Boolean listar) {
		this.listar = listar;
	}

	public Boolean getBuscar() {
		return buscar;
	}

	public void setBuscar(Boolean buscar) {
		this.buscar = buscar;
	}

	public Boolean getCriar() {
		return criar;
	}

	public void setCriar(Boolean criar) {
		this.criar = criar;
	}

	public Boolean getEditar() {
		return editar;
	}

	public void setEditar(Boolean editar) {
		this.editar = editar;
	}

	public Boolean getDeletar() {
		return deletar;
	}

	public void setDeletar(Boolean deletar) {
		this.deletar = deletar;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PermissaoModel [");
		if (recurso != null) {
			builder.append("recurso=").append(recurso).append(", ");
		}
		if (listar != null) {
			builder.append("listar=").append(listar).append(", ");
		}
		if (buscar != null) {
			builder.append("buscar=").append(buscar).append(", ");
		}
		if (criar != null) {
			builder.append("criar=").append(criar).append(", ");
		}
		if (editar != null) {
			builder.append("editar=").append(editar).append(", ");
		}
		if (deletar != null) {
			builder.append("deletar=").append(deletar);
		}
		builder.append("]");
		return builder.toString();
	}

	public PermissaoModel(@NotBlank RecursoMapEnum recurso, @NotBlank Boolean listar, @NotBlank Boolean buscar, @NotBlank Boolean criar, @NotBlank Boolean editar, @NotBlank Boolean deletar) {
		super();
		this.recurso = recurso;
		this.listar = listar;
		this.buscar = buscar;
		this.criar = criar;
		this.editar = editar;
		this.deletar = deletar;
	}

	public PermissaoModel() {
		super();

	}

}
