package com.ocoelhogabriel.security_control_custom.application.dto;

import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Permission;

public class PermissaoDTO {

	private String recurso;
	private Integer listar;
	private Integer buscar;
	private Integer criar;
	private Integer editar;
	private Integer deletar;

	public String getRecurso() {
		return recurso;
	}

	public void setRecurso(String recurso) {
		this.recurso = recurso;
	}

	public Integer getListar() {
		return listar;
	}

	public void setListar(Integer listar) {
		this.listar = listar;
	}

	public Integer getBuscar() {
		return buscar;
	}

	public void setBuscar(Integer buscar) {
		this.buscar = buscar;
	}

	public Integer getCriar() {
		return criar;
	}

	public void setCriar(Integer criar) {
		this.criar = criar;
	}

	public Integer getEditar() {
		return editar;
	}

	public void setEditar(Integer editar) {
		this.editar = editar;
	}

	public Integer getDeletar() {
		return deletar;
	}

	public void setDeletar(Integer delete) {
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

	public PermissaoDTO(String recurso, Integer listar, Integer buscar, Integer criar, Integer editar, Integer delete) {
		super();
		this.recurso = recurso;
		this.listar = listar;
		this.buscar = buscar;
		this.criar = criar;
		this.editar = editar;
		this.deletar = delete;
	}

	public PermissaoDTO(Permission permission) {
		super();
		this.recurso = permission.getResource().getName();
		this.listar = permission.getList();
		this.buscar = permission.getFind();
		this.criar = permission.getCreate();
		this.editar = permission.getEdit();
		this.deletar = permission.getDelete();
	}

	public PermissaoDTO() {
		super();

	}

}
