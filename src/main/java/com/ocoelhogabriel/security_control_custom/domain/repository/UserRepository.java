package com.ocoelhogabriel.security_control_custom.domain.repository;

import com.ocoelhogabriel.security_control_custom.domain.entity.UserDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends DomainRepository<UserDomain, Long> {

    Optional<UserDomain> findByLogin(String login);

    // Este método será refatorado para usar Specification
    Page<UserDomain> findAll(String filter, Pageable pageable);

    List<UserDomain> findByIdIn(Collection<Long> ids);

    Page<UserDomain> findByIdIn(Pageable pageable, Collection<Long> ids);

    Page<UserDomain> findByLoginLike(String login, Pageable pageable);

    Page<UserDomain> findByLoginLikeAndIdIn(String login, Pageable pageable, Collection<Long> ids);
}
