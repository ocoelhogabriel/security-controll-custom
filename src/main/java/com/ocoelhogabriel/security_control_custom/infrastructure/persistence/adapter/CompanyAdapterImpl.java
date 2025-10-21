package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.adapter;

import com.ocoelhogabriel.security_control_custom.domain.entity.CompanyDomain;
import com.ocoelhogabriel.security_control_custom.domain.repository.CompanyRepository;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Company;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.mapper.CompanyMapper;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository.CompanyJpaRepository;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.specification.CompanySpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CompanyAdapterImpl implements CompanyRepository {

    private final CompanyMapper companyMapper;
    private final CompanyJpaRepository companyJpaRepository;

    @Autowired
    public CompanyAdapterImpl(CompanyJpaRepository companyJpaRepository, CompanyMapper companyMapper) {
        this.companyJpaRepository = companyJpaRepository;
        this.companyMapper = companyMapper;
    }

    @Override
    public CompanyDomain save(CompanyDomain entity) {
        Company companyEntity = companyMapper.toEntity(entity);
        Company savedEntity = companyJpaRepository.save(companyEntity);
        return companyMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<CompanyDomain> findById(Long id) {
        return companyJpaRepository.findById(id).map(companyMapper::toDomain);
    }

    @Override
    public List<CompanyDomain> findAll() {
        return companyJpaRepository.findAll().stream().map(companyMapper::toDomain).toList();
    }

    @Override
    public Page<CompanyDomain> findAll(Pageable pageable) {
        return companyJpaRepository.findAll(pageable).map(companyMapper::toDomain);
    }

    @Override
    public void delete(CompanyDomain entity) {
        companyJpaRepository.delete(companyMapper.toEntity(entity));
    }

    @Override
    public void deleteById(Long id) {
        companyJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return companyJpaRepository.existsById(id);
    }

    @Override
    public Optional<CompanyDomain> findByDocument(Long document) {
        return companyJpaRepository.findByDocument(document).map(companyMapper::toDomain);
    }

    @Override
    public Page<CompanyDomain> findAll(Pageable pageable, String name, Map<String, Object> scopeFilters) {
        Specification<Company> spec = Specification.where(CompanySpecifications.withScopeFilters(scopeFilters))
                                                  .and(CompanySpecifications.withNameLike(name));
        return companyJpaRepository.findAll(spec, pageable).map(companyMapper::toDomain);
    }

    @Override
    public List<CompanyDomain> findAll(Map<String, Object> scopeFilters) {
        Specification<Company> spec = CompanySpecifications.withScopeFilters(scopeFilters);
        return companyJpaRepository.findAll(spec).stream().map(companyMapper::toDomain).toList();
    }

    @Override
    public Optional<CompanyDomain> findById(Long id, Map<String, Object> scopeFilters) {
        Specification<Company> spec = Specification.where(CompanySpecifications.withScopeFilters(scopeFilters))
                                                  .and((root, query, cb) -> cb.equal(root.get("id"), id));
        return companyJpaRepository.findOne(spec).map(companyMapper::toDomain);
    }

    @Override
    public Optional<CompanyDomain> findByDocument(Long document, Map<String, Object> scopeFilters) {
        Specification<Company> spec = Specification.where(CompanySpecifications.withScopeFilters(scopeFilters))
                                                  .and((root, query, cb) -> cb.equal(root.get("document"), document));
        return companyJpaRepository.findOne(spec).map(companyMapper::toDomain);
    }
}
