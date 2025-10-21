package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.criteria.Predicate;

@Entity
@Table(name = "scope")
public class Scope implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description")
	private String description;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ScopeDomain [");
		if (id != null) {
			builder.append("id=").append(id).append(", ");
		}
		if (name != null) {
			builder.append("name=").append(name).append(", ");
		}
		if (description != null) {
			builder.append("description=").append(description);
		}
		builder.append("]");
		return builder.toString();
	}

	public Scope(Long id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public Scope() {
		super();

	}


}
