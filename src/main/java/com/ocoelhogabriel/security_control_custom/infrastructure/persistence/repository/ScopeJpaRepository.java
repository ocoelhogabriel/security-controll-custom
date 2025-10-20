package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Scope;

public interface ScopeJpaRepository extends JpaRepository<Scope, Long>, JpaSpecificationExecutor<Scope> {

	Page<Scope> findByAbrcodIn(Pageable pageable, Collection<Long> list);

	Page<Scope> findByAbrnomLike(String nome, Pageable pageable);

	Page<Scope> findByAbrnomLikeAndAbrcodIn(String nome, Pageable pageable, Collection<Long> list);

	List<Scope> findByAbrcodIn(Collection<Long> list);

	Optional<Scope> findByAbrnomLike(String nome);
}
