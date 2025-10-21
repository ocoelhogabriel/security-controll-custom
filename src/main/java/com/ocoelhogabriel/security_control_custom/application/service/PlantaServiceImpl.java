package com.ocoelhogabriel.security_control_custom.application.service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import com.ocoelhogabriel.security_control_custom.domain.entity.CompanyDomain;
import com.ocoelhogabriel.security_control_custom.domain.entity.PlanDomain;
import com.ocoelhogabriel.security_control_custom.domain.repository.PlanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ocoelhogabriel.security_control_custom.infrastructure.exception.CustomMessageExcep;
import com.ocoelhogabriel.security_control_custom.application.handler.AbrangenciaHandler;
import com.ocoelhogabriel.security_control_custom.application.dto.PlantaModel;
import com.ocoelhogabriel.security_control_custom.application.dto.PlantaDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.CheckAbrangenciaRec;
import com.ocoelhogabriel.security_control_custom.domain.service.IPlantaService;
import com.ocoelhogabriel.security_control_custom.infrastructure.utils.message.MessageResponse;
import jakarta.persistence.EntityNotFoundException;

@Service
public class PlantaServiceImpl implements IPlantaService {

    private static final Logger logger = LoggerFactory.getLogger(PlantaServiceImpl.class);
    private static final String RECURSO = "Planta";

    @Autowired
    private PlanRepository planRepository; // Substituído PlanJpaRepository por PlanRepository

    @Autowired
    private EmpresaServiceImpl empresaServiceImpl;

    @Autowired
    private AbrangenciaHandler abrangenciaHandler;

    private static final String PLANTA = "PLANTA";

    public CheckAbrangenciaRec findAbrangencia() {
        return abrangenciaHandler.checkAbrangencia(PLANTA);
    }

    @Override
    public ResponseEntity<PlantaDTO> save(PlantaModel planta) throws IOException {
        validatePlantaFields(planta);

        CompanyDomain empresa = empresaServiceImpl.findById(planta.getEmpresa());
        if (empresa == null)
            throw new EntityNotFoundException("Empresa não encontrada.");

        PlanDomain planDomain = new PlanDomain(null, planta.getNome(), empresa);
        PlanDomain savedPlan = planRepository.save(planDomain);
        logger.info("Planta salva com sucesso: {}",

                savedPlan);
        return MessageResponse.create(new

                PlantaDTO(savedPlan));
    }

    @Override
    public ResponseEntity<Void> deleteByPlacod(Long codigo) throws IOException {
        try {
            PlanDomain entity = findEntity(codigo);
            if (entity == null) {
                throw new EntityNotFoundException("Planta não encontrada com o código: " + codigo);
            }

            planRepository.deleteById(entity.getId()); // Usando removeById do PlanRepository
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
        if (entity == null)
            throw new EntityNotFoundException("Planta não encontrada.");

        CompanyDomain empresa = empresaServiceImpl.findById(planta.getEmpresa());
        if (empresa == null)
            throw new EntityNotFoundException("Empresa não encontrada.");

        entity.setName(planta.getNome());
        entity.setCompanyDomain(empresa);

        PlanDomain updatedPlan = planRepository.save(entity);
        logger.info("Planta atualizada com sucesso: {}", updatedPlan);

        return MessageResponse.success(new PlantaDTO(updatedPlan));

    }

    @Override
    public List<PlantaDTO> sendListAbrangenciaPlantaDTO() throws IOException {
        return planRepository.findAll().stream().map(PlantaDTO::new).toList();
    }

    @Override
    public ResponseEntity<List<PlantaDTO>> findAllPlantaDTO() throws IOException {
        List<PlanDomain> result;
        if (findAbrangencia().isHier() == 0) {
            result = planRepository.findAll();
        } else {
            List<Long> abrangenciaIds = findAbrangencia().listAbrangencia();
            result = planRepository.findAll().stream().filter(plan -> abrangenciaIds.contains(plan.getId())).toList();
        }
        List<PlantaDTO> plantaDTOs = result.stream().map(this::convertPlantaDTO).toList();
        return MessageResponse.success(plantaDTOs);
    }

    @Override
    public ResponseEntity<Page<PlantaDTO>> plantaFindAllPaginado(String searchTerm, Pageable pageable) throws EntityNotFoundException, IOException {
        Page<PlanDomain> result;
        if (findAbrangencia().isHier() == 0) {
            result = planRepository.findAll(searchTerm, null, pageable);
        } else {
            List<Long> abrangenciaIds = findAbrangencia().listAbrangencia();
            result = planRepository.findAll(searchTerm, abrangenciaIds, pageable);
        }
        return ResponseEntity.ok(result.map(this::convertPlantaDTO));
    }

    @Override
    public ResponseEntity<PlantaDTO> findById(Long codigo) throws IOException, EmptyResultDataAccessException {
        Objects.requireNonNull(codigo, "Código da Planta está nulo.");

        PlanDomain result = findEntity(codigo);
        if (result == null) {
            logger.error("Planta não encontrada.");
            throw new EntityNotFoundException("Planta não encontrada.");
        }

        Long idPermitted = abrangenciaHandler.findIdAbrangenciaPermi(findAbrangencia(), result.getId());
        if (idPermitted == null) {
            throw new EntityNotFoundException("Sem Abrangência para essa planta.");
        }

        return MessageResponse.success(convertPlantaDTO(result));
    }

    private PlantaDTO convertPlantaDTO(PlanDomain planDomain) {
        CompanyDomain companyDomain = empresaServiceImpl.findById(planDomain.getCompanyDomain().getId());
        return new PlantaDTO(planDomain, companyDomain);
    }

    PlanDomain findEntity(Long codigo) {
        Objects.requireNonNull(codigo, "Código está nulo.");
        return planRepository.findById(codigo).orElse(null);
    }

    private void validatePlantaFields(PlantaModel planta) {
        Objects.requireNonNull(planta.getEmpresa(), "Código da Empresa está nulo.");
        Objects.requireNonNull(planta.getNome(), "Nome da planta está nulo.");
    }

    PlantaDTO findPlantaAbrangencia(Long codigo) {

        Long idPermitted = abrangenciaHandler.findIdAbrangenciaPermi(findAbrangencia(), codigo);
        if (idPermitted == null) {
            return null;
        }
        PlanDomain result = findEntity(idPermitted);
        if (result == null) {
            logger.error("Planta não encontrada.");
            return null;
        }
        // empresaServiceImpl.findByIdAbrangencia agora retorna CompanyDomain
        CompanyDomain companyDomain = empresaServiceImpl.findById(result.getCompanyDomain().getId());
        return new PlantaDTO(result, companyDomain);
    }

}
