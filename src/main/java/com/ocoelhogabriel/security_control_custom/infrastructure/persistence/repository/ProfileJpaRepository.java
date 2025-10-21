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

	List<Profile> findByIdIn(Collection<Long> list);

	Page<Profile> findByIdIn(Pageable page, Collection<Long> list);

	Page<Profile> findByNameLike(String name, Pageable page);

	Page<Profile> findByNameLikeAndIdIn(String name, Pageable page, Collection<Long> list);

	Optional<Profile> findByName(String name);
}
