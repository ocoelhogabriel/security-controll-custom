package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.ScopeDetails;

public interface ScopeDetailsJpaRepository extends JpaRepository<ScopeDetails, Long> {

	List<ScopeDetails> findByScope_id(Long id);

	List<ScopeDetails> findByScope_idAndResource_nameContaining(Long id, String name);

    Optional<ScopeDetails> findByScope_idAndResource_id(Long scope, Long resource);
}
