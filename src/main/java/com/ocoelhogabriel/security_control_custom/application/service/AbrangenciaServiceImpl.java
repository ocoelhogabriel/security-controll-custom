package com.ocoelhogabriel.security_control_custom.application.service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.ocoelhogabriel.security_control_custom.application.dto.AbrangenciaDetalhesModel;
import com.ocoelhogabriel.security_control_custom.application.dto.AbrangenciaModel;
import com.ocoelhogabriel.security_control_custom.application.dto.AbrangenciaDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.AbrangenciaDetalhesDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.AbrangenciaListaDetalhesDTO;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Scope;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.ScopeDetails;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Resources;
import com.ocoelhogabriel.security_control_custom.application.dto.ItensAbrangentes;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository.ScopeDetailsJpaRepository;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository.ScopeJpaRepository;
import com.ocoelhogabriel.security_control_custom.application.usecase.IAbrangenciaService;
import com.ocoelhogabriel.security_control_custom.application.usecase.IEmpresaService;
import com.ocoelhogabriel.security_control_custom.application.usecase.IPlantaService;
import com.ocoelhogabriel.security_control_custom.infrastructure.utils.JsonNodeConverter;
import com.ocoelhogabriel.security_control_custom.infrastructure.utils.message.MessageResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NonUniqueResultException;

@Service
public class AbrangenciaServiceImpl implements IAbrangenciaService {

	private static final Logger log = LoggerFactory.getLogger(AbrangenciaServiceImpl.class);

	@Autowired
	private ScopeJpaRepository scopeJpaRepository;
	@Autowired
	private ScopeDetailsJpaRepository abrangenciaDetalhesRepository;
	@Autowired
	private RecursoServiceImpl recursoService;
	@Autowired
	private IEmpresaService empresaService;
	@Autowired
	private IPlantaService IPlantaService;


	public ScopeDetails findByAbrangenciaAndRecursoContaining(Scope codigo, Resources nome) {
		List<ScopeDetails> results = abrangenciaDetalhesRepository.findByAbrangencia_abrcodAndRecurso_recnomContaining(codigo.getId(), nome.getName());

		if (results.size() > 1) {
			throw new NonUniqueResultException("Query did not return a unique result: " + results.size() + " results were returned");
		}

		return results.isEmpty() ? null : results.get(0);
	}

	public ScopeDetails findByAbrangenciaAndRecursoContainingAbrangencia(Scope codigo, Resources nome) {
		List<ScopeDetails> results = abrangenciaDetalhesRepository.findByAbrangencia_abrcodAndRecurso_recnomContaining(codigo.getId(), nome.getName());
		if (results.isEmpty())
			return null;
		return results.get(0);
	}

	public Scope findByIdEntity(@NonNull Long codigo) throws EntityNotFoundException {
		return scopeJpaRepository.findById(codigo).orElse(null);
	}

	public Scope findByIdEntity(String nome) {
		return scopeJpaRepository.findByAbrnomLike(nome).orElse(null);
	}

	public AbrangenciaDTO findByIdSimples(@NonNull Long codigo) throws EntityNotFoundException {
		Scope scope = findByIdEntity(codigo);
		return new AbrangenciaDTO(scope);
	}

	public ItensAbrangentes findByItemAbrangenceEntity() throws IOException {
		var empresaList = empresaService.sendListAbrangenciaEmpresaDTO();
		var planta = IPlantaService.sendListAbrangenciaPlantaDTO();

		return new ItensAbrangentes(empresaList, planta);
	}

	@Override
	public ResponseEntity<ItensAbrangentes> findByItemAbrangence() throws IOException {
		return MessageResponse.success(findByItemAbrangenceEntity());
	}

