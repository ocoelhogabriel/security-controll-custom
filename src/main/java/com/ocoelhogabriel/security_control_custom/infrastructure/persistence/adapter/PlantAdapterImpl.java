package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.adapter;

import com.ocoelhogabriel.security_control_custom.domain.entity.PlanDomain;
import com.ocoelhogabriel.security_control_custom.domain.repository.PlanRepository;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Plan;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.mapper.PlanMapper;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository.PlanJpaRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PlantAdapterImpl implements PlanRepository {

    private final PlanJpaRepository planJpaRepository;
    private final PlanMapper planMapper;

    @Autowired
    public PlantAdapterImpl(PlanJpaRepository planJpaRepository, PlanMapper planMapper) {
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
        return planJpaRepository.findAll().stream().map(planMapper::toDomain).collect(Collectors.toList());
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
    public Page<PlanDomain> findAll(String spec, List<Long> listId, Pageable pageable) {
        Specification<Plan> ent = filterByFields(spec, listId);
        return planJpaRepository.findAll(ent, pageable).map(planMapper::toDomain);
    }

    public Specification<Plan> filterByFields(String searchTerm, List<Long> listId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtragem por lista de IDs da company
            if (listId != null && !listId.isEmpty()) {
                predicates.add(root.get("id_company").in(listId));
            }

            // Filtragem por termo de busca
            if (searchTerm != null && !searchTerm.isEmpty()) {
                String likePattern = "%" + searchTerm.toLowerCase() + "%";

                List<Predicate> searchPredicates = new ArrayList<>();

                // Adiciona predicado para o campo `name`
                searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern));

                // Tenta converter o termo de busca para Long
                try {
                    Long searchTermLong = Long.valueOf(searchTerm);
                    searchPredicates.add(criteriaBuilder.equal(root.get("id"), searchTermLong));
                } catch (NumberFormatException e) {
                    // Ignora se a conversão falhar
                }

                predicates.add(criteriaBuilder.or(searchPredicates.toArray(Predicate[]::new)));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

}
