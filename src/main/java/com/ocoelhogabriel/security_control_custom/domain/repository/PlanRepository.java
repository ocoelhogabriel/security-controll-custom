package com.ocoelhogabriel.security_control_custom.domain.repository;

import com.ocoelhogabriel.security_control_custom.domain.entity.PlanDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlanRepository extends DomainRepository<PlanDomain, Long> {

    Page<PlanDomain> findAll(String spec, List<Long> listId, Pageable pageable);
}
