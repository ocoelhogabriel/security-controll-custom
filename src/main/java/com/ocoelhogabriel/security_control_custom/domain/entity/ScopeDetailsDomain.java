package com.ocoelhogabriel.security_control_custom.domain.entity;

public class ScopeDetailsDomain {

    private Long id;
    private ScopeDomain scopeDomain;
    private ResourcesDomain resource;
    private Integer hierarchy;
    private String data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ScopeDomain getScopeDomain() {
        return scopeDomain;
    }

    public void setScopeDomain(ScopeDomain scopeDomain) {
        this.scopeDomain = scopeDomain;
    }

    public ResourcesDomain getResource() {
        return resource;
    }

    public void setResource(ResourcesDomain resource) {
        this.resource = resource;
    }

    public Integer getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(Integer hierarchy) {
        this.hierarchy = hierarchy;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ScopeDetailsDomain [");
        if (id != null) {
            builder.append("id=").append(id).append(", ");
        }
        if (scopeDomain != null) {
            builder.append("scopeDomain=").append(scopeDomain).append(", ");
        }
        if (resource != null) {
            builder.append("resource=").append(resource).append(", ");
        }
        if (hierarchy != null) {
            builder.append("hierarchy=").append(hierarchy).append(", ");
        }
        if (data != null) {
            builder.append("data=").append(data);
        }
        builder.append("]");
        return builder.toString();
    }

    public ScopeDetailsDomain(Long id, ScopeDomain scopeDomain, ResourcesDomain resource, Integer hierarchy, String data) {
        this.id = id;
        this.scopeDomain = scopeDomain;
        this.resource = resource;
        this.hierarchy = hierarchy;
        this.data = data;
    }

    public ScopeDetailsDomain() {
        super();

    }

}
