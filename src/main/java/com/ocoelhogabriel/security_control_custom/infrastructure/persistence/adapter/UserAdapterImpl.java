package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.adapter;

import com.ocoelhogabriel.security_control_custom.domain.entity.UserDomain;
import com.ocoelhogabriel.security_control_custom.domain.repository.UserRepository;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.User;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.mapper.UserMapper;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository.UserJpaRepository;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.specification.UserSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserAdapterImpl implements UserRepository {

    private final UserJpaRepository usuarioJpaRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserAdapterImpl(UserJpaRepository usuarioJpaRepository, UserMapper userMapper) {
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDomain save(UserDomain userDomain) {
        User userEntity = userMapper.toEntity(userDomain);
        User savedEntity = usuarioJpaRepository.save(userEntity);
        return userMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<UserDomain> findById(Long id) {
        return usuarioJpaRepository.findById(id).map(userMapper::toDomain);
    }

    @Override
    public Optional<UserDomain> findByLogin(String login) {
        return usuarioJpaRepository.findByLogin(login).map(userMapper::toDomain);
    }

    @Override
    public List<UserDomain> findAll() {
        return usuarioJpaRepository.findAll().stream().map(userMapper::toDomain).toList();
    }

    @Override
    public Page<UserDomain> findAll(Pageable pageable) {
        return usuarioJpaRepository.findAll(pageable).map(userMapper::toDomain);
    }

    @Override
    public void delete(UserDomain entity) {
        usuarioJpaRepository.delete(userMapper.toEntity(entity));
    }

    @Override
    public void deleteById(Long id) {
        usuarioJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return usuarioJpaRepository.existsById(id);
    }

    @Override
    public Page<UserDomain> findAll(Pageable pageable, String login, Map<String, Object> scopeFilters) {
        Specification<User> spec = Specification.where(UserSpecifications.withScopeFilters(scopeFilters))
                                              .and(UserSpecifications.withLoginFilter(login));
        return usuarioJpaRepository.findAll(spec, pageable).map(userMapper::toDomain);
    }

    @Override
    public List<UserDomain> findAll(Map<String, Object> scopeFilters) {
        Specification<User> spec = UserSpecifications.withScopeFilters(scopeFilters);
        return usuarioJpaRepository.findAll(spec).stream().map(userMapper::toDomain).toList();
    }

    @Override
    public Optional<UserDomain> findById(Long id, Map<String, Object> scopeFilters) {
        Specification<User> spec = Specification.where(UserSpecifications.withScopeFilters(scopeFilters))
                                              .and((root, query, cb) -> cb.equal(root.get("id"), id));
        return usuarioJpaRepository.findOne(spec).map(userMapper::toDomain);
    }
}
