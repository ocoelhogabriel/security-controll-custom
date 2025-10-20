package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Company;

public interface CompanyJpaRepository {

	Optional<Company> findByEmpcnp(Long empcnp);

	Page<Company> findByEmpcodIn(Pageable pageable, Collection<Long> list);

	Page<Company> findByEmpnomLike(String empnom, Pageable pageable);

	Page<Company> findByEmpnomLikeAndEmpcodIn(String empnom, Pageable pageable, Collection<Long> list);

	List<Company> findByEmpcodIn(Collection<Long> abdcods);

}
