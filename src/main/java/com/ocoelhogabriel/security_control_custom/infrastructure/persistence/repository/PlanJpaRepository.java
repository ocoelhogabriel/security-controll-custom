package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository;

import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PlanJpaRepository extends JpaRepository<Plan, Long>, JpaSpecificationExecutor<Plan> {
    Optional<Plan> findByName(String name);
}
