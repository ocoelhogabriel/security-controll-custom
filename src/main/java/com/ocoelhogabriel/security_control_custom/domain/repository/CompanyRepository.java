package com.ocoelhogabriel.security_control_custom.domain.repository;

import com.ocoelhogabriel.security_control_custom.domain.entity.CompanyDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CompanyRepository extends DomainRepository<CompanyDomain, Long> {

    Optional<CompanyDomain> findByDocument(Long document);

    Page<CompanyDomain> findAll(Pageable pageable, String name, Map<String, Object> scopeFilters);

    List<CompanyDomain> findAll(Map<String, Object> scopeFilters);

    Optional<CompanyDomain> findById(Long id, Map<String, Object> scopeFilters);

    Optional<CompanyDomain> findByDocument(Long document, Map<String, Object> scopeFilters);
}