	@Override
	public ResponseEntity<Page<AbrangenciaListaDetalhesDTO>> findAll(String nome, Pageable pageable) throws EntityNotFoundException, IOException {
		Objects.requireNonNull(pageable, "Pageable da Abrangência está nulo.");
		Specification<Scope> spec = Specification.where(null);
		spec = spec.and(Scope.filterByFields(nome));

		Page<Scope> result = scopeJpaRepository.findAll(spec, pageable);

		return MessageResponse.success(result.map(abrangencia -> {
			var details = abrangenciaDetalhesRepository.findByAbrangencia_Abrcod(abrangencia.getId());
			var detailsDTOList = details.stream().map(AbrangenciaDetalhesDTO::new).toList();
			return new AbrangenciaListaDetalhesDTO(abrangencia, detailsDTOList);
		}));
	}

	@Override
	public ResponseEntity<AbrangenciaListaDetalhesDTO> save(AbrangenciaModel abrangenciaModel) throws IOException {
		Scope scope = null;
		try {
			Objects.requireNonNull(abrangenciaModel.getNome(), "Nome da Abrangência está nulo.");
			Objects.requireNonNull(abrangenciaModel.getDescricao(), "Descrição da Abrangência está nula.");

			scope = scopeJpaRepository.save(new Scope(null, abrangenciaModel.getNome(), abrangenciaModel.getDescricao()));

			var detalhes = createAbrangenciaDetalhesList(scope, abrangenciaModel.getRecursos());

			return MessageResponse.create(new AbrangenciaListaDetalhesDTO(scope, detalhes));
		} catch (DataIntegrityViolationException e) {
			log.error("Erro de integridade ao criar a Abrangência: ", e);
			if (scope != null && scope.getId() != null)
				scopeJpaRepository.deleteById(scope.getId());

			throw new IOException("Erro de integridade ao criar a Abrangência.", e);
		} catch (Exception e) {
			log.error("Erro ao criar a Abrangência e seus detalhes: ", e);
			if (scope != null && scope.getId() != null)
				scopeJpaRepository.deleteById(scope.getId());

			throw new IOException("Erro inesperado ao criar a Abrangência.", e);
		}
	}

	public List<AbrangenciaDetalhesDTO> createAbrangenciaDetalhesList(Scope scope, List<AbrangenciaDetalhesModel> recursos) {
		return recursos == null ? List.of() // Retorna uma lista vazia em vez de null.
				: recursos.stream().map(recurso -> new AbrangenciaDetalhesDTO(saveOrUpdateAbrangenciaDetalhes(scope, recurso))).toList();
	}

	@Override
	public ResponseEntity<AbrangenciaListaDetalhesDTO> update(@NonNull Long codigo, AbrangenciaModel abrangenciaModel) {
		Objects.requireNonNull(codigo, "Código da Abrangência está nulo.");
		Objects.requireNonNull(abrangenciaModel.getNome(), "Nome da Abrangência está nulo.");

		Scope scope = scopeJpaRepository.save(new Scope(codigo, abrangenciaModel.getNome(), abrangenciaModel.getDescricao()));

		List<AbrangenciaDetalhesDTO> abrangenciaDetalhesDTOList = abrangenciaModel.getRecursos() == null ? null : abrangenciaModel.getRecursos().stream().map(recurso -> new AbrangenciaDetalhesDTO(saveOrUpdateAbrangenciaDetalhes(
                scope, recurso))).toList();

		if (abrangenciaDetalhesDTOList == null || abrangenciaDetalhesDTOList.isEmpty()) {
			abrangenciaDetalhesRepository.deleteById(codigo);
		}

		return MessageResponse.create(new AbrangenciaListaDetalhesDTO(scope, abrangenciaDetalhesDTOList));
	}

