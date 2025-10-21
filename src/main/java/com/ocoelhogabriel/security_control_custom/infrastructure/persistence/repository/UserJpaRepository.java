package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository;

import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

	Optional<User> findByLogin(String login);

}
