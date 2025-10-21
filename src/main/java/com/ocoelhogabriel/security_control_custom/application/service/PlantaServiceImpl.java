package com.ocoelhogabriel.security_control_custom.application.service;

import com.google.gson.Gson;
import com.ocoelhogabriel.security_control_custom.application.dto.PlantaDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.PlantaModel;
import com.ocoelhogabriel.security_control_custom.domain.entity.CompanyDomain;
import com.ocoelhogabriel.security_control_custom.domain.entity.PlanDomain;
import com.ocoelhogabriel.security_control_custom.domain.entity.UserDomain;
import com.ocoelhogabriel.security_control_custom.domain.repository.PlanRepository;
import com.ocoelhogabriel.security_control_custom.domain.repository.ResourcesRepository;
import com.ocoelhogabriel.security_control_custom.domain.repository.ScopeDetailsRepository;
import com.ocoelhogabriel.security_control_custom.domain.service.IPlantaService;
import com.ocoelhogabriel.security_control_custom.infrastructure.exception.CustomMessageExcep;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class PlantaServiceImpl implements IPlantaService {

    private static final Logger logger = LoggerFactory.getLogger(PlantaServiceImpl.class);
    private static final String RECURSO = "Planta";
    private static final String PLANTA_RESOURCE_NAME = "PLANTA";

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private EmpresaServiceImpl empresaServiceImpl;

    @Autowired
    private ScopeDetailsRepository scopeDetailsRepository;

    @Autowired
    private ResourcesRepository resourcesRepository;

    private final Gson gson = new Gson();

    @Override
    public ResponseEntity<PlantaDTO> save(PlantaModel planta) throws IOException {
        validatePlantaFields(planta);

        CompanyDomain empresa = empresaServiceImpl.findById(planta.getEmpresa());

        PlanDomain planDomain = new PlanDomain(null, planta.getNome(), empresa);
        PlanDomain savedPlan = planRepository.save(planDomain);
        logger.info("Planta salva com sucesso: {}", savedPlan);
        return MessageResponse.create(new PlantaDTO(savedPlan));
    }

    @Override
    public ResponseEntity<Void> deleteByPlacod(Long codigo) throws IOException {
        try {
            PlanDomain entity = findEntity(codigo);
            planRepository.deleteById(entity.getId());
            return MessageResponse.noContent();
        } catch (Exception e) {
            logger.error("Não foi possível encontrar a Planta com o ID fornecido. Error: ", e);
            throw CustomMessageExcep.exceptionEntityNotFoundException(codigo, RECURSO, e);
        }
    }

    @Override
    public ResponseEntity<PlantaDTO> update(Long codigo, PlantaModel planta) throws IOException {
        Objects.requireNonNull(codigo, "Código da Planta está nulo.");
        validatePlantaFields(planta);

        PlanDomain entity = findEntity(codigo);
        CompanyDomain empresa = empresaServiceImpl.findById(planta.getEmpresa());

        entity.setName(planta.getNome());
        entity.setCompanyDomain(empresa);

        PlanDomain updatedPlan = planRepository.save(entity);
        logger.info("Planta atualizada com sucesso: {}", updatedPlan);

        return MessageResponse.success(new PlantaDTO(updatedPlan));
    }

    @Override
    public List<PlantaDTO> sendListAbrangenciaPlantaDTO() {
        Optional<UserDomain> authenticatedUser = getAuthenticatedUserDomain();
        Map<String, Object> scopeFilters = ScopeUtils.getScopeFilters(authenticatedUser, PLANTA_RESOURCE_NAME, resourcesRepository, scopeDetailsRepository, gson);
        return planRepository.findAll(scopeFilters).stream().map(this::convertPlantaDTO).toList();
    }

    @Override
    public ResponseEntity<List<PlantaDTO>> findAllPlantaDTO() {
        Optional<UserDomain> authenticatedUser = getAuthenticatedUserDomain();
        Map<String, Object> scopeFilters = ScopeUtils.getScopeFilters(authenticatedUser, PLANTA_RESOURCE_NAME, resourcesRepository, scopeDetailsRepository, gson);
        List<PlanDomain> result = planRepository.findAll(scopeFilters);
        List<PlantaDTO> plantaDTOs = result.stream().map(this::convertPlantaDTO).toList();
        return MessageResponse.success(plantaDTOs);
    }

    @Override
    public ResponseEntity<Page<PlantaDTO>> plantaFindAllPaginado(String searchTerm, Pageable pageable) {
        Optional<UserDomain> authenticatedUser = getAuthenticatedUserDomain();
        Map<String, Object> scopeFilters = ScopeUtils.getScopeFilters(authenticatedUser, PLANTA_RESOURCE_NAME, resourcesRepository, scopeDetailsRepository, gson);
        Page<PlanDomain> result = planRepository.findAll(pageable, searchTerm, scopeFilters);
        return ResponseEntity.ok(result.map(this::convertPlantaDTO));
    }

    @Override
    public ResponseEntity<PlantaDTO> findById(Long codigo) throws EmptyResultDataAccessException {
        Objects.requireNonNull(codigo, "Código da Planta está nulo.");
        PlanDomain result = findEntity(codigo);
        return MessageResponse.success(convertPlantaDTO(result));
    }

    private PlantaDTO convertPlantaDTO(PlanDomain planDomain) {
        CompanyDomain companyDomain = empresaServiceImpl.findById(planDomain.getCompanyDomain().getId());
        return new PlantaDTO(planDomain, companyDomain);
    }

    PlanDomain findEntity(Long codigo) {
        Objects.requireNonNull(codigo, "Código está nulo.");
        Optional<UserDomain> authenticatedUser = getAuthenticatedUserDomain();
        Map<String, Object> scopeFilters = ScopeUtils.getScopeFilters(authenticatedUser, PLANTA_RESOURCE_NAME, resourcesRepository, scopeDetailsRepository, gson);

        return planRepository.findById(codigo, scopeFilters)
                .orElseThrow(() -> new EntityNotFoundException("Planta não encontrada com o código: " + codigo + " ou fora da sua abrangência."));
    }

    public PlanDomain findByName(String name) {
        return planRepository.findByName(name).orElse(null);
    }

    private void validatePlantaFields(PlantaModel planta) {
        Objects.requireNonNull(planta.getEmpresa(), "Código da Empresa está nulo.");
        Objects.requireNonNull(planta.getNome(), "Nome da planta está nulo.");
    }

    private Optional<UserDomain> getAuthenticatedUserDomain() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserDomain)) {
            return Optional.empty();
        }
        return Optional.of((UserDomain) authentication.getPrincipal());
    }
}
