package com.ocoelhogabriel.security_control_custom.domain.repository;

import com.ocoelhogabriel.security_control_custom.domain.entity.PermissionDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends DomainRepository<PermissionDomain, Long> {

    void deleteByProfileId(Long profileId);

    Optional<List<PermissionDomain>> findByProfileId(Long profileId);

    Optional<List<PermissionDomain>> findByProfileIdAndIdIn(Long profileId, Collection<Long> ids);

    Optional<PermissionDomain> findByProfileIdAndResourceName(Long profileId, String resourceName);

    List<PermissionDomain> findByIdIn(Collection<Long> ids);

    Page<PermissionDomain> findByIdIn(Pageable pageable, Collection<Long> ids);

    Page<PermissionDomain> findByResourceNameLike(String resourceName, Pageable pageable);

    Page<PermissionDomain> findByResourceNameLikeAndIdIn(String resourceName, Pageable pageable, Collection<Long> ids);
}
