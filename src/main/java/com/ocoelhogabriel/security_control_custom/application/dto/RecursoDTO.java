package com.ocoelhogabriel.security_control_custom.application.dto;

import com.ocoelhogabriel.security_control_custom.domain.entity.ResourcesDomain;

public class RecursoDTO extends CodigoExtends {

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
		builder.append("RecursoDTO [");
		if (nome != null) {
			builder.append("nome=").append(nome).append(", ");
		}
		if (descricao != null) {
			builder.append("descricao=").append(descricao);
		}
		builder.append("]");
		return builder.toString();
	}

	public RecursoDTO(Long codigo, String nome, String descricao) {
		super(codigo);
		this.nome = nome;
		this.descricao = descricao;
	}

	public RecursoDTO(ResourcesDomain recDomain) {
		super();
		this.setCodigo(recDomain.getId());
		this.setNome(recDomain.getName());
		this.setDescricao(recDomain.getDescription());

	}

	public RecursoDTO() {
		super();

	}

	public RecursoDTO(Long codigo) {
		super(codigo);

	}

}
