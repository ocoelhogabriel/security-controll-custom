package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.specification;

import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Plan;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlanSpecifications {

    public static Specification<Plan> withScopeFilters(Map<String, Object> scopeFilters) {
        return (root, query, criteriaBuilder) -> {
            if (scopeFilters != null && scopeFilters.containsKey("plantId")) {
                Object rawValue = scopeFilters.get("plantId");
                if (rawValue instanceof Collection) {
                    @SuppressWarnings("unchecked")
                    Collection<?> rawIds = (Collection<?>) rawValue;
                    if (rawIds.isEmpty()) {
                        return criteriaBuilder.disjunction(); // Explicit empty list denies access.
                    }
                    List<Long> ids = rawIds.stream()
                            .map(obj -> Long.valueOf(String.valueOf(obj).split("\\.")[0]))
                            .collect(Collectors.toList());
                    return root.get("id").in(ids);
                }
            }
            return criteriaBuilder.conjunction(); // No plantId filter, so no restriction from this spec.
        };
    }

    public static Specification<Plan> withNameLike(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }
}
