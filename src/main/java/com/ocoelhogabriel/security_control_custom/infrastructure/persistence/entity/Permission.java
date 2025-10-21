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

	@Column(name = "can_list", nullable = false) // Renomeado de 'list'
	private Boolean list;

	@Column(name = "can_find", nullable = false) // Renomeado de 'find'
	private Boolean find;

	@Column(name = "can_create", nullable = false) // Renomeado de 'create'
	private Boolean create;

	@Column(name = "can_edit", nullable = false) // Renomeado de 'edit'
	private Boolean edit;

	@Column(name = "can_delete", nullable = false) // Renomeado de 'delete'
	private Boolean delete;

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

	public Boolean getList() {
		return list;
	}

	public void setList(Boolean list) {
		this.list = list;
	}

	public Boolean getFind() {
		return find;
	}

	public void setFind(Boolean find) {
		this.find = find;
	}

	public Boolean getCreate() {
		return create;
	}

	public void setCreate(Boolean create) {
		this.create = create;
	}

	public Boolean getEdit() {
		return edit;
	}

	public void setEdit(Boolean edit) {
		this.edit = edit;
	}

	public Boolean getDelete() {
		return delete;
	}

	public void setDelete(Boolean delete) {
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

	public Permission(Long id, Profile profile, Resources resource, Boolean list, Boolean find, Boolean create, Boolean edit, Boolean delete) {
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
