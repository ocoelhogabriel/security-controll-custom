package com.ocoelhogabriel.security_control_custom.domain.repository;

import com.ocoelhogabriel.security_control_custom.domain.entity.ProfileDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends DomainRepository<ProfileDomain, Long> {

    List<ProfileDomain> findByIdIn(Collection<Long> ids);

    Page<ProfileDomain> findByIdIn(Pageable pageable, Collection<Long> ids);

    Page<ProfileDomain> findByNameLike(String name, Pageable pageable);

    Page<ProfileDomain> findByNameLikeAndIdIn(String name, Pageable pageable, Collection<Long> ids);

    Optional<ProfileDomain> findByName(String name);
}
