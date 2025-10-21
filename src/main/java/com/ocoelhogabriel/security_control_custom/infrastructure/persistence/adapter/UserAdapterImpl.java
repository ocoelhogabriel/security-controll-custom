package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.adapter;

import com.ocoelhogabriel.security_control_custom.domain.entity.UserDomain;
import com.ocoelhogabriel.security_control_custom.domain.repository.UserRepository;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.User;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.mapper.UserMapper;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository.UserJpaRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class UserAdapterImpl implements UserRepository {

    private final UserJpaRepository usuarioJpaRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserAdapterImpl(UserJpaRepository usuarioJpaRepository, UserMapper userMapper) {
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<UserDomain> findById(Long id) {
        return usuarioJpaRepository.findById(id).map(userMapper::toDomain);
    }

    @Override
    public Optional<UserDomain> findByLogin(String login) {
        return usuarioJpaRepository.findByLogin(login).map(userMapper::toDomain);
    }

    @Override
    public List<UserDomain> findAll() {
        return usuarioJpaRepository.findAll().stream().map(userMapper::toDomain).toList();
    }

    @Override
    public Page<UserDomain> findAll(Pageable pageable) {
        return usuarioJpaRepository.findAll(pageable).map(userMapper::toDomain);
    }

    @Override
    public void delete(UserDomain entity) {
        usuarioJpaRepository.delete(userMapper.toEntity(entity));
    }

    @Override
    public Page<UserDomain> findAll(String filter, Pageable pageable) {
        Specification<User> spec = createSpecification(filter);
        return usuarioJpaRepository.findAll(spec, pageable).map(userMapper::toDomain);
    }

    @Override
    public UserDomain save(UserDomain userDomain) {
        User userEntity = userMapper.toEntity(userDomain);
        User savedEntity = usuarioJpaRepository.save(userEntity);
        return userMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        usuarioJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long aLong) {
        return usuarioJpaRepository.existsById(aLong);
    }

    @Override
    public List<UserDomain> findByIdIn(Collection<Long> ids) {
        return usuarioJpaRepository.findByIdIn(ids).stream().map(userMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Page<UserDomain> findByIdIn(Pageable pageable, Collection<Long> ids) {
        return usuarioJpaRepository.findByIdIn(pageable, ids).map(userMapper::toDomain);
    }

    @Override
    public Page<UserDomain> findByLoginLike(String login, Pageable pageable) {
        return usuarioJpaRepository.findByLoginLike(login, pageable).map(userMapper::toDomain);
    }

    @Override
    public Page<UserDomain> findByLoginLikeAndIdIn(String login, Pageable pageable, Collection<Long> ids) {
        return usuarioJpaRepository.findByLoginLikeAndIdIn(login, pageable, ids).map(userMapper::toDomain);
    }

    /**
     * Lógica de filtragem que antes estava na entidade ou no serviço. Agora é uma responsabilidade interna desta implementação de repositório.
     */
    private Specification<User> createSpecification(String searchTerm) {
        return (root, query, criteriaBuilder) -> {
            if (searchTerm == null || searchTerm.isEmpty()) {
                return criteriaBuilder.conjunction(); // Retorna uma condição que é sempre verdadeira
            }

            List<Predicate> predicates = new ArrayList<>();
            String likePattern = "%" + searchTerm.toLowerCase() + "%";

            filterLikeAndLower("name", root, criteriaBuilder, predicates, likePattern);
            filterLikeAndLower("login", root, criteriaBuilder, predicates, likePattern);
            filterLikeAndLower("email", root, criteriaBuilder, predicates, likePattern);

            try {
                Long searchTermLong = Long.valueOf(searchTerm);
                predicates.add(criteriaBuilder.equal(root.get("id"), searchTermLong));
                predicates.add(criteriaBuilder.equal(root.get("cpf"), searchTermLong));
            } catch (NumberFormatException e) {
                // Ignora se não for um número
            }

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }

    public static void filterLikeAndLower(String searchTerm, Root<User> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates,
            String likePattern) {
        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(searchTerm)), likePattern));

    }


}
