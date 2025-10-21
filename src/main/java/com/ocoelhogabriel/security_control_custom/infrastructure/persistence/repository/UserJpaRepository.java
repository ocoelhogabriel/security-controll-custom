package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserJpaRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
	List<User> findByIdIn(Collection<Long> list);

	Page<User> findByIdIn(Pageable pageable, Collection<Long> list);

	Page<User> findByLoginLike(String login, Pageable pageable);

	Page<User> findByLoginLikeAndIdIn(String login, Pageable pageable, Collection<Long> list);

	Optional<User> findByLogin(String login);
}
