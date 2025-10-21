package com.ocoelhogabriel.security_control_custom.application.dto;

import com.ocoelhogabriel.security_control_custom.domain.entity.PermissionDomain;

public class PermissaoDTO {

	private String recurso;
	private Boolean listar;
	private Boolean buscar;
	private Boolean criar;
	private Boolean editar;
	private Boolean deletar;

	public String getRecurso() {
		return recurso;
	}

	public void setRecurso(String recurso) {
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

	public void setDeletar(Boolean delete) {
		this.deletar = delete;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PermissaoDTO [");
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

	public PermissaoDTO(String recurso, Boolean listar, Boolean buscar, Boolean criar, Boolean editar, Boolean delete) {
		super();
		this.recurso = recurso;
		this.listar = listar;
		this.buscar = buscar;
		this.criar = criar;
		this.editar = editar;
		this.deletar = delete;
	}

	public PermissaoDTO(PermissionDomain permissionDomain) {
		super();
		this.recurso = permissionDomain.getResource().getName();
		this.listar = permissionDomain.getList();
		this.buscar = permissionDomain.getFind();
		this.criar = permissionDomain.getCreate();
		this.editar = permissionDomain.getEdit();
		this.deletar = permissionDomain.getDelete();
	}

	public PermissaoDTO() {
		super();

	}

}
