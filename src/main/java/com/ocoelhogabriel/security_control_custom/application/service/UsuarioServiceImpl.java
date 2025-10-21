package com.ocoelhogabriel.security_control_custom.application.service;

import com.google.gson.Gson;
import com.ocoelhogabriel.security_control_custom.application.dto.UsuarioDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.UsuarioModel;
import com.ocoelhogabriel.security_control_custom.application.dto.UsuarioPermissaoDTO;
import com.ocoelhogabriel.security_control_custom.application.exception.AdminUserModificationException;
import com.ocoelhogabriel.security_control_custom.application.exception.UserNotFoundException;
import com.ocoelhogabriel.security_control_custom.application.port.in.IUsuarioService;
import com.ocoelhogabriel.security_control_custom.domain.entity.*;
import com.ocoelhogabriel.security_control_custom.domain.repository.*;
import com.ocoelhogabriel.security_control_custom.infrastructure.utils.ScopeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ScopeRepository abrangenciaService;

    @Autowired
    private CompanyRepository empresaService;

    @Autowired
    private ScopeDetailsRepository scopeDetailsRepository;

    @Autowired
    private ResourcesRepository resourcesRepository;

    private final Gson gson = new Gson();

    public UserDomain findLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("Usuário não existe!"));
    }

    public UserDomain findLoginInternal(String login) {
        return userRepository.findByLogin(login).orElse(null);
    }

    @Override
    public Page<UsuarioDTO> findAll(String nome, @NonNull Pageable pageable) {
        Page<UserDomain> users = findAllEntity(nome, pageable);
        return users.map(UsuarioDTO::new);
    }

    public Page<UserDomain> findAllEntity(String nome, @NonNull Pageable pageable) {
        Objects.requireNonNull(pageable, "Pageable do Usuário está nulo.");
        Optional<UserDomain> authenticatedUser = getAuthenticatedUserDomain();
        Map<String, Object> scopeFilters = ScopeUtils.getScopeFilters(authenticatedUser, "USUARIO", resourcesRepository, scopeDetailsRepository, gson);
        return userRepository.findAll(pageable, nome, scopeFilters);
    }

    @Override
    public List<UsuarioDTO> findAll() {
        List<UserDomain> users = findAllEntity();
        return users.stream().map(UsuarioDTO::new).toList();
    }

    public List<UserDomain> findAllEntity() {
        Optional<UserDomain> authenticatedUser = getAuthenticatedUserDomain();
        Map<String, Object> scopeFilters = ScopeUtils.getScopeFilters(authenticatedUser, "USUARIO", resourcesRepository, scopeDetailsRepository, gson);
        return userRepository.findAll(scopeFilters);
    }

    @Override
    public UsuarioDTO findById(@NonNull Long codigo) {
        UserDomain user = findByIdEntity(codigo);
        return new UsuarioDTO(user);
    }

    public UserDomain findByIdEntity(Long cod) {
        Optional<UserDomain> authenticatedUser = getAuthenticatedUserDomain();
        Map<String, Object> scopeFilters = ScopeUtils.getScopeFilters(authenticatedUser, "USUARIO", resourcesRepository, scopeDetailsRepository, gson);
        return userRepository.findById(cod, scopeFilters)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com o código: " + cod + " ou fora da sua abrangência."));
    }

    public UserDomain saveUpdateEntity(@NonNull Long codigo, @NonNull UsuarioModel userModel) {
        UserDomain existingUser = findByIdEntity(codigo);

        if ("admin".equalsIgnoreCase(existingUser.getLogin())) {
            log.info("Usuário admin não pode ser alterado: " + existingUser);
            throw new AdminUserModificationException("Usuário admin não pode ser alterado.");
        }

        return updateUserInfo(existingUser, userModel);
    }

    public UserDomain saveUpdateEntity(@NonNull UsuarioModel userModel) {
        Optional<UserDomain> existingUserOpt = userRepository.findByLogin(userModel.getLogin());
        if (existingUserOpt.isPresent() && "admin".equalsIgnoreCase(existingUserOpt.get().getLogin())) {
            UserDomain existingUser = existingUserOpt.get();
            log.info("Usuário com o login admin já existe: " + existingUser);
            throw new AdminUserModificationException("Usuário admin já existe!");
        }

        UserDomain userToSave = existingUserOpt.orElseGet(UserDomain::new);
        return updateUserInfo(userToSave, userModel);
    }

    @Override
    public UsuarioPermissaoDTO findByIdPermission(@NonNull Long codigo) {
        UserDomain user = findByIdEntity(codigo);
        ProfileDomain profile = profileRepository.findById(user.getProfileDomain().getId())
                .orElseThrow(() -> new UserNotFoundException("Perfil não encontrado para o usuário: " + codigo));
        return new UsuarioPermissaoDTO(user, null);
    }

    @Override
    public UsuarioDTO saveUpdateEncodePassword(@NonNull UsuarioModel userModel) {
        return new UsuarioDTO(saveUpdateEntity(userModel));
    }

    @Override
    public UsuarioDTO saveUpdateEncodePassword(@NonNull Long codigo, @NonNull UsuarioModel userModel) {
        return new UsuarioDTO(saveUpdateEntity(codigo, userModel));
    }

    @Override
    public void delete(@NonNull Long codigo) {
        UserDomain userToDelete = userRepository.findById(codigo)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com o código: " + codigo));

        if ("admin".equalsIgnoreCase(userToDelete.getLogin())) {
            log.info("Usuário admin não pode ser apagado: " + userToDelete);
            throw new AdminUserModificationException("Usuário admin não pode ser apagado.");
        }

        try {
            userRepository.deleteById(codigo);
        } catch (Exception e) {
            log.error("Erro ao apagar o item. Mensagem: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao apagar o item. Mensagem: " + e.getMessage(), e); // Considerar exceção mais específica
        }
    }

    private UserDomain updateUserInfo(UserDomain userDomain, UsuarioModel userModel) {
        ProfileDomain profile = profileRepository.findById(userModel.getPerfil())
                .orElseThrow(() -> new UserNotFoundException("Perfil não encontrado com o código: " + userModel.getPerfil()));
        CompanyDomain company = empresaService.findById(userModel.getEmpresa())
                .orElseThrow(() -> new UserNotFoundException(
                        "Empresa não encontrada com o código: " + userModel.getEmpresa()));
        ScopeDomain scope = abrangenciaService.findById(userModel.getAbrangencia())
                .orElseThrow(() -> new UserNotFoundException("Abrangência não encontrada com o código: " + userModel.getAbrangencia()));

        userDomain.setName(userModel.getNome());
        userDomain.setCpf(userModel.getCpf());
        userDomain.setLogin(userModel.getLogin());
        userDomain.setPassword(passwordEncoder.encode(userModel.getSenha()));
        userDomain.setEmail(Optional.ofNullable(userModel.getEmail()).orElse(""));
        userDomain.setProfileDomain(profile);
        userDomain.setScopeDomain(scope);
        userDomain.setCompanyDomain(company);
        return userRepository.save(userDomain);
    }

    private Optional<UserDomain> getAuthenticatedUserDomain() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserDomain)) {
            return Optional.empty();
        }
        return Optional.of((UserDomain) authentication.getPrincipal());
    }
}
