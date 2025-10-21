package com.ocoelhogabriel.security_control_custom.domain.entity;

public class PlanDomain {

    private Long id;
    private String name;
    private CompanyDomain companyDomain;

    public PlanDomain() {
    }

    public PlanDomain(Long id, String name, CompanyDomain companyDomain) {
        this.id = id;
        this.name = name;
        this.companyDomain = companyDomain;
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

    public CompanyDomain getCompanyDomain() {
        return companyDomain;
    }

    public void setCompanyDomain(CompanyDomain companyDomain) {
        this.companyDomain = companyDomain;
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
        if (companyDomain != null) {
            builder.append("companyDomain=").append(companyDomain);
        }
        builder.append("]");
        return builder.toString();
    }

}
