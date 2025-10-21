package com.ocoelhogabriel.security_control_custom.application.service;

import com.ocoelhogabriel.security_control_custom.application.dto.UsuarioDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.UsuarioModel;
import com.ocoelhogabriel.security_control_custom.application.dto.UsuarioPermissaoDTO;
import com.ocoelhogabriel.security_control_custom.domain.service.IUsuarioService;
import com.ocoelhogabriel.security_control_custom.domain.entity.CompanyDomain;
import com.ocoelhogabriel.security_control_custom.domain.entity.ProfileDomain;
import com.ocoelhogabriel.security_control_custom.domain.entity.ScopeDomain;
import com.ocoelhogabriel.security_control_custom.domain.entity.ScopeDetailsDomain;
import com.ocoelhogabriel.security_control_custom.domain.entity.UserDomain;
import com.ocoelhogabriel.security_control_custom.domain.entity.ResourcesDomain;
import com.ocoelhogabriel.security_control_custom.domain.repository.*;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.specification.UserSpecifications;
import com.ocoelhogabriel.security_control_custom.infrastructure.utils.message.MessageResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
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
    private PermissionRepository permissaoService;

    @Autowired
    private ScopeRepository abrangenciaService;

    @Autowired
    private CompanyRepository empresaService;

    @Autowired
    private ScopeDetailsRepository scopeDetailsRepository;

    @Autowired
    private ResourcesRepository resourcesRepository;

    private final Gson gson = new Gson(); // Para parsing de JSON

    public UserDomain findLogin(String login) throws EntityNotFoundException {
        return userRepository.findByLogin(login) // Usando findByLogin do UserRepository
                .orElseThrow(() -> new EntityNotFoundException("Usuário não existe!"));
    }

    public UserDomain findLoginEntity(String login) {
        return userRepository.findByLogin(login) // Usando findByLogin do UserRepository
                .orElseThrow(() -> new EntityNotFoundException("Usuário não existe!"));
    }

    // Método para uso interno (ex: CreateAdminHandler) que não aplica filtros de abrangência
    public UserDomain findLoginEntityInternal(String login) {
        return userRepository.findByLogin(login).orElse(null);
    }

    public UserDomain findLoginEntityNull(String login) {
        return userRepository.findByLogin(login).orElse(null); // Usando findByLogin do UserRepository
    }

    @Override
    public ResponseEntity<Page<UsuarioDTO>> findAll(String nome, @NonNull Pageable pageable) throws EntityNotFoundException, IOException {
        Page<UserDomain> users = findAllEntity(nome, pageable);
        return MessageResponse.success(users.map(UsuarioDTO::new));
    }

    public Page<UserDomain> findAllEntity(String nome, @NonNull Pageable pageable) throws EntityNotFoundException {
        Objects.requireNonNull(pageable, "Pageable do Usuário está nulo.");

        Optional<UserDomain> authenticatedUser = getAuthenticatedUserDomain();
        Map<String, Object> scopeFilters = getScopeFilters(authenticatedUser, "USUARIO");

        Specification<UserDomain> finalSpec = UserSpecifications.withScopeFilters(scopeFilters);

        if (nome != null && !nome.isEmpty()) {
            finalSpec = finalSpec.and(UserSpecifications.withLoginFilter(nome));
        }

        return userRepository.findAll(pageable);
    }

    @Override
    public ResponseEntity<List<UsuarioDTO>> findAll() throws EntityNotFoundException, IOException {
        List<UserDomain> users = findAllEntity();
        List<UsuarioDTO> userDTOs = users.stream().map(UsuarioDTO::new).toList();
        return MessageResponse.success(userDTOs);
    }

    public List<UserDomain> findAllEntity() throws EntityNotFoundException {
        Optional<UserDomain> authenticatedUser = getAuthenticatedUserDomain();
        Map<String, Object> scopeFilters = getScopeFilters(authenticatedUser, "USUARIO");

        Specification<UserDomain> finalSpec = UserSpecifications.withScopeFilters(scopeFilters);

        return userRepository.findAll();
    }

    @Override
    public ResponseEntity<UsuarioDTO> findById(@NonNull Long codigo) throws EntityNotFoundException, IOException {
        return MessageResponse.success(findByUsuario(codigo));
    }

    public UserDomain findByIdEntity(Long cod) throws EntityNotFoundException {
        Optional<UserDomain> authenticatedUser = getAuthenticatedUserDomain();
        Map<String, Object> scopeFilters = getScopeFilters(authenticatedUser, "USUARIO");

        Specification<UserDomain> finalSpec = UserSpecifications.withScopeFilters(scopeFilters)
                .and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), cod));

        return userRepository.findById(cod)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o código: " + cod + " ou fora da sua abrangência."));
    }

    // Método para uso interno (ex: CreateAdminHandler) que não aplica filtros de abrangência
    public UserDomain findByIdEntityInternal(Long cod) {
        return userRepository.findById(cod).orElse(null);
    }

    public UserDomain saveUpdateEntity(@NonNull Long codigo, @NonNull UsuarioModel userModel) throws EntityNotFoundException, IOException {
        UserDomain existingUser = findByIdEntity(codigo);

        if ("admin".equalsIgnoreCase(existingUser.getLogin())) {
            log.info("Usuário admin não pode ser alterado: " + existingUser);
            throw new IOException("Usuário admin não pode ser alterado.");
        }

        return updateUserInfo(existingUser, userModel);
    }

    public UserDomain saveUpdateEntity(@NonNull UsuarioModel userModel) throws EntityNotFoundException, IOException {
        Optional<UserDomain> existingUserOpt = userRepository.findByLogin(userModel.getLogin());
        if (existingUserOpt.isPresent() && "admin".equalsIgnoreCase(existingUserOpt.get().getLogin())) {
            UserDomain existingUser = existingUserOpt.get();
            log.info("Usuário com o login admin já existe: " + existingUser);
            throw new IOException("Usuário admin já existe!");
        }

        // Se o usuário não existe, cria um novo UserDomain
        UserDomain userToSave = existingUserOpt.orElseGet(UserDomain::new);
        return updateUserInfo(userToSave, userModel);
    }

    public UsuarioDTO findByUsuario(Long codigo) throws EntityNotFoundException {
        UserDomain user = findByIdEntity(codigo);
        return new UsuarioDTO(user);
    }

    @Override
    public ResponseEntity<UsuarioPermissaoDTO> findByIdPermission(@NonNull Long codigo) throws EntityNotFoundException, IOException {
        UserDomain user = findByIdEntity(codigo);
        // Corrigido para buscar o perfil corretamente
        ProfileDomain profile = profileRepository.findById(user.getProfileDomain().getId())
                .orElseThrow(() -> new EntityNotFoundException("Perfil não encontrado para o usuário: " + codigo));
        return MessageResponse.success(new UsuarioPermissaoDTO(user, null));
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

    private UserDomain updateUserInfo(UserDomain userDomain, UsuarioModel userModel) throws EntityNotFoundException {
        ProfileDomain profile = profileRepository.findById(userModel.getPerfil())
                .orElseThrow(() -> new EntityNotFoundException("Perfil não encontrado com o código: " + userModel.getPerfil()));
        CompanyDomain company = empresaService.findById(userModel.getEmpresa())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Empresa não encontrada com o código: " + userModel.getEmpresa())); // Usando método interno
        ScopeDomain scope = abrangenciaService.findById(userModel.getAbrangencia())
                .orElseThrow(() -> new EntityNotFoundException("Abrangência não encontrada com o código: " + userModel.getAbrangencia()));

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

    /**
     * Retorna o Optional<UserDomain> do usuário autenticado.
     *
     * @return Optional<UserDomain> do usuário autenticado, ou Optional.empty() se não houver usuário autenticado ou for inválido.
     */
    private Optional<UserDomain> getAuthenticatedUserDomain() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserDomain)) {
            return Optional.empty();
        }
        return Optional.of((UserDomain) authentication.getPrincipal());
    }

    /**
     * Extrai os filtros de abrangência para um dado recurso e usuário.
     *
     * @param authenticatedUser Optional<UserDomain> do usuário autenticado.
     * @param resourceName      O nome do recurso (ex: "USUARIO", "EMPRESA").
     * @return Um mapa de filtros (ex: {"companyId": 1, "plantId": [1, 2]}).
     */
    private Map<String, Object> getScopeFilters(Optional<UserDomain> authenticatedUser, String resourceName) {
        if (authenticatedUser.isEmpty() || authenticatedUser.get().getScopeDomain() == null || resourceName == null) {
            return Collections.emptyMap();
        }

        UserDomain user = authenticatedUser.get();

        ResourcesDomain resource = resourcesRepository.findByName(resourceName)
                .orElseThrow(() -> new EntityNotFoundException("Recurso não encontrado: " + resourceName));

        Optional<ScopeDetailsDomain> scopeDetailsOptional = scopeDetailsRepository.findByScopeIdAndResourceId(user.getScopeDomain().getId(),
                resource.getId());

        if (scopeDetailsOptional.isEmpty()) {
            return Collections.emptyMap();
        }

        ScopeDetailsDomain scopeDetails = scopeDetailsOptional.get();
        String abddatJson = scopeDetails.getData();

        if (abddatJson == null || abddatJson.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, Object> filters = new HashMap<>();
        try {
            Type type = new TypeToken<Map<String, Object>>() {
            }.getType();
            filters = gson.fromJson(abddatJson, type);
        } catch (Exception e) {
            log.error("Erro ao parsear abddat JSON para filtros de abrangência: " + abddatJson, e);
            return Collections.emptyMap();
        }

        return filters;
    }
}
