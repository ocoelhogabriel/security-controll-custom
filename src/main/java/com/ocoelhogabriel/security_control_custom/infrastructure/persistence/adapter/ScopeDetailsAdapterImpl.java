package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.adapter;

import com.ocoelhogabriel.security_control_custom.domain.entity.ScopeDetailsDomain;
import com.ocoelhogabriel.security_control_custom.domain.repository.ScopeDetailsRepository;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.ScopeDetails;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.mapper.ScopeDetailsMapper;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository.ScopeDetailsJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ScopeDetailsAdapterImpl implements ScopeDetailsRepository {

    private final ScopeDetailsJpaRepository scopeDetailsJpaRepository;
    private final ScopeDetailsMapper scopeDetailsMapper;

    @Autowired
    public ScopeDetailsAdapterImpl(ScopeDetailsJpaRepository scopeDetailsJpaRepository, ScopeDetailsMapper scopeDetailsMapper) {
        this.scopeDetailsJpaRepository = scopeDetailsJpaRepository;
        this.scopeDetailsMapper = scopeDetailsMapper;
    }

    @Override
    public ScopeDetailsDomain save(ScopeDetailsDomain entity) {
        ScopeDetails scopeDetailsEntity = scopeDetailsMapper.toEntity(entity);
        ScopeDetails savedEntity = scopeDetailsJpaRepository.save(scopeDetailsEntity);
        return scopeDetailsMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<ScopeDetailsDomain> findById(Long id) {
        return scopeDetailsJpaRepository.findById(id).map(scopeDetailsMapper::toDomain);
    }

    @Override
    public List<ScopeDetailsDomain> findAll() {
        return scopeDetailsJpaRepository.findAll().stream()
                .map(scopeDetailsMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ScopeDetailsDomain> findAll(Pageable pageable) {
        return scopeDetailsJpaRepository.findAll(pageable).map(scopeDetailsMapper::toDomain);
    }

    @Override
    public void delete(ScopeDetailsDomain entity) {
        scopeDetailsJpaRepository.delete(scopeDetailsMapper.toEntity(entity));
    }

    @Override
    public void deleteById(Long id) {
        scopeDetailsJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return scopeDetailsJpaRepository.existsById(id);
    }

    @Override
    public List<ScopeDetailsDomain> findByScopeId(Long scopeId) {
        return scopeDetailsJpaRepository.findByScope_id(scopeId).stream()
                .map(scopeDetailsMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScopeDetailsDomain> findByScopeIdAndResourceNameContaining(Long scopeId, String resourceName) {
        return scopeDetailsJpaRepository.findByScope_idAndResource_nameContaining(scopeId, resourceName).stream()
                .map(scopeDetailsMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ScopeDetailsDomain> findByScopeIdAndResourceId(Long scopeId, Long resourceId) {
        return scopeDetailsJpaRepository.findByScope_idAndResource_id(scopeId, resourceId).map(scopeDetailsMapper::toDomain);
    }
}
