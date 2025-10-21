package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.specification;

import com.ocoelhogabriel.security_control_custom.domain.entity.CompanyDomain;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CompanySpecifications {

    public static Specification<CompanyDomain> withScopeFilters(Map<String, Object> filters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Exemplo: Filtrar por companyId
            if (filters.containsKey("companyId")) {
                Object companyIdObj = filters.get("companyId");
                if (companyIdObj instanceof Long) {
                    Long companyId = (Long) companyIdObj;
                    predicates.add(criteriaBuilder.equal(root.get("id"), companyId));
                } else if (companyIdObj instanceof Integer) { // Handle Integer if JSON parsing returns Integer
                    Long companyId = ((Integer) companyIdObj).longValue();
                    predicates.add(criteriaBuilder.equal(root.get("id"), companyId));
                }
            }

            // Adicione outras condições de filtro aqui conforme necessário
            // Ex: if (filters.containsKey("plantId")) { ... }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<CompanyDomain> withNameLike(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction(); // Retorna uma condição verdadeira se o filtro estiver vazio
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    // Combine specifications
    public static Specification<CompanyDomain> combine(Specification<CompanyDomain> spec1, Specification<CompanyDomain> spec2) {
        return Specification.where(spec1).and(spec2);
    }
}
