package com.ocoelhogabriel.security_control_custom.application.dto;

import com.ocoelhogabriel.security_control_custom.domain.entity.CompanyDomain;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Company;

public class EmpresaDTO extends CodigoExtends {
	private Long cnpj;
	private String nome;
	private String nomeFantasia;
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

	public static String consultaPagable(String value) {
		switch (value.toUpperCase()) {
		case "CODIGO":
			return "empcod";
		case "CNPJ":
			return "empcnp";
		case "NOME":
			return "empnom";
		case "NOMEFANTASIA":
			return "empfan";
		case "TELEFONE":
			return "emptel";
		default:
			throw new IllegalArgumentException("Unexpected value: " + value);
		}
	}

	@Override
	public String toString() {
        String builder = "EmpresaDto [codigo=" + getCodigo() + ", cnpj=" + cnpj + ", nome=" + nome + ", nomeFantasia=" + nomeFantasia + ", telefone="
                + telefone + "]";
		return builder;
	}

	public EmpresaDTO(Long codigo, Long cnpj, String nome, String nomeFantasia, String telefone) {
		super(codigo);
		this.cnpj = cnpj;
		this.nome = nome;
		this.nomeFantasia = nomeFantasia;
		this.telefone = telefone;
	}

	public EmpresaDTO(CompanyDomain emp) {
		super();
		this.setCodigo(emp.getId());
		this.cnpj = emp.getDocument();
		this.nome = emp.getName();
		this.nomeFantasia = emp.getTradeName();
		this.telefone = emp.getContact();
	}

	public EmpresaDTO() {
		super();
	}

}
