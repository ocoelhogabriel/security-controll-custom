package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository;

import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Resources;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ResourcesJpaRepository extends JpaRepository<Resources, Long>, JpaSpecificationExecutor<Resources> {
    Optional<Resources> findByName(@NonNull String name);

    List<Resources> findByIdIn(Collection<Long> ids);

    Page<Resources> findByIdIn(Pageable page, Collection<Long> ids);

    Page<Resources> findByNameLike(String name, Pageable page);

    Page<Resources> findByNameLikeAndIdIn(String name, Pageable page, Collection<Long> ids);

}
