package com.ocoelhogabriel.security_control_custom.application.dto;

import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Company;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Plan;

public class PlantaDTO extends CodigoExtends {

	private EmpresaDTO empresa;
	private String nome;

	public PlantaDTO(EmpresaDTO empresa, String nome) {
		this.empresa = empresa;
		this.nome = nome;
	}

	public static String filtrarDirecao(String str) {
		switch (str.toUpperCase()) {
		case "CODIGO" -> {
			return "placod";
		}
		case "EMPRESA" -> {
			return "empcod";
		}
		case "NOME" -> {
			return "planom";
		}
		default -> throw new AssertionError();
		}
	}

	public PlantaDTO(Long codigo) {
		super(codigo);
	}

	public PlantaDTO(Plan plan) {

		this.setCodigo(plan.getId());
		this.empresa = new EmpresaDTO(plan.getEmpresa());
		this.nome = plan.getName();
	}

	public PlantaDTO(Plan plan, Company company) {

		this.setCodigo(plan.getId());
		this.empresa = company == null ? null : new EmpresaDTO(company);
		this.nome = plan.getName();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PlantaDTO [");
		if (empresa != null)
			builder.append("empresa=").append(empresa).append(", ");
		if (nome != null)
			builder.append("nome=").append(nome);
		builder.append("]");
		return builder.toString();
	}

	public EmpresaDTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaDTO empresa) {
		this.empresa = empresa;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
