package com.ocoelhogabriel.security_control_custom.domain.repository;

import com.ocoelhogabriel.security_control_custom.domain.entity.UserDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserRepository extends DomainRepository<UserDomain, Long> {

    Optional<UserDomain> findByLogin(String login);

    Page<UserDomain> findAll(Pageable pageable, String login, Map<String, Object> scopeFilters);

    List<UserDomain> findAll(Map<String, Object> scopeFilters);

    Optional<UserDomain> findById(Long id, Map<String, Object> scopeFilters);

}
