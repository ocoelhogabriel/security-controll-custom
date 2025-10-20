package com.ocoelhogabriel.security_control_custom.domain.repository;

import com.ocoelhogabriel.security_control_custom.domain.entity.UserDomain;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserRepository extends DomainRepository<UserDomain, Long> {

    Optional<UserDomain> findByLogin(String login);

    Page<UserDomain> findAll(String filter, Pageable pageable);
}
