package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.adapter;

import com.ocoelhogabriel.security_control_custom.application.dto.CheckAbrangenciaRec;
import com.ocoelhogabriel.security_control_custom.application.dto.PlantaDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.PlantaModel;
import com.ocoelhogabriel.security_control_custom.application.handler.AbrangenciaHandler;
import com.ocoelhogabriel.security_control_custom.application.usecase.IPlantaService;
import com.ocoelhogabriel.security_control_custom.infrastructure.exception.CustomMessageExcep;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Plan;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository.PlanJpaRepository;
import com.ocoelhogabriel.security_control_custom.infrastructure.utils.message.MessageResponse;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class PlantaServiceImpl implements IPlantaService {

	private static final Logger logger = LoggerFactory.getLogger(PlantaServiceImpl.class);
	private static final String RECURSO = "Planta";

	@Autowired
	private PlanJpaRepository planJpaRepository;

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

		var emp = empresaServiceImpl.findById(planta.getEmpresa());
		if (emp == null)
			throw new EntityNotFoundException("Empresa não encontrada.");
		var entity = new Plan();
		entity.plantaUpdateOrSave(planta.getNome(), emp);
		Plan savedPlan = planJpaRepository.save(entity);
		logger.info("Planta salva com sucesso: " + savedPlan);
		return MessageResponse.create(new PlantaDTO(savedPlan));
	}

	@Override
	public ResponseEntity<Void> deleteByPlacod(Long codigo) throws IOException {
		try {
			var entity = findEntity(codigo);
			if (entity == null) {
				throw new EntityNotFoundException("Planta não encontrada com o código: " + codigo);
			}

			planJpaRepository.deleteById(entity.getId());
			return MessageResponse.noContent();
		} catch (EmptyResultDataAccessException e) {
			logger.error("Não foi possível encontrar a Planta com o ID fornecido. Error: ", e);
			throw CustomMessageExcep.exceptionEntityNotFoundException(codigo, RECURSO, e);
		}
	}

	@Override
	public ResponseEntity<PlantaDTO> update(Long codigo, PlantaModel planta) throws IOException {
		Objects.requireNonNull(codigo, "Código da Planta está nulo.");
		validatePlantaFields(planta);

		var entity = findEntity(codigo);
		if (entity == null)
			throw new EntityNotFoundException("Planta não encontrada.");
		var emp = empresaServiceImpl.findById(planta.getEmpresa());
		if (emp == null)
			throw new EntityNotFoundException("Empresa não encontrada.");
		entity.plantaUpdateOrSave(planta.getNome(), emp);

		Plan updatedPlan = planJpaRepository.save(entity);
		logger.info("Planta atualizada com sucesso: " + updatedPlan);

		return MessageResponse.success(new PlantaDTO(updatedPlan));

	}

	@Override
	public List<PlantaDTO> sendListAbrangenciaPlantaDTO() throws IOException {
		return planJpaRepository.findAll().stream().map(PlantaDTO::new).toList();
	}

	@Override
	public ResponseEntity<List<PlantaDTO>> findAllPlantaDTO() throws IOException {

		Specification<Plan> spec = Specification.where(null);

		if (findAbrangencia().isHier() == 0) {
			spec = spec.and(Plan.filterByFields(null, null));
		} else {
			spec = spec.and(Plan.filterByFields(null, findAbrangencia().listAbrangencia()));
		}
		List<PlantaDTO> plantaDTOs = planJpaRepository.findAll(spec).stream().map(this::convertPlantaDTO).toList();
		return MessageResponse.success(plantaDTOs);
	}

	@Override
	public ResponseEntity<Page<PlantaDTO>> plantaFindAllPaginado(String searchTerm, Pageable pageable) throws EntityNotFoundException, IOException {

		Specification<Plan> spec = Specification.where(null);

		if (findAbrangencia().isHier() == 0) {
			spec = spec.and(Plan.filterByFields(searchTerm, null));
		} else {
			spec = spec.and(Plan.filterByFields(searchTerm, findAbrangencia().listAbrangencia()));
		}
		Page<Plan> result = planJpaRepository.findAll(spec, pageable);
		return ResponseEntity.ok(result.map(this::convertPlantaDTO));
	}

	@Override
	public ResponseEntity<PlantaDTO> findById(Long codigo) throws IOException, EmptyResultDataAccessException {
		Objects.requireNonNull(codigo, "Código da Planta está nulo.");


		Plan result = findEntity(codigo);
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

	private PlantaDTO convertPlantaDTO(Plan planEntity) {
		return new PlantaDTO(planEntity, empresaServiceImpl.findByIdAbrangencia(planEntity.getEmpresa()));
	}

	Plan findEntity(Long codigo) {
		Objects.requireNonNull(codigo, "Código está nulo.");
		return planJpaRepository.findById(codigo).orElse(null);
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
		Plan result = findEntity(idPermitted);
		if (result == null) {
			logger.error("Planta não encontrada.");
			return null;
		}
		return new PlantaDTO(result, empresaServiceImpl.findByIdAbrangencia(result.getEmpresa()));
	}

}
