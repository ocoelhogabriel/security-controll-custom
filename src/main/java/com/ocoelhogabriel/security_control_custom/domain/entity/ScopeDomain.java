package com.ocoelhogabriel.security_control_custom.domain.entity;

public class ScopeDomain {

    private Long id;
    private String name;
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

    public ScopeDomain(Long id, String name, String description) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public ScopeDomain() {
        super();

    }

}
