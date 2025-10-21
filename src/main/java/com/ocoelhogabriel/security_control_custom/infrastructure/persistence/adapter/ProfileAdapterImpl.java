package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.adapter;

import com.ocoelhogabriel.security_control_custom.domain.entity.ProfileDomain;
import com.ocoelhogabriel.security_control_custom.domain.repository.ProfileRepository;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Profile;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.mapper.ProfileMapper;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository.ProfileJpaRepository;
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
public class ProfileAdapterImpl implements ProfileRepository {

    private final ProfileJpaRepository profileJpaRepository;
    private final ProfileMapper profileMapper;

    @Autowired
    public ProfileAdapterImpl(ProfileJpaRepository profileJpaRepository, ProfileMapper profileMapper) {
        this.profileJpaRepository = profileJpaRepository;
        this.profileMapper = profileMapper;
    }

    @Override
    public ProfileDomain save(ProfileDomain entity) {
        Profile profileEntity = profileMapper.toEntity(entity);
        Profile savedEntity = profileJpaRepository.save(profileEntity);
        return profileMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<ProfileDomain> findById(Long id) {
        return profileJpaRepository.findById(id).map(profileMapper::toDomain);
    }

    @Override
    public List<ProfileDomain> findAll() {
        return profileJpaRepository.findAll().stream()
                .map(profileMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProfileDomain> findAll(Pageable pageable) {
        return profileJpaRepository.findAll(pageable).map(profileMapper::toDomain);
    }

    @Override
    public void delete(ProfileDomain entity) {
        profileJpaRepository.delete(profileMapper.toEntity(entity));
    }

    @Override
    public void deleteById(Long id) {
        profileJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return profileJpaRepository.existsById(id);
    }

    @Override
    public List<ProfileDomain> findByIdIn(Collection<Long> ids) {
        return profileJpaRepository.findByIdIn(ids).stream().map(profileMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Page<ProfileDomain> findByIdIn(Pageable pageable, Collection<Long> ids) {
        return profileJpaRepository.findByIdIn(pageable, ids).map(profileMapper::toDomain);
    }

    @Override
    public Page<ProfileDomain> findByNameLike(String name, Pageable pageable) {
        return profileJpaRepository.findByNameLike(name, pageable).map(profileMapper::toDomain);
    }

    @Override
    public Page<ProfileDomain> findByNameLikeAndIdIn(String name, Pageable pageable, Collection<Long> ids) {
        return profileJpaRepository.findByNameLikeAndIdIn(name, pageable, ids).map(profileMapper::toDomain);
    }

    @Override
    public Optional<ProfileDomain> findByName(String name) {
        return profileJpaRepository.findByName(name).map(profileMapper::toDomain);
    }


    public static Specification<Profile> filterByFields(String searchTerm) {
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
