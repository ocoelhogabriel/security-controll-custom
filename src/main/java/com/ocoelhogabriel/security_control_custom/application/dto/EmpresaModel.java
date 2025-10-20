package com.ocoelhogabriel.security_control_custom.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Empresa")
public class EmpresaModel {

	@NotBlank(message = "O campo 'cnpj' é obrigatório e não pode estar em branco.")
	@Schema(name = "cnpj", description = "CNPJ da Empresa", example = "21233728172312", format = "Integer")
	private Long cnpj;

	@NotBlank(message = "O campo 'nome' é obrigatório e não pode estar em branco.")
	@Schema(name = "nome", description = "Nome da Empresa", example = "Nome Empresa X", format = "String")
	private String nome;

	@Schema(name = "nomeFantasia", description = "Nome Fantasia da Empresa", example = "Empresa X", format = "String")
	private String nomeFantasia;

	@Schema(name = "telefone", description = "Telefone da Empresa", example = "(99)99999-9999", format = "String")
	private String telefone;

	public Long getCnpj() {
		return cnpj;
	}

	public void setCnpj(Long cnpj) {
		this.cnpj = cnpj;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EmpresaModel [");
		if (cnpj != null) {
			builder.append("cnpj=").append(cnpj).append(", ");
		}
		if (nome != null) {
			builder.append("nome=").append(nome).append(", ");
		}
		if (nomeFantasia != null) {
			builder.append("nomeFantasia=").append(nomeFantasia).append(", ");
		}
		if (telefone != null) {
			builder.append("telefone=").append(telefone);
		}
		builder.append("]");
		return builder.toString();
	}

	public EmpresaModel(Long cnpj, String nome, String nomeFantasia, String telefone) {
		super();
		this.cnpj = cnpj;
		this.nome = nome;
		this.nomeFantasia = nomeFantasia;
		this.telefone = telefone;
	}

	public EmpresaModel() {
		super();

	}

}
