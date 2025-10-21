package com.ocoelhogabriel.security_control_custom.domain.repository;

import com.ocoelhogabriel.security_control_custom.domain.entity.ScopeDetailsDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScopeDetailsRepository extends DomainRepository<ScopeDetailsDomain, Long> {

    List<ScopeDetailsDomain> findByScopeId(Long scopeId);

    List<ScopeDetailsDomain> findByScopeIdAndResourceNameContaining(Long scopeId, String resourceName);

    Optional<ScopeDetailsDomain> findByScopeIdAndResourceId(Long scopeId, Long resourceId);
}
