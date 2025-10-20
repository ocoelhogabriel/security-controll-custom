package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "permission")
public class Permission {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_profile", nullable = false)
	private Profile profile;

	@ManyToOne
	@JoinColumn(name = "id_resource", nullable = false)
	private Resources resource;

	@Column(name = "list", nullable = false)
	private Integer list;

	@Column(name = "find", nullable = false)
	private Integer find;

	@Column(name = "create", nullable = false)
	private Integer create;

	@Column(name = "edit", nullable = false)
	private Integer edit;

	@Column(name = "delete", nullable = false)
	private Integer delete;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Profile getPerfil() {
		return profile;
	}

	public void setPerfil(Profile profile) {
		this.profile = profile;
	}

	public Resources getResource() {
		return resource;
	}

	public void setReccod(Resources recnom) {
		this.resource = recnom;
	}

	public Integer getList() {
		return list;
	}

	public void setList(Integer list) {
		this.list = list;
	}

	public Integer getFind() {
		return find;
	}

	public void setFind(Integer find) {
		this.find = find;
	}

	public Integer getCreate() {
		return create;
	}

	public void setCreate(Integer create) {
		this.create = create;
	}

	public Integer getEdit() {
		return edit;
	}

	public void setEdit(Integer edit) {
		this.edit = edit;
	}

	public Integer getDelete() {
		return delete;
	}

	public void setDelete(Integer delete) {
		this.delete = delete;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PermissionDomain [");
		if (id != null) {
			builder.append("id=").append(id).append(", ");
		}
		if (profile != null) {
			builder.append("perfil=").append(profile).append(", ");
		}
		if (resource != null) {
			builder.append("recnom=").append(resource).append(", ");
		}
		if (list != null) {
			builder.append("list=").append(list).append(", ");
		}
		if (find != null) {
			builder.append("find=").append(find).append(", ");
		}
		if (create != null) {
			builder.append("create=").append(create).append(", ");
		}
		if (edit != null) {
			builder.append("edit=").append(edit).append(", ");
		}
		if (delete != null) {
			builder.append("delete=").append(delete);
		}
		builder.append("]");
		return builder.toString();
	}

	public Permission(Long id, Profile profile, Resources resource, Integer list, Integer find, Integer create, Integer edit, Integer delete) {
		super();
		this.id = id;
		this.profile = profile;
		this.resource = resource;
		this.list = list;
		this.find = find;
		this.create = create;
		this.edit = edit;
		this.delete = delete;
	}

	public Permission() {
		super();

	}

}
