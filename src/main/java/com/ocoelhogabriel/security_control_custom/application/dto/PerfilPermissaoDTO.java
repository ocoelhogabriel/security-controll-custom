package com.ocoelhogabriel.security_control_custom.application.dto;

import java.util.List;

import com.ocoelhogabriel.security_control_custom.domain.entity.ProfileDomain;

public class PerfilPermissaoDTO extends CodigoExtends {

	private String nome;

	private String descricao;

	private List<PermissaoDTO> permissoes;

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

	public List<PermissaoDTO> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<PermissaoDTO> permissoes) {
		this.permissoes = permissoes;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PerfilPermissaoDTO [");
		if (nome != null) {
			builder.append("nome=").append(nome).append(", ");
		}
		if (descricao != null) {
			builder.append("descricao=").append(descricao).append(", ");
		}
		if (permissoes != null) {
			builder.append("permissoes=").append(permissoes);
		}
		builder.append("]");
		return builder.toString();
	}

	public PerfilPermissaoDTO(Long codigo, String nome, String descricao, List<PermissaoDTO> permissoes) {
		super(codigo);
		this.nome = nome;
		this.descricao = descricao;
		this.permissoes = permissoes;
	}

	public PerfilPermissaoDTO(ProfileDomain profileDomain, List<PermissaoDTO> permissoes) {
		super();
		this.setCodigo(profileDomain.getId());
		this.nome = profileDomain.getName();
		this.descricao = profileDomain.getDescription();
		this.permissoes = permissoes;
	}

	public PerfilPermissaoDTO() {
		super();

	}

	public PerfilPermissaoDTO(Long codigo) {
		super(codigo);

	}

}
