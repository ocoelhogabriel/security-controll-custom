package com.ocoelhogabriel.security_control_custom.domain.repository;

import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.ScopeDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScopeDetailsRepository extends DomainRepository<ScopeDetails, Long> {


}
