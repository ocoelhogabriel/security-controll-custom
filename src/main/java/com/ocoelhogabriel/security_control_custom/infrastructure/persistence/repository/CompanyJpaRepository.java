package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyJpaRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByDocument(Long document);

    Page<Company> findByIdIn(Pageable pageable, Collection<Long> list);

    Page<Company> findByNameLike(String name, Pageable pageable);

    Page<Company> findByNameLikeAndIdIn(String name, Pageable pageable, Collection<Long> ids);

    List<Company> findByIdIn(Collection<Long> ids);

}
