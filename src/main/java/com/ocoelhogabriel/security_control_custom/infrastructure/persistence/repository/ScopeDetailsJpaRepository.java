package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.ScopeDetails;

public interface ScopeDetailsJpaRepository extends JpaRepository<ScopeDetails, Long> {

	List<ScopeDetails> findByAbrangencia_Abrcod(Long codigo);

	List<ScopeDetails> findByAbrangencia_abrcodAndRecurso_recnomContaining(Long codigo, String nome);

}
