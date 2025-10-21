package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.specification;

import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.User;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserSpecifications {

    public static Specification<User> withScopeFilters(Map<String, Object> scopeFilters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (scopeFilters != null && scopeFilters.containsKey("companyId")) {
                Object rawValue = scopeFilters.get("companyId");
                if (rawValue instanceof Collection) {
                    @SuppressWarnings("unchecked")
                    Collection<?> rawIds = (Collection<?>) rawValue;
                    if (!rawIds.isEmpty()) {
                        List<Long> ids = rawIds.stream()
                                .map(obj -> Long.valueOf(String.valueOf(obj).split("\\.")[0]))
                                .collect(Collectors.toList());
                        predicates.add(root.get("company").get("id").in(ids));
                    }
                }
            }

            if (predicates.isEmpty()) {
                return criteriaBuilder.conjunction(); // No filters, return all
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<User> withLoginFilter(String login) {
        return (root, query, criteriaBuilder) -> {
            if (login == null || login.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("login")), "%" + login.toLowerCase() + "%");
        };
    }
}
