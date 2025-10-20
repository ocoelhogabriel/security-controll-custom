package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository;

import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Resources;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ResourcesJpaRepository extends JpaRepository<Resources, Long> {
    Optional<Resources> findByRecnom(@NonNull String nome);

    List<Resources> findByReccodIn(Collection<Long> list);

    Page<Resources> findByReccodIn(Pageable page, Collection<Long> list);

    Page<Resources> findByRecnomLike(String nome, Pageable page);

    Page<Resources> findByRecnomLikeAndReccodIn(String nome, Pageable page, Collection<Long> list);

}
