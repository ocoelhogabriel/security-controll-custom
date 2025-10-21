package com.ocoelhogabriel.security_control_custom.domain.repository;

import com.ocoelhogabriel.security_control_custom.domain.entity.PlanDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PlanRepository extends DomainRepository<PlanDomain, Long> {

    Page<PlanDomain> findAll(Pageable pageable, String name, Map<String, Object> scopeFilters);

    List<PlanDomain> findAll(Map<String, Object> scopeFilters);

    Optional<PlanDomain> findById(Long id, Map<String, Object> scopeFilters);

    Optional<PlanDomain> findByName(String name);

}
