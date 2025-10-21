package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.adapter;

import com.ocoelhogabriel.security_control_custom.domain.entity.ResourcesDomain;
import com.ocoelhogabriel.security_control_custom.domain.repository.ResourcesRepository;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Resources;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.mapper.ResourcesMapper;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository.ResourcesJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ResourcesAdapterImpl implements ResourcesRepository {

    private final ResourcesMapper resourcesMapper;
    private final ResourcesJpaRepository resourcesJpaRepository;

    @Autowired
    public ResourcesAdapterImpl(ResourcesMapper resourcesMapper, ResourcesJpaRepository resourcesJpaRepository) {
        this.resourcesMapper = resourcesMapper;
        this.resourcesJpaRepository = resourcesJpaRepository;
    }

    private void validateResourcesDomainFields(ResourcesDomain recursoModel) {
        Objects.requireNonNull(recursoModel.getName(), "Nome do Recurso está nulo.");
        Objects.requireNonNull(recursoModel.getDescription(), "Descrição do Recurso está nulo.");
    }

    @Override
    public ResourcesDomain save(ResourcesDomain entity) {
        validateResourcesDomainFields(entity);
        return resourcesMapper.toDomain(resourcesJpaRepository.save(resourcesMapper.toEntity(entity)));
    }

    @Override
    public Optional<ResourcesDomain> findById(Long aLong) {
        return resourcesJpaRepository.findById(aLong).map(resourcesMapper::toDomain);
    }

    @Override
    public List<ResourcesDomain> findAll() {
        return resourcesJpaRepository.findAll().stream().map(resourcesMapper::toDomain).toList();
    }

    @Override
    public Page<ResourcesDomain> findAll(Pageable pageable) {
        return resourcesJpaRepository.findAll(pageable).map(resourcesMapper::toDomain);
    }

    @Override
    public void delete(ResourcesDomain entity) {
        resourcesJpaRepository.delete(resourcesMapper.toEntity(entity));
    }

    @Override
    public void deleteById(Long aLong) {
        Objects.requireNonNull(aLong, "Código do Recurso está nulo.");
        resourcesJpaRepository.deleteById(aLong);

    }

    @Override
    public boolean existsById(Long aLong) {
        return resourcesJpaRepository.existsById(aLong);
    }

    @Override
    public Optional<ResourcesDomain> findByName(String name) {
        return resourcesJpaRepository.findByName(name).map(resourcesMapper::toDomain);
    }

    @Override
    public List<ResourcesDomain> findByIdIn(Collection<Long> ids) {
        return resourcesJpaRepository.findByIdIn(ids).stream().map(resourcesMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Page<ResourcesDomain> findByIdIn(Pageable pageable, Collection<Long> ids) {
        return resourcesJpaRepository.findByIdIn(pageable, ids).map(resourcesMapper::toDomain);
    }

    @Override
    public Page<ResourcesDomain> findByNameLike(String name, Pageable pageable) {
        return resourcesJpaRepository.findByNameLike(name, pageable).map(resourcesMapper::toDomain);
    }

    @Override
    public Page<ResourcesDomain> findByNameLikeAndIdIn(String name, Pageable pageable, Collection<Long> ids) {
        return resourcesJpaRepository.findByNameLikeAndIdIn(name, pageable, ids).map(resourcesMapper::toDomain);
    }
}
