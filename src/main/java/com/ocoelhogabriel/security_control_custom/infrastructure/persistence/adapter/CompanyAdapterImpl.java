package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.adapter;

import com.ocoelhogabriel.security_control_custom.domain.entity.CompanyDomain;
import com.ocoelhogabriel.security_control_custom.domain.repository.CompanyRepository;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Company;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.mapper.CompanyMapper;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository.CompanyJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CompanyAdapterImpl implements CompanyRepository {

    private final CompanyMapper companyMapper;
    private final CompanyJpaRepository companyRepository;

    @Autowired
    public CompanyAdapterImpl(CompanyJpaRepository companyJpaRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyJpaRepository;
        this.companyMapper = companyMapper;
    }

    @Override
    public CompanyDomain save(CompanyDomain entity) {
        Company companyEntity = companyMapper.toEntity(entity);
        Company savedEntity = companyRepository.save(companyEntity);
        return companyMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<CompanyDomain> findById(Long id) {
        return companyRepository.findById(id).map(companyMapper::toDomain);
    }

    @Override
    public List<CompanyDomain> findAll() {
        return companyRepository.findAll().stream().map(companyMapper::toDomain).toList();
    }

    @Override
    public Page<CompanyDomain> findAll(Pageable pageable) {
        return companyRepository.findAll(pageable).map(companyMapper::toDomain);
    }

    @Override
    public void delete(CompanyDomain entity) {
        companyRepository.delete(companyMapper.toEntity(entity));
    }

    @Override
    public void deleteById(Long id) {
        companyRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return companyRepository.existsById(id);
    }

    @Override
    public Optional<CompanyDomain> findByDocument(Long document) {
        return companyRepository.findByDocument(document).map(companyMapper::toDomain);
    }

    @Override
    public Page<CompanyDomain> findByIdIn(Pageable pageable, Collection<Long> ids) {
        return companyRepository.findByIdIn(pageable, ids).map(companyMapper::toDomain);
    }

    @Override
    public Page<CompanyDomain> findByNameLike(String name, Pageable pageable) {
        return companyRepository.findByNameLike(name, pageable).map(companyMapper::toDomain);
    }

    @Override
    public Page<CompanyDomain> findByNameLikeAndIdIn(String name, Pageable pageable, Collection<Long> ids) {
        return companyRepository.findByNameLikeAndIdIn(name, pageable, ids).map(companyMapper::toDomain);
    }

    @Override
    public List<CompanyDomain> findByIdIn(Collection<Long> ids) {
        return companyRepository.findByIdIn(ids).stream().map(companyMapper::toDomain).toList();
    }
}
