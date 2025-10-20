package com.ocoelhogabriel.security_control_custom.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface DomainRepository<T, ID> {

    /**
     * Saves an entity.
     *
     * @param entity the entity to save
     * @return the saved entity
     */
    T save(T entity);

    /**
     * Finds an entity by its identifier.
     *
     * @param id the identifier of the entity to find
     * @return an Optional containing the found entity, or empty if not found
     */
    Optional<T> findById(ID id);

    /**
     * Finds all entities.
     *
     * @return a list of all entities
     */
    List<T> findAll();

    /**
     * Finds all entities.
     *
     * @return a list of all entities
     */
    Page<T> findAll(Pageable pageable);

    /**
     * Deletes an entity.
     *
     * @param entity the entity to delete
     */
    void delete(T entity);

    /**
     * Deletes an entity by its identifier.
     *
     * @param id the identifier of the entity to delete
     */
    void deleteById(ID id);

    /**
     * Checks if an entity exists.
     *
     * @param id the identifier of the entity to check
     * @return true if the entity exists, false otherwise
     */
    boolean existsById(ID id);
}
