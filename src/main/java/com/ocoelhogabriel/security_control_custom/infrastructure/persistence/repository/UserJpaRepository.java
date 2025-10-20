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
	List<User> findByUsucodIn(Collection<Long> list);

	Page<User> findByUsucodIn(Pageable pageable, Collection<Long> list);

	Page<User> findByUsulogLike(String usulog, Pageable pageable);

	Page<User> findByUsulogLikeAndUsucodIn(String usulog, Pageable pageable, Collection<Long> list);

	Optional<User> findByUsulog(String usulog);
}
