package com.ocoelhogabriel.security_control_custom.domain.entity;

public class PermissionDomain {
    private Long id;
    private ProfileDomain profileDomain;
    private ResourcesDomain resource;
    private Boolean list;
    private Boolean find;
    private Boolean create;
    private Boolean edit;
    private Boolean delete;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProfileDomain getProfileDomain() {
        return profileDomain;
    }

    public void setProfileDomain(ProfileDomain profileDomain) {
        this.profileDomain = profileDomain;
    }

    public ResourcesDomain getResource() {
        return resource;
    }

    public void setResource(ResourcesDomain resource) {
        this.resource = resource;
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
        if (profileDomain != null) {
            builder.append("perfil=").append(profileDomain).append(", ");
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

    public PermissionDomain(Long id, ProfileDomain profileDomain, ResourcesDomain resource, Boolean list, Boolean find, Boolean create, Boolean edit,
            Boolean delete) {
        super();
        this.id = id;
        this.profileDomain = profileDomain;
        this.resource = resource;
        this.list = list;
        this.find = find;
        this.create = create;
        this.edit = edit;
        this.delete = delete;
    }

    public PermissionDomain() {
        super();

    }

}
