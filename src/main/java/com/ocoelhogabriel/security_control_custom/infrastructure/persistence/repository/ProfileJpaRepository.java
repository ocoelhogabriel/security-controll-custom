package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Profile;

public interface ProfileJpaRepository extends JpaRepository<Profile, Long>, JpaSpecificationExecutor<Profile> {

	List<Profile> findByPercodIn(Collection<Long> list);

	Page<Profile> findByPercodIn(Pageable page, Collection<Long> list);

	Page<Profile> findByPernomLike(String nome, Pageable page);

	Page<Profile> findByPernomLikeAndPercodIn(String nome, Pageable page, Collection<Long> list);

	Optional<Profile> findByPernom(String nome);
}
