package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository;

import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CompanyJpaRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {

    Optional<Company> findByDocument(Long document);

}
