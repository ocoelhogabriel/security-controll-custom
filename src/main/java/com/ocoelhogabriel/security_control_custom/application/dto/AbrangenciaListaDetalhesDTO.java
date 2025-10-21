package com.ocoelhogabriel.security_control_custom.application.dto;

import java.util.List;

import com.ocoelhogabriel.security_control_custom.domain.entity.ScopeDomain;

public class AbrangenciaListaDetalhesDTO extends CodigoExtends {

	private String nome;

	private String descricao;

	private List<AbrangenciaDetalhesDTO> recursos;

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

	public List<AbrangenciaDetalhesDTO> getRecursos() {
		return recursos;
	}

	public void setRecursos(List<AbrangenciaDetalhesDTO> recursos) {
		this.recursos = recursos;
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
		if (recursos != null) {
			builder.append("recursos=").append(recursos);
		}
		builder.append("]");
		return builder.toString();
	}

	public AbrangenciaListaDetalhesDTO(ScopeDomain abrDomain, List<AbrangenciaDetalhesDTO> recursos) {
		super();
		this.setCodigo(abrDomain.getId());
		this.nome = abrDomain.getName();
		this.descricao = abrDomain.getDescription();
		this.recursos = recursos;

	}

	public AbrangenciaListaDetalhesDTO(Long codigo, String nome, String descricao, List<AbrangenciaDetalhesDTO> recursos) {
		super(codigo);
		this.nome = nome;
		this.descricao = descricao;
		this.recursos = recursos;
	}

	public AbrangenciaListaDetalhesDTO() {
		super();

	}

	public AbrangenciaListaDetalhesDTO(Long codigo) {
		super(codigo);

	}

}
