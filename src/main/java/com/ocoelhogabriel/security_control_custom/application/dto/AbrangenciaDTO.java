package com.ocoelhogabriel.security_control_custom.application.dto;

import com.ocoelhogabriel.security_control_custom.domain.entity.ScopeDomain;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Scope;

public class AbrangenciaDTO extends CodigoExtends {

	private String nome;

	private String descricao;

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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AbrangenciaDTO [");
		if (nome != null) {
			builder.append("nome=").append(nome).append(", ");
		}
		if (descricao != null) {
			builder.append("descricao=").append(descricao).append(", ");
		}
		builder.append("]");
		return builder.toString();
	}

	public AbrangenciaDTO(ScopeDomain abr) {
		super();
		this.setCodigo(abr.getId());
		this.nome = abr.getName();
		this.descricao = abr.getDescription();

	}

	public AbrangenciaDTO(Long codigo, String nome, String descricao) {
		super(codigo);
		this.nome = nome;
		this.descricao = descricao;
	}

	public AbrangenciaDTO() {
		super();

	}

	public AbrangenciaDTO(Long codigo) {
		super(codigo);

	}

}
