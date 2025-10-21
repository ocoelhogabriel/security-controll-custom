package com.ocoelhogabriel.security_control_custom.application.service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ocoelhogabriel.security_control_custom.domain.entity.CompanyDomain;
import com.ocoelhogabriel.security_control_custom.domain.entity.ResourcesDomain;
import com.ocoelhogabriel.security_control_custom.domain.entity.ScopeDetailsDomain;
import com.ocoelhogabriel.security_control_custom.domain.entity.UserDomain;
import com.ocoelhogabriel.security_control_custom.domain.repository.CompanyRepository;
import com.ocoelhogabriel.security_control_custom.domain.repository.ResourcesRepository;
import com.ocoelhogabriel.security_control_custom.domain.repository.ScopeDetailsRepository;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.specification.CompanySpecifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ocoelhogabriel.security_control_custom.application.dto.EmpresaModel;
import com.ocoelhogabriel.security_control_custom.application.dto.EmpresaDTO;
import com.ocoelhogabriel.security_control_custom.domain.service.IEmpresaService;
import com.ocoelhogabriel.security_control_custom.infrastructure.utils.message.MessageResponse;
import jakarta.persistence.EntityNotFoundException;

@Service
public class EmpresaServiceImpl implements IEmpresaService {

    private static final Logger log = LoggerFactory.getLogger(EmpresaServiceImpl.class);

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ScopeDetailsRepository scopeDetailsRepository;

    @Autowired
    private ResourcesRepository resourcesRepository;

    private final Gson gson = new Gson();

    private static final String EMPRESA_RESOURCE_NAME = "EMPRESA";

    @Override
    public ResponseEntity<Void> empresaDeleteById(Long codigo) throws IOException {
        try {
            var empresa = findByIdEntity(codigo);
            if (empresa == null)
                throw new EntityNotFoundException("Empresa não encontrada com o código: " + codigo);

            companyRepository.deleteById(empresa.getId());
            return MessageResponse.noContent();
        } catch (EmptyResultDataAccessException e) {
            log.error("Erro ao deletar a empresa. Erro: ", e);
            throw new EntityNotFoundException("Empresa não encontrada com o código: " + codigo, e);
        }
    }

    @Override
    public ResponseEntity<Page<EmpresaDTO>> empresaFindAllPaginado(String nome, Pageable pageable) throws IOException {
        Objects.requireNonNull(pageable, "Pageable da Empresa está nulo.");

        Optional<UserDomain> authenticatedUser = getAuthenticatedUserDomain();
        Map<String, Object> scopeFilters = getScopeFilters(authenticatedUser, EMPRESA_RESOURCE_NAME);

        Specification<CompanyDomain> finalSpec = CompanySpecifications.withScopeFilters(scopeFilters);

        if (nome != null && !nome.isEmpty()) {
            finalSpec = finalSpec.and(CompanySpecifications.withNameLike(nome));
        }

        Page<CompanyDomain> result = companyRepository.findAll(pageable);
        return MessageResponse.success(result.map(EmpresaDTO::new));
    }

    @Override
    public List<EmpresaDTO> sendListAbrangenciaEmpresaDTO() {
        Optional<UserDomain> authenticatedUser = getAuthenticatedUserDomain();
        Map<String, Object> scopeFilters = getScopeFilters(authenticatedUser, EMPRESA_RESOURCE_NAME);
        Specification<CompanyDomain> finalSpec = CompanySpecifications.withScopeFilters(scopeFilters);
        return companyRepository.findAll().stream().map(EmpresaDTO::new).toList();
    }

    @Override
    public ResponseEntity<List<EmpresaDTO>> empresaFindAll() throws IOException {
        Optional<UserDomain> authenticatedUser = getAuthenticatedUserDomain();
        Map<String, Object> scopeFilters = getScopeFilters(authenticatedUser, EMPRESA_RESOURCE_NAME);
        Specification<CompanyDomain> finalSpec = CompanySpecifications.withScopeFilters(scopeFilters);

        List<CompanyDomain> result = companyRepository.findAll();
        return MessageResponse.success(result.stream().map(EmpresaDTO::new).toList());
    }

