package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.adapter;

import com.ocoelhogabriel.security_control_custom.domain.entity.PlanDomain;
import com.ocoelhogabriel.security_control_custom.domain.repository.PlanRepository;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Plan;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.mapper.PlanMapper;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository.PlanJpaRepository;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.specification.PlanSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class PlanAdapterImpl implements PlanRepository {

    private final PlanJpaRepository planJpaRepository;
    private final PlanMapper planMapper;

    @Autowired
    public PlanAdapterImpl(PlanJpaRepository planJpaRepository, PlanMapper planMapper) {
        this.planJpaRepository = planJpaRepository;
        this.planMapper = planMapper;
    }

    @Override
    public PlanDomain save(PlanDomain entity) {
        Plan planEntity = planMapper.toEntity(entity);
        Plan savedEntity = planJpaRepository.save(planEntity);
        return planMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<PlanDomain> findById(Long id) {
        return planJpaRepository.findById(id).map(planMapper::toDomain);
    }

    @Override
    public List<PlanDomain> findAll() {
        return planJpaRepository.findAll().stream().map(planMapper::toDomain).toList();
    }

    @Override
    public Page<PlanDomain> findAll(Pageable pageable) {
        return planJpaRepository.findAll(pageable).map(planMapper::toDomain);
    }

    @Override
    public void delete(PlanDomain entity) {
        planJpaRepository.delete(planMapper.toEntity(entity));
    }

    @Override
    public void deleteById(Long id) {
        planJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return planJpaRepository.existsById(id);
    }

    @Override
    public Page<PlanDomain> findAll(Pageable pageable, String name, Map<String, Object> scopeFilters) {
        Specification<Plan> spec = Specification.where(PlanSpecifications.withScopeFilters(scopeFilters)).and(PlanSpecifications.withNameLike(name));
        return planJpaRepository.findAll(spec, pageable).map(planMapper::toDomain);
    }

    @Override
    public List<PlanDomain> findAll(Map<String, Object> scopeFilters) {
        Specification<Plan> spec = PlanSpecifications.withScopeFilters(scopeFilters);
        return planJpaRepository.findAll(spec).stream().map(planMapper::toDomain).toList();
    }

    @Override
    public Optional<PlanDomain> findById(Long id, Map<String, Object> scopeFilters) {
        Specification<Plan> spec = Specification.where(PlanSpecifications.withScopeFilters(scopeFilters))
                .and((root, query, cb) -> cb.equal(root.get("id"), id));
        return planJpaRepository.findOne(spec).map(planMapper::toDomain);
    }

    @Override
    public Optional<PlanDomain> findByName(String name) {
        return planJpaRepository.findByName(name).map(planMapper::toDomain);
    }
}
