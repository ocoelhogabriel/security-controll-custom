package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.criteria.Predicate;

@Entity
@Table(name = "plan")
public class Plan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;
	@Column(length = 100, nullable = false)
	private String name;
	@ManyToOne
	@JoinColumn(name = "id_company", nullable = false)
	private Company company;

	public Plan() {
	}

	public Plan(Long id, String name, Company company) {
		this.id = id;
		this.name = name;
		this.company = company;
	}

	public Plan plantaUpdateOrSave(String name, Company company) {
		this.name = name;
		this.company = company;
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Company getEmpresa() {
		return company;
	}

	public void setEmpresa(Company company) {
		this.company = company;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PlanDomain [");
		if (id != null) {
			builder.append("id=").append(id).append(", ");
		}
		if (name != null) {
			builder.append("name=").append(name).append(", ");
		}
		if (company != null) {
			builder.append("company=").append(company);
		}
		builder.append("]");
		return builder.toString();
	}



}
