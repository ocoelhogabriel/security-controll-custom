package com.ocoelhogabriel.security_control_custom.domain.entity;

public class CompanyDomain {

    private Long id;

    private Long document;

    private String name;

    private String tradeName;

    private String contact;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocument() {
        return document;
    }

    public void setDocument(Long document) {
        this.document = document;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CompanyDomain [id=");
        builder.append(id);
        builder.append(", document=");
        builder.append(document);
        builder.append(", name=");
        builder.append(name);
        builder.append(", tradeName=");
        builder.append(tradeName);
        builder.append(", contact=");
        builder.append(contact);
        builder.append("]");
        return builder.toString();
    }

    public CompanyDomain(Long id, Long document, String name, String tradeName, String contact) {
        super();
        this.id = id;
        this.document = document;
        this.name = name;
        this.tradeName = tradeName;
        this.contact = contact;

    }

    public CompanyDomain() {
        super();
    }

}
