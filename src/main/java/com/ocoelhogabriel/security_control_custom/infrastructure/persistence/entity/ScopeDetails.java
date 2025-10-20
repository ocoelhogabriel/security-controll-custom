package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "scope_details")
public class ScopeDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_scope", nullable = false)
	private Scope scope;

	@ManyToOne
	@JoinColumn(name = "id_resource", nullable = false)
	private Resources resource;

	@Column(name = "hierarchy", nullable = false)
	private Integer hierarchy;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(nullable = false, columnDefinition = "jsonb")
	private String abddat;

	public Long getId() {
		return id;
	}

	public void setId(Long abdcod) {
		this.id = abdcod;
	}

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	public Resources getResource() {
		return resource;
	}

	public void setReccod(Resources recnom) {
		this.resource = recnom;
	}

	public Integer getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Integer abdhie) {
		this.hierarchy = abdhie;
	}

	public String getAbddat() {
		return abddat;
	}

	public void setAbddat(String abddat) {
		this.abddat = abddat;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ScopeDetailsDomain [");
		if (id != null) {
			builder.append("abdcod=").append(id).append(", ");
		}
		if (scope != null) {
			builder.append("abrangencia=").append(scope).append(", ");
		}
		if (resource != null) {
			builder.append("reccod=").append(resource).append(", ");
		}
		if (hierarchy != null) {
			builder.append("abdhie=").append(hierarchy).append(", ");
		}
		if (abddat != null) {
			builder.append("abddat=").append(abddat);
		}
		builder.append("]");
		return builder.toString();
	}

	public ScopeDetails(Long id, Scope scope, Resources resource, Integer hierarchy, String abddat) {
		this.id = id;
		this.scope = scope;
		this.resource = resource;
		this.hierarchy = hierarchy;
		this.abddat = abddat;
	}

	public ScopeDetails() {
		super();

	}

}