	@Override
	public ResponseEntity<List<AbrangenciaListaDetalhesDTO>> findAll() throws EntityNotFoundException, IOException {
		List<Scope> scopeList = scopeJpaRepository.findAll();

		List<AbrangenciaListaDetalhesDTO> abrangenciaDTOList = scopeList.stream().map(abrangencia -> {
			var details = abrangenciaDetalhesRepository.findByAbrangencia_Abrcod(abrangencia.getId());
			var detailsDTOList = details.stream().map(AbrangenciaDetalhesDTO::new).toList();
			return new AbrangenciaListaDetalhesDTO(abrangencia, detailsDTOList);
		}).toList();

		return MessageResponse.success(abrangenciaDTOList);
	}

	@Override
	public ResponseEntity<AbrangenciaListaDetalhesDTO> findById(@NonNull Long codigo) throws EntityNotFoundException, IOException {
		Scope scope = findByIdEntity(codigo);
		if (scope == null)
			return MessageResponse.success(null);

		List<AbrangenciaDetalhesDTO> detailsDTOList = abrangenciaDetalhesRepository.findByAbrangencia_Abrcod(scope.getId()).stream().map(AbrangenciaDetalhesDTO::new).toList();

		return MessageResponse.success(new AbrangenciaListaDetalhesDTO(scope, detailsDTOList.isEmpty() ? null : detailsDTOList));
	}

	@Override
	public ResponseEntity<Void> delete(@NonNull Long codigo) throws IOException {

		Scope entityScope = findByIdEntity(codigo);
		if (entityScope == null)
			throw new EntityNotFoundException("Abrangência não encontrada com o código: " + codigo);

		var listAbrangenciaDetalhes = abrangenciaDetalhesRepository.findByAbrangencia_Abrcod(entityScope.getId());

		listAbrangenciaDetalhes.forEach(map -> {
			Long abdcod = map.getId();
			if (abdcod != null) {
				abrangenciaDetalhesRepository.deleteById(abdcod);
			} else {
				log.warn("Detalhe da abrangência com ID nulo encontrado. Ignorando deleção.");
			}
		});

		scopeJpaRepository.deleteById(codigo);
		return MessageResponse.noContent();

	}

	public ScopeDetails saveOrUpdateAbrangenciaDetalhes(Scope scope, AbrangenciaDetalhesModel recurso) {
		String recursoNome = Objects.requireNonNull(recurso.getRecurso().getNome(), "Nome do recurso está nulo.");
		Objects.requireNonNull(recurso.getHierarquia(), "Valor da Hierarquia está nulo.");

		Resources resourcesEntity = recursoService.findByIdEntity(recursoNome);

		ScopeDetails detalhesOpt = findByAbrangenciaAndRecursoContaining(scope, resourcesEntity);

		JsonNodeConverter jsonNode = new JsonNodeConverter();
		ScopeDetails detalhes = new ScopeDetails(detalhesOpt == null ? null : detalhesOpt.getId(),
                scope, resourcesEntity, recurso.getHierarquia(), jsonNode.convertToDatabaseColumn(recurso.getDados()));

		return abrangenciaDetalhesRepository.save(detalhes);
	}

	public ScopeDetails saveOrUpdateAbrangenciaDetalhes(Scope scope, ScopeDetails detalhes) {
		Objects.requireNonNull(scope, "Abrangência está nula.");
		Objects.requireNonNull(detalhes, "Detalhes estão nulos.");
		Objects.requireNonNull(detalhes.getResource(), "Recurso está nulo.");
		Objects.requireNonNull(detalhes.getHierarchy(), "Hierarquia está nula.");
		Objects.requireNonNull(detalhes.getAbddat(), "Dados estão nulos.");

		Resources resources = detalhes.getResource();

		ScopeDetails detalhesOpt = findByAbrangenciaAndRecursoContaining(scope, resources);

		ScopeDetails scopeDetails = new ScopeDetails(detalhesOpt == null ? null : detalhesOpt.getId(),
                scope, resources, detalhes.getHierarchy(), detalhes.getAbddat());

		return abrangenciaDetalhesRepository.save(scopeDetails);
	}

	public Scope createUpdateAbrangencia(Scope scope) {
		return scopeJpaRepository.save(scope);
	}
}
