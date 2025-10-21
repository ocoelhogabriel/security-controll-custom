package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "company")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "document", nullable = false)
    private Long document;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "tradeName")
    private String tradeName;

    @Column(name = "contact")
    private String contact;

    public Company(Long id, String name, String tradeName, String contact) {
        super();
        this.id = id;
        this.name = name;
        this.tradeName = tradeName;
        this.contact = contact;
    }

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

        String builder =
                "CompanyDomain [id=" + id + ", document=" + document + ", name=" + name + ", tradeName=" + tradeName + ", contact=" + contact + "]";
        return builder;
    }

    public Company(Long id, Long document, String name, String tradeName, String contact) {
        super();
        this.id = id;
        this.document = document;
        this.name = name;
        this.tradeName = tradeName;
        this.contact = contact;

    }

    public Company() {
        super();

    }

}
