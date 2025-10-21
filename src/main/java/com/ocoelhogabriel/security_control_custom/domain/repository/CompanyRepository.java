package com.ocoelhogabriel.security_control_custom.domain.repository;

import com.ocoelhogabriel.security_control_custom.domain.entity.CompanyDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends DomainRepository<CompanyDomain, Long> {

    Optional<CompanyDomain> findByDocument(Long document);

    Page<CompanyDomain> findByIdIn(Pageable pageable, Collection<Long> ids);

    Page<CompanyDomain> findByNameLike(String name, Pageable pageable);

    Page<CompanyDomain> findByNameLikeAndIdIn(String name, Pageable pageable, Collection<Long> ids);

    List<CompanyDomain> findByIdIn(Collection<Long> ids);
}
