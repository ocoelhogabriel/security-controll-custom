package com.ocoelhogabriel.security_control_custom.application.dto;

import com.ocoelhogabriel.security_control_custom.domain.entity.CompanyDomain;
import com.ocoelhogabriel.security_control_custom.domain.entity.PlanDomain;

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

	public PlantaDTO(PlanDomain planDomain) {
		this.setCodigo(planDomain.getId());
		this.empresa = new EmpresaDTO(planDomain.getCompanyDomain());
		this.nome = planDomain.getName();
	}

	public PlantaDTO(PlanDomain planDomain, CompanyDomain companyDomain) {
		this.setCodigo(planDomain.getId());
		this.empresa = companyDomain == null ? null : new EmpresaDTO(companyDomain);
		this.nome = planDomain.getName();
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
