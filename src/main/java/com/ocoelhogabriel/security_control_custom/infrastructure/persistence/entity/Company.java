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

    public static Specification<Company> filterByFields(String searchTerm, List<Long> listAbrangencia) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (listAbrangencia != null && !listAbrangencia.isEmpty()) {
                predicates.add(root.get("id").in(listAbrangencia));
            }

            if (searchTerm != null && !searchTerm.isEmpty()) {
                String likePattern = "%" + searchTerm.toLowerCase() + "%";

                List<Predicate> searchPredicates = new ArrayList<>();

                // Add predicates for string fields
                searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern));
                searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("tradeName")), likePattern));
                searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("contact")), likePattern));

                // Attempt to convert the search term to Long and Integer
                try {
                    Long searchTermLong = Long.valueOf(searchTerm);
                    searchPredicates.add(criteriaBuilder.equal(root.get("id"), searchTermLong));
                    searchPredicates.add(criteriaBuilder.equal(root.get("document"), searchTermLong));
                } catch (NumberFormatException e) {
                    // Ignore if the conversion fails
                }

                predicates.add(criteriaBuilder.or(searchPredicates.toArray(Predicate[]::new)));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

}
