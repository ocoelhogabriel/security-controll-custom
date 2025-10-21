package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository;

import com.jayway.jsonpath.JsonPath;
import com.ocoelhogabriel.security_control_custom.domain.entity.PlanDomain;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Plan;

public interface PlanJpaRepository extends JpaRepository<Plan, Long>, JpaSpecificationExecutor<Plan> {

}
