package com.ocoelhogabriel.security_control_custom.application.service;

import com.google.gson.Gson;
import com.ocoelhogabriel.security_control_custom.application.dto.EmpresaDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.EmpresaModel;
import com.ocoelhogabriel.security_control_custom.domain.entity.CompanyDomain;
import com.ocoelhogabriel.security_control_custom.domain.entity.UserDomain;
import com.ocoelhogabriel.security_control_custom.domain.repository.CompanyRepository;
import com.ocoelhogabriel.security_control_custom.domain.repository.ResourcesRepository;
import com.ocoelhogabriel.security_control_custom.domain.repository.ScopeDetailsRepository;
import com.ocoelhogabriel.security_control_custom.domain.service.IEmpresaService;
import com.ocoelhogabriel.security_control_custom.infrastructure.utils.ScopeUtils;
import com.ocoelhogabriel.security_control_custom.infrastructure.utils.message.MessageResponse;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
            CompanyDomain empresa = findByIdEntity(codigo);
            companyRepository.deleteById(empresa.getId());
            return MessageResponse.noContent();
        } catch (EmptyResultDataAccessException e) {
            log.error("Erro ao deletar a empresa. Erro: ", e);
            throw new EntityNotFoundException("Empresa não encontrada com o código: " + codigo, e);
        }
    }

    @Override
    public ResponseEntity<Page<EmpresaDTO>> empresaFindAllPaginado(String nome, Pageable pageable) {
        Objects.requireNonNull(pageable, "Pageable da Empresa está nulo.");
        Optional<UserDomain> authenticatedUser = getAuthenticatedUserDomain();
        Map<String, Object> scopeFilters = ScopeUtils.getScopeFilters(authenticatedUser, EMPRESA_RESOURCE_NAME, resourcesRepository, scopeDetailsRepository, gson);
        Page<CompanyDomain> result = companyRepository.findAll(pageable, nome, scopeFilters);
        return MessageResponse.success(result.map(EmpresaDTO::new));
    }

    @Override
    public List<EmpresaDTO> sendListAbrangenciaEmpresaDTO() {
        Optional<UserDomain> authenticatedUser = getAuthenticatedUserDomain();
        Map<String, Object> scopeFilters = ScopeUtils.getScopeFilters(authenticatedUser, EMPRESA_RESOURCE_NAME, resourcesRepository, scopeDetailsRepository, gson);
        return companyRepository.findAll(scopeFilters).stream().map(EmpresaDTO::new).toList();
    }

    @Override
    public ResponseEntity<List<EmpresaDTO>> empresaFindAll() {
        Optional<UserDomain> authenticatedUser = getAuthenticatedUserDomain();
        Map<String, Object> scopeFilters = ScopeUtils.getScopeFilters(authenticatedUser, EMPRESA_RESOURCE_NAME, resourcesRepository, scopeDetailsRepository, gson);
        List<CompanyDomain> result = companyRepository.findAll(scopeFilters);
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
        return MessageResponse.success(new EmpresaDTO(empresa));
    }

    @Override
    public ResponseEntity<EmpresaDTO> empresaFindByCnpjApi(Long cnpj) throws IOException {
        CompanyDomain company = empresaFindByCnpjEntity(cnpj);
        return MessageResponse.success(new EmpresaDTO(company));
    }

    public CompanyDomain findById(Long codigo) {
        Objects.requireNonNull(codigo, "Código da Empresa está nulo.");
        return findByIdEntity(codigo);
    }

    public CompanyDomain empresaFindByCnpjEntity(Long cnpj) {
        Objects.requireNonNull(cnpj, "CNPJ da Empresa está nulo.");
        Optional<UserDomain> authenticatedUser = getAuthenticatedUserDomain();
        Map<String, Object> scopeFilters = ScopeUtils.getScopeFilters(authenticatedUser, EMPRESA_RESOURCE_NAME, resourcesRepository, scopeDetailsRepository, gson);
        return companyRepository.findByDocument(cnpj, scopeFilters).orElse(null);
    }

    public CompanyDomain findByIdEntity(@NonNull Long codigo) {
        Objects.requireNonNull(codigo, "Código da Empresa está nulo.");
        Optional<UserDomain> authenticatedUser = getAuthenticatedUserDomain();
        Map<String, Object> scopeFilters = ScopeUtils.getScopeFilters(authenticatedUser, EMPRESA_RESOURCE_NAME, resourcesRepository, scopeDetailsRepository, gson);
        return companyRepository.findById(codigo, scopeFilters)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com o código: " + codigo + " ou fora da sua abrangência."));
    }

    public CompanyDomain findByDocumentInternal(Long cnpj) {
        return companyRepository.findByDocument(cnpj).orElse(null);
    }

    private Optional<UserDomain> getAuthenticatedUserDomain() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserDomain)) {
            return Optional.empty();
        }
        return Optional.of((UserDomain) authentication.getPrincipal());
    }
}
