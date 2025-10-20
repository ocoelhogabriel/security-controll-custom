package com.ocoelhogabriel.security_control_custom.domain.entity;

public class PermissionDomain {
    private Long id;
    private ProfileDomain profileDomain;
    private ResourcesDomain resource;
    private Integer list;
    private Integer find;
    private Integer create;
    private Integer edit;
    private Integer delete;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProfileDomain getPerfil() {
        return profileDomain;
    }

    public void setPerfil(ProfileDomain profileDomain) {
        this.profileDomain = profileDomain;
    }

    public Integer getList() {
        return list;
    }

    public void setList(Integer list) {
        this.list = list;
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

    public PermissionDomain(Long id, ProfileDomain profileDomain, ResourcesDomain resource, Integer list, Integer find, Integer create, Integer edit, Integer delete) {
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
