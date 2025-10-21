package com.ocoelhogabriel.security_control_custom.domain.repository;

import com.ocoelhogabriel.security_control_custom.domain.entity.ScopeDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ScopeRepository extends DomainRepository<ScopeDomain, Long> {

    Page<ScopeDomain> findByIdIn(Pageable pageable, Collection<Long> ids);

    Page<ScopeDomain> findByNameLike(String name, Pageable pageable);

    Page<ScopeDomain> findByNameLikeAndIdIn(String name, Pageable pageable, Collection<Long> ids);

    List<ScopeDomain> findByIdIn(Collection<Long> ids);

    Optional<ScopeDomain> findByNameLike(String name);
}
