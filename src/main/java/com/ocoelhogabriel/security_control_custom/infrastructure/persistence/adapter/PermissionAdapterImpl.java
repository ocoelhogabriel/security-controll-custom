package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.adapter;

import com.ocoelhogabriel.security_control_custom.domain.entity.PermissionDomain;
import com.ocoelhogabriel.security_control_custom.domain.repository.PermissionRepository;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Permission;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.mapper.PermissionMapper;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository.PermissionJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PermissionAdapterImpl implements PermissionRepository {

    private final PermissionJpaRepository permissionJpaRepository;
    private final PermissionMapper permissionMapper;

    @Autowired
    public PermissionAdapterImpl(PermissionJpaRepository permissionJpaRepository, PermissionMapper permissionMapper) {
        this.permissionJpaRepository = permissionJpaRepository;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public PermissionDomain save(PermissionDomain entity) {
        Permission permissionEntity = permissionMapper.toEntity(entity);
        Permission savedEntity = permissionJpaRepository.save(permissionEntity);
        return permissionMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<PermissionDomain> findById(Long id) {
        return permissionJpaRepository.findById(id).map(permissionMapper::toDomain);
    }

    @Override
    public List<PermissionDomain> findAll() {
        return permissionJpaRepository.findAll().stream()
                .map(permissionMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<PermissionDomain> findAll(Pageable pageable) {
        return permissionJpaRepository.findAll(pageable).map(permissionMapper::toDomain);
    }

    @Override
    public void delete(PermissionDomain entity) {
        permissionJpaRepository.delete(permissionMapper.toEntity(entity));
    }

    @Override
    public void deleteById(Long id) {
        permissionJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return permissionJpaRepository.existsById(id);
    }

    @Override
    public void deleteByProfileId(Long profileId) {
        permissionJpaRepository.deleteByProfile_Id(profileId);
    }

    @Override
    public Optional<List<PermissionDomain>> findByProfileId(Long profileId) {
        return permissionJpaRepository.findByProfile_Id(profileId)
                .map(permissions -> permissions.stream().map(permissionMapper::toDomain).collect(Collectors.toList()));
    }

    @Override
    public Optional<List<PermissionDomain>> findByProfileIdAndIdIn(Long profileId, Collection<Long> ids) {
        return permissionJpaRepository.findByProfile_IdAndIdIn(profileId, ids)
                .map(permissions -> permissions.stream().map(permissionMapper::toDomain).collect(Collectors.toList()));
    }

    @Override
    public Optional<PermissionDomain> findByProfileIdAndResourceName(Long profileId, String resourceName) {
        return permissionJpaRepository.findByProfile_IdAndResource_Name(profileId, resourceName)
                .map(permissionMapper::toDomain);
    }

    @Override
    public List<PermissionDomain> findByIdIn(Collection<Long> ids) {
        return permissionJpaRepository.findByIdIn(ids).stream().map(permissionMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Page<PermissionDomain> findByIdIn(Pageable pageable, Collection<Long> ids) {
        return permissionJpaRepository.findByIdIn(pageable, ids).map(permissionMapper::toDomain);
    }

    @Override
    public Page<PermissionDomain> findByResourceNameLike(String resourceName, Pageable pageable) {
        return permissionJpaRepository.findByResource_NameLike(resourceName, pageable).map(permissionMapper::toDomain);
    }

    @Override
    public Page<PermissionDomain> findByResourceNameLikeAndIdIn(String resourceName, Pageable pageable, Collection<Long> ids) {
        return permissionJpaRepository.findByResource_NameLikeAndIdIn(resourceName, pageable, ids).map(permissionMapper::toDomain);
    }
}
