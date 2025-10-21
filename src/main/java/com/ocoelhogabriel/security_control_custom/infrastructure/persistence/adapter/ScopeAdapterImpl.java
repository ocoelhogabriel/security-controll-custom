package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.adapter;

import com.ocoelhogabriel.security_control_custom.domain.entity.ScopeDomain;
import com.ocoelhogabriel.security_control_custom.domain.repository.ScopeRepository;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Scope;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.mapper.ScopeMapper;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository.ScopeJpaRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ScopeAdapterImpl implements ScopeRepository {

    private final ScopeJpaRepository scopeJpaRepository;
    private final ScopeMapper scopeMapper;

    @Autowired
    public ScopeAdapterImpl(ScopeJpaRepository scopeJpaRepository, ScopeMapper scopeMapper) {
        this.scopeJpaRepository = scopeJpaRepository;
        this.scopeMapper = scopeMapper;
    }

    @Override
    public ScopeDomain save(ScopeDomain entity) {
        Scope scopeEntity = scopeMapper.toEntity(entity);
        Scope savedEntity = scopeJpaRepository.save(scopeEntity);
        return scopeMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<ScopeDomain> findById(Long id) {
        return scopeJpaRepository.findById(id).map(scopeMapper::toDomain);
    }

    @Override
    public List<ScopeDomain> findAll() {
        return scopeJpaRepository.findAll().stream()
                .map(scopeMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ScopeDomain> findAll(Pageable pageable) {
        return scopeJpaRepository.findAll(pageable).map(scopeMapper::toDomain);
    }

    @Override
    public void delete(ScopeDomain entity) {
        scopeJpaRepository.delete(scopeMapper.toEntity(entity));
    }

    @Override
    public void deleteById(Long id) {
        scopeJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return scopeJpaRepository.existsById(id);
    }

    @Override
    public Page<ScopeDomain> findByIdIn(Pageable pageable, Collection<Long> ids) {
        return scopeJpaRepository.findByIdIn(pageable, ids).map(scopeMapper::toDomain);
    }

    @Override
    public Page<ScopeDomain> findByNameLike(String name, Pageable pageable) {
        return scopeJpaRepository.findByNameLike(name, pageable).map(scopeMapper::toDomain);
    }

    @Override
    public Page<ScopeDomain> findByNameLikeAndIdIn(String name, Pageable pageable, Collection<Long> ids) {
        return scopeJpaRepository.findByNameLikeAndIdIn(name, pageable, ids).map(scopeMapper::toDomain);
    }

    @Override
    public List<ScopeDomain> findByIdIn(Collection<Long> ids) {
        return scopeJpaRepository.findByIdIn(ids).stream().map(scopeMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<ScopeDomain> findByNameLike(String name) {
        return scopeJpaRepository.findByName(name).map(scopeMapper::toDomain);
    }

    public static Specification<Scope> filterByFields(String searchTerm) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchTerm != null && !searchTerm.isEmpty()) {
                String likePattern = "%" + searchTerm.toLowerCase() + "%";

                List<Predicate> searchPredicates = new ArrayList<>();

                // Add predicates for string fields
                searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern));
                searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern));

                // Attempt to convert the search term to Long
                try {
                    Long searchTermLong = Long.valueOf(searchTerm);
                    searchPredicates.add(criteriaBuilder.equal(root.get("id"), searchTermLong));
                } catch (NumberFormatException e) {
                    // Ignore if the conversion fails
                }

                predicates.add(criteriaBuilder.or(searchPredicates.toArray(Predicate[]::new)));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

}
