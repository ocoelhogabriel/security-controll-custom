package com.ocoelhogabriel.security_control_custom.application.service;

import com.ocoelhogabriel.security_control_custom.application.dto.UsuarioDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.UsuarioModel;
import com.ocoelhogabriel.security_control_custom.application.dto.UsuarioPermissaoDTO;
import com.ocoelhogabriel.security_control_custom.application.usecase.IUsuarioService;
import com.ocoelhogabriel.security_control_custom.domain.entity.UserDomain;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.adapter.UserRepositoryImpl;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository.UserJpaRepository;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Company;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Profile;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Scope;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.User;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.mapper.UserMapper;
import com.ocoelhogabriel.security_control_custom.infrastructure.utils.message.MessageResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);
    UserMapper userMapper = new UserMapper();
    @Autowired
    private UserJpaRepository userRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PerfilPermissaoServiceImpl permissaoService;

    @Autowired
    private AbrangenciaServiceImpl abrangenciaService;

    @Autowired
    private EmpresaServiceImpl empresaService;

    public UserDomain findLogin(String login) throws EntityNotFoundException {
        return userRepository.findByUsulog(login)
                .map(u -> userMapper.toDomain(u))
                .orElseThrow(() -> new EntityNotFoundException("Usuário não existe!"));
    }

    public UserDomain findLoginEntity(String login) {
        return userRepository.findByUsulog(login)
                .map(u -> userMapper.toDomain(u))
                .orElseThrow(() -> new EntityNotFoundException("Usuário não existe!"));
    }

    public UserDomain findLoginEntityNull(String login) {
        return userRepository.findByUsulog(login).map(u -> userMapper.toDomain(u)).orElse(null);
    }

    public Page<UserDomain> findAllEntity(String nome, @NonNull Pageable pageable) throws EntityNotFoundException {
        Objects.requireNonNull(pageable, "Pageable do Usuário está nulo.");
        Specification<User> spec = filterByFields(nome);
        return userRepository.findAll(spec, pageable).map(u -> userMapper.toDomain(u));
    }

    public List<UserDomain> findAllEntity() throws EntityNotFoundException {
        return userRepository.findAll().stream().map(u -> userMapper.toDomain(u)).toList();
    }

    public UserDomain findByIdEntity(Long cod) throws EntityNotFoundException {
        return userRepository.findById(cod)
                .map(u -> userMapper.toDomain(u))
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o código: " + cod));
    }

    public UserDomain saveUpdateEntity(@NonNull Long codigo, @NonNull UsuarioModel userModel) throws EntityNotFoundException, IOException {
        User existingUser = userRepository.findById(codigo)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o código: " + codigo));

        if ("admin".equalsIgnoreCase(existingUser.getLogin())) {
            log.info("Usuário admin não pode ser alterado: " + existingUser);
            throw new IOException("Usuário admin não pode ser alterado.");
        }

        return updateUserInfo(existingUser, userModel);
    }

    public UserDomain saveUpdateEntity(@NonNull UsuarioModel userModel) throws EntityNotFoundException, IOException {
        Optional<User> existingUserOpt = userRepository.findByUsulog(userModel.getLogin());
        if (existingUserOpt.isPresent() && "admin".equalsIgnoreCase(existingUserOpt.get().getLogin())) {
            User existingUser = existingUserOpt.get();
            log.info("Usuário com o login admin já existe: " + existingUser);
            throw new IOException("Usuário admin já existe!");
        }

        return updateUserInfo(existingUserOpt.get(), userModel);
    }

    public UsuarioDTO findByUsuario(Long codigo) throws EntityNotFoundException {
        UserDomain user = findByIdEntity(codigo);
        return new UsuarioDTO(user);
    }

    @Override
    public ResponseEntity<Page<UsuarioDTO>> findAll(String nome, @NonNull Pageable pageable) throws EntityNotFoundException, IOException {
        Page<UserDomain> users = findAllEntity(nome, pageable);
        return MessageResponse.success(users.map(UsuarioDTO::new));
    }

    @Override
    public ResponseEntity<List<UsuarioDTO>> findAll() throws EntityNotFoundException, IOException {
        List<UserDomain> users = findAllEntity();
        List<UsuarioDTO> userDTOs = users.stream().map(UsuarioDTO::new).toList();
        return MessageResponse.success(userDTOs);
    }

    @Override
    public ResponseEntity<UsuarioDTO> findById(@NonNull Long codigo) throws EntityNotFoundException, IOException {
        return MessageResponse.success(findByUsuario(codigo));
    }

    @Override
    public ResponseEntity<UsuarioPermissaoDTO> findByIdPermission(@NonNull Long codigo) throws EntityNotFoundException, IOException {
        UserDomain user = findByIdEntity(codigo);
        return MessageResponse.success(new UsuarioPermissaoDTO(user, permissaoService.findByIdPerfil(user.getProfileDomain().getId())));
    }

    @Override
    public ResponseEntity<UsuarioDTO> saveUpdateEncodePassword(@NonNull UsuarioModel userModel) throws EntityNotFoundException, IOException {
        UserDomain user = saveUpdateEntity(userModel);
        return MessageResponse.create(new UsuarioDTO(user));
    }

    @Override
    public ResponseEntity<UsuarioDTO> saveUpdateEncodePassword(@NonNull Long codigo, @NonNull UsuarioModel userModel)
            throws EntityNotFoundException, IOException {
        UserDomain user = saveUpdateEntity(codigo, userModel);
        return MessageResponse.success(new UsuarioDTO(user));
    }

    @Override
    public ResponseEntity<Void> delete(@NonNull Long codigo) throws IOException {
        try {
            userRepository.deleteById(codigo);
            return MessageResponse.noContent();
        } catch (Exception e) {
            log.error("Erro ao apagar o item. Mensagem: " + e.getMessage(), e);
            throw new IOException("Erro ao apagar o item. Mensagem: " + e.getMessage(), e);
        }
    }

    private UserDomain updateUserInfo(User user, UsuarioModel userModel) throws EntityNotFoundException {
        Profile profile = permissaoService.findByIdPerfilEntity(userModel.getPerfil());
        Company company = empresaService.findByIdEntity(userModel.getEmpresa());
        Scope scope = abrangenciaService.findByIdEntity(userModel.getAbrangencia());

        user.setName(userModel.getNome());
        user.setCpf(userModel.getCpf());
        user.setLogin(userModel.getLogin());
        user.setPassword(passwordEncoder.encode(userModel.getSenha()));
        user.setEmail(Optional.ofNullable(userModel.getEmail()).orElse(""));
        user.setPerfil(profile);
        user.setAbrangencia(scope);
        user.setEmpresa(company);
        return userMapper.toDomain(userRepository.save(user));
    }

    public static Specification<User> filterByFields(String searchTerm) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchTerm != null && !searchTerm.isEmpty()) {
                String likePattern = "%" + searchTerm.toLowerCase() + "%";

                List<Predicate> searchPredicates = new ArrayList<>();

                // Add predicates for string fields
                searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern));
                searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("login")), likePattern));
                searchPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("password")), likePattern));
                UserRepositoryImpl.filterLikeAndLower(searchTerm, root, criteriaBuilder, searchPredicates, likePattern);

                predicates.add(criteriaBuilder.or(searchPredicates.toArray(Predicate[]::new)));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

}
