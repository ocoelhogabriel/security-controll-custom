package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Permission;
import jakarta.transaction.Transactional;

public interface PermissionJpaRepository extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {

	@Transactional
	void deleteByProfile_Id(Long profileId);

	Optional<List<Permission>> findByProfile_Id(Long profileId);

	Optional<List<Permission>> findByProfile_IdAndIdIn(Long profileId, Collection<Long> ids);

	Optional<Permission> findByProfile_IdAndResource_Name(Long profileId, String resourceName);

	List<Permission> findByIdIn(Collection<Long> ids);

	Page<Permission> findByIdIn(Pageable page, Collection<Long> ids);

	Page<Permission> findByResource_NameLike(String name, Pageable page);

	Page<Permission> findByResource_NameLikeAndIdIn(String name, Pageable page, Collection<Long> ids);

}
