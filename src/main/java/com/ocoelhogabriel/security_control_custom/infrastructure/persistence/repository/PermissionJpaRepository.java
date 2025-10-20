package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Permission;
import jakarta.transaction.Transactional;

public interface PermissionJpaRepository extends JpaRepository<Permission, Long> {

	@Transactional
	void deleteByPerfil_Percod(Long percod);

	Optional<List<Permission>> findByPerfil_percod(Long codigo);

	Optional<List<Permission>> findByPerfil_PercodAndPemcodIn(Long codigo, Collection<Long> list);

	Optional<Permission> findByPerfil_percodAndRecurso_recnom(Long codigo, String recurso);

	List<Permission> findByPemcodIn(Collection<Long> list);

	Page<Permission> findByPemcodIn(Pageable page, Collection<Long> list);

	Page<Permission> findByRecurso_RecnomLike(String nome, Pageable page);

	Page<Permission> findByRecurso_RecnomLikeAndPemcodIn(String nome, Pageable page, Collection<Long> list);

}
