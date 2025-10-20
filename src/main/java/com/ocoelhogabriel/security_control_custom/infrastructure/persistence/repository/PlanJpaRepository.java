package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Plan;

public interface PlanJpaRepository extends JpaRepository<Plan, Long>, JpaSpecificationExecutor<Plan> {

	void removeByPlacod(Long codigo);

}