    @Override
    public ResponseEntity<EmpresaDTO> empresaUpdate(Long codigo, EmpresaModel empresaModel) throws IOException {
        Objects.requireNonNull(empresaModel.getCnpj(), "CNPJ da Empresa está nulo.");
        Objects.requireNonNull(empresaModel.getNome(), "Nome da Empresa está nulo.");

        CompanyDomain empresa = findByIdEntity(codigo);

        String nomeFantasia = Optional.ofNullable(empresaModel.getNomeFantasia()).orElse(empresa.getTradeName());
        String telefone = Optional.ofNullable(empresaModel.getTelefone()).orElse(empresa.getContact());

        empresa.setDocument(empresaModel.getCnpj());
        empresa.setName(empresaModel.getNome());
        empresa.setTradeName(nomeFantasia);
        empresa.setContact(telefone);

        return MessageResponse.success(new EmpresaDTO(companyRepository.save(empresa)));
    }

    @Override
    public ResponseEntity<EmpresaDTO> empresaSave(EmpresaModel empresaModel) throws IOException {
        Objects.requireNonNull(empresaModel.getCnpj(), "CNPJ da Empresa está nulo.");
        Objects.requireNonNull(empresaModel.getNome(), "Nome da Empresa está nulo.");

        try {
            CompanyDomain companyDomain = new CompanyDomain(null,
                    empresaModel.getCnpj(),
                    empresaModel.getNome(),
                    empresaModel.getNomeFantasia(),
                    empresaModel.getTelefone());
            CompanyDomain savedCompany = companyRepository.save(companyDomain);
            return MessageResponse.create(new EmpresaDTO(savedCompany));
        } catch (Exception e) {
            log.error("Erro ao realizar o cadastro de uma empresa.", e);
            throw new IOException("Erro ao realizar o cadastro de uma empresa.", e);
        }
    }

    @Override
    public ResponseEntity<EmpresaDTO> findByIdApi(Long codigo) throws IOException {
        CompanyDomain empresa = findByIdEntity(codigo);
        if (empresa == null) {
            throw new EntityNotFoundException("Empresa não encontrada ou fora da sua abrangência.");
        }
        return MessageResponse.success(new EmpresaDTO(empresa));
    }

    @Override
    public ResponseEntity<EmpresaDTO> empresaFindByCnpjApi(Long cnpj) throws IOException {
        CompanyDomain company = empresaFindByCnpjEntity(cnpj);
        if (company == null) {
            throw new EntityNotFoundException("Empresa não encontrada ou fora da sua abrangência.");
        }
        return MessageResponse.success(new EmpresaDTO(company));
    }

    public CompanyDomain findById(Long codigo) {
        Objects.requireNonNull(codigo, "Código da Empresa está nulo.");
        return findByIdEntity(codigo);
    }

    public CompanyDomain empresaFindByCnpjEntity(Long cnpj) {
        Objects.requireNonNull(cnpj, "CNPJ da Empresa está nulo.");
        Optional<UserDomain> authenticatedUser = getAuthenticatedUserDomain();
        Map<String, Object> scopeFilters = getScopeFilters(authenticatedUser, EMPRESA_RESOURCE_NAME);

        Specification<CompanyDomain> finalSpec = CompanySpecifications.withScopeFilters(scopeFilters)
                .and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("document"), cnpj));

        return companyRepository.findByDocument(cnpj).orElse(null);
    }

    // Método para uso interno (ex: CreateAdminHandler) que não aplica filtros de abrangência
    public CompanyDomain empresaFindByCnpjEntityInternal(Long cnpj) {
        Objects.requireNonNull(cnpj, "CNPJ da Empresa está nulo.");
        return companyRepository.findByDocument(cnpj).orElse(null);
    }

    public CompanyDomain findByIdEntity(@NonNull Long codigo) {
        Objects.requireNonNull(codigo, "Código da Empresa está nulo.");
        Optional<UserDomain> authenticatedUser = getAuthenticatedUserDomain();
        Map<String, Object> scopeFilters = getScopeFilters(authenticatedUser, EMPRESA_RESOURCE_NAME);

        Specification<CompanyDomain> finalSpec = CompanySpecifications.withScopeFilters(scopeFilters)
                .and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), codigo));

        return companyRepository.findById(codigo).orElse(null);
    }

    // Método para uso interno (ex: CreateAdminHandler) que não aplica filtros de abrangência
    public CompanyDomain findByIdEntityInternal(@NonNull Long codigo) {
        Objects.requireNonNull(codigo, "Código da Empresa está nulo.");
        return companyRepository.findById(codigo).orElse(null);
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
