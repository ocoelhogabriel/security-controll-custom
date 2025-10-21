package com.ocoelhogabriel.security_control_custom.domain.repository;

import com.ocoelhogabriel.security_control_custom.domain.entity.ResourcesDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ResourcesRepository extends DomainRepository<ResourcesDomain, Long> {

    Optional<ResourcesDomain> findByName(String name);

    List<ResourcesDomain> findByIdIn(Collection<Long> ids);

    Page<ResourcesDomain> findByIdIn(Pageable pageable, Collection<Long> ids);

    Page<ResourcesDomain> findByNameLike(String name, Pageable pageable);

    Page<ResourcesDomain> findByNameLikeAndIdIn(String name, Pageable pageable, Collection<Long> ids);
}
