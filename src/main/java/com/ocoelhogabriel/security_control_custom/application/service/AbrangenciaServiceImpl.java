package com.ocoelhogabriel.security_control_custom.application.service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.ocoelhogabriel.security_control_custom.domain.entity.ResourcesDomain;
import com.ocoelhogabriel.security_control_custom.domain.entity.ScopeDetailsDomain;
import com.ocoelhogabriel.security_control_custom.domain.entity.ScopeDomain;
import com.ocoelhogabriel.security_control_custom.domain.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.ocoelhogabriel.security_control_custom.application.dto.AbrangenciaDetalhesModel;
import com.ocoelhogabriel.security_control_custom.application.dto.AbrangenciaModel;
import com.ocoelhogabriel.security_control_custom.application.dto.AbrangenciaDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.AbrangenciaDetalhesDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.AbrangenciaListaDetalhesDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.ItensAbrangentes;
import com.ocoelhogabriel.security_control_custom.domain.service.IAbrangenciaService;
import com.ocoelhogabriel.security_control_custom.domain.service.IPlantaService;
import com.ocoelhogabriel.security_control_custom.infrastructure.utils.JsonNodeConverter;
import com.ocoelhogabriel.security_control_custom.infrastructure.utils.message.MessageResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NonUniqueResultException;

@Service
public class AbrangenciaServiceImpl implements IAbrangenciaService {

	private static final Logger log = LoggerFactory.getLogger(AbrangenciaServiceImpl.class);

	@Autowired
	private ScopeRepository scopeRepository; // Substituído ScopeJpaRepository por ScopeRepository
	@Autowired
	private ScopeDetailsRepository scopeDetailsRepository; // Substituído ScopeDetailsJpaRepository por ScopeDetailsRepository
	@Autowired
	private ResourcesRepository recursoService;
	@Autowired
	private CompanyRepository empresaService;
	@Autowired
	private PlanRepository plantaService;


	public ScopeDetailsDomain findByAbrangenciaAndRecursoContaining(Long scopeId, String resourceName) {
		List<ScopeDetailsDomain> results = scopeDetailsRepository.findByScopeIdAndResourceNameContaining(scopeId, resourceName);

		if (results.size() > 1) {
			throw new NonUniqueResultException("Query did not return a unique result: " + results.size() + " results were returned");
		}

		return results.isEmpty() ? null : results.get(0);
	}

	public ScopeDetailsDomain findByAbrangenciaAndRecursoContainingAbrangencia(Long scopeId, String resourceName) {
		List<ScopeDetailsDomain> results = scopeDetailsRepository.findByScopeIdAndResourceNameContaining(scopeId, resourceName);
		if (results.isEmpty())
			return null;
		return results.get(0);
	}

	public ScopeDomain findByIdEntity(@NonNull Long codigo) throws EntityNotFoundException {
		return scopeRepository.findById(codigo).orElse(null);
	}

	public ScopeDomain findByIdEntity(String nome) {
		return scopeRepository.findByNameLike(nome).orElse(null);
	}

	public AbrangenciaDTO findByIdSimples(@NonNull Long codigo) throws EntityNotFoundException {
		ScopeDomain scope = findByIdEntity(codigo);
		return new AbrangenciaDTO(scope);
	}


	@Override
	public ResponseEntity<Page<AbrangenciaListaDetalhesDTO>> findAll(String nome, Pageable pageable) throws EntityNotFoundException, IOException {
		Objects.requireNonNull(pageable, "Pageable da Abrangência está nulo.");

		Page<ScopeDomain> result;
		if (nome == null || nome.isEmpty()) {
			result = scopeRepository.findAll(pageable);
		} else {
			result = scopeRepository.findByNameLike(nome, pageable);
		}

		return MessageResponse.success(result.map(abrangencia -> {
			var details = scopeDetailsRepository.findByScopeId(abrangencia.getId());
			var detailsDTOList = details.stream().map(AbrangenciaDetalhesDTO::new).toList();
			return new AbrangenciaListaDetalhesDTO(abrangencia, detailsDTOList);
		}));
	}

	@Override
	public ResponseEntity<AbrangenciaListaDetalhesDTO> save(AbrangenciaModel abrangenciaModel) throws IOException {
		ScopeDomain scope = null;
		try {
			Objects.requireNonNull(abrangenciaModel.getNome(), "Nome da Abrangência está nulo.");
			Objects.requireNonNull(abrangenciaModel.getDescricao(), "Descrição da Abrangência está nula.");

			scope = scopeRepository.save(new ScopeDomain(null, abrangenciaModel.getNome(), abrangenciaModel.getDescricao()));

			var detalhes = createAbrangenciaDetalhesList(scope, abrangenciaModel.getRecursos());

			return MessageResponse.create(new AbrangenciaListaDetalhesDTO(scope, detalhes));
		} catch (DataIntegrityViolationException e) {
			log.error("Erro de integridade ao criar a Abrangência: ", e);
			if (scope != null && scope.getId() != null)
				scopeRepository.deleteById(scope.getId());

			throw new IOException("Erro de integridade ao criar a Abrangência.", e);
		} catch (Exception e) {
			log.error("Erro ao criar a Abrangência e seus detalhes: ", e);
			if (scope != null && scope.getId() != null)
				scopeRepository.deleteById(scope.getId());

			throw new IOException("Erro inesperado ao criar a Abrangência.", e);
		}
	}

	public List<AbrangenciaDetalhesDTO> createAbrangenciaDetalhesList(ScopeDomain scopeDomain, List<AbrangenciaDetalhesModel> recursos) {
		return recursos == null ? List.of() // Retorna uma lista vazia em vez de null.
				: recursos.stream().map(recurso -> new AbrangenciaDetalhesDTO(saveOrUpdateAbrangenciaDetalhes(scopeDomain, recurso))).toList();
	}

	@Override
	public ResponseEntity<AbrangenciaListaDetalhesDTO> update(@NonNull Long codigo, AbrangenciaModel abrangenciaModel) {
		Objects.requireNonNull(codigo, "Código da Abrangência está nulo.");
		Objects.requireNonNull(abrangenciaModel.getNome(), "Nome da Abrangência está nulo.");

		ScopeDomain existingScope = scopeRepository.findById(codigo)
				.orElseThrow(() -> new EntityNotFoundException("Abrangência não encontrada com o código: " + codigo));

		existingScope.setName(abrangenciaModel.getNome());
		existingScope.setDescription(abrangenciaModel.getDescricao());

		ScopeDomain updatedScope = scopeRepository.save(existingScope);

		List<AbrangenciaDetalhesDTO> abrangenciaDetalhesDTOList = abrangenciaModel.getRecursos() == null ? null : abrangenciaModel.getRecursos().stream().map(recurso -> new AbrangenciaDetalhesDTO(saveOrUpdateAbrangenciaDetalhes(
                updatedScope, recurso))).toList();

		// Lógica para deletar detalhes antigos que não estão na lista atual
		List<Long> currentDetailIds = abrangenciaDetalhesDTOList != null ? abrangenciaDetalhesDTOList.stream()
				.map(AbrangenciaDetalhesDTO::getCodigo).filter(Objects::nonNull).collect(Collectors.toList()) : List.of();

		scopeDetailsRepository.findByScopeId(updatedScope.getId()).stream()
				.filter(detail -> !currentDetailIds.contains(detail.getId()))
				.forEach(detail -> scopeDetailsRepository.deleteById(detail.getId()));

		return MessageResponse.create(new AbrangenciaListaDetalhesDTO(updatedScope, abrangenciaDetalhesDTOList));
	}

	@Override
	public ResponseEntity<List<AbrangenciaListaDetalhesDTO>> findAll() throws EntityNotFoundException, IOException {
		List<ScopeDomain> scopeList = scopeRepository.findAll();

		List<AbrangenciaListaDetalhesDTO> abrangenciaDTOList = scopeList.stream().map(abrangencia -> {
			var details = scopeDetailsRepository.findByScopeId(abrangencia.getId());
			var detailsDTOList = details.stream().map(AbrangenciaDetalhesDTO::new).toList();
			return new AbrangenciaListaDetalhesDTO(abrangencia, detailsDTOList);
		}).toList();

		return MessageResponse.success(abrangenciaDTOList);
	}

	@Override
	public ResponseEntity<AbrangenciaListaDetalhesDTO> findById(@NonNull Long codigo) throws EntityNotFoundException, IOException {
		ScopeDomain scope = findByIdEntity(codigo);
		if (scope == null)
			return MessageResponse.success(null);

		List<AbrangenciaDetalhesDTO> detailsDTOList = scopeDetailsRepository.findByScopeId(scope.getId()).stream().map(AbrangenciaDetalhesDTO::new).toList();

		return MessageResponse.success(new AbrangenciaListaDetalhesDTO(scope, detailsDTOList.isEmpty() ? null : detailsDTOList));
	}

	@Override
	public ResponseEntity<Void> delete(@NonNull Long codigo) throws IOException {

		ScopeDomain entityScope = findByIdEntity(codigo);
		if (entityScope == null)
			throw new EntityNotFoundException("Abrangência não encontrada com o código: " + codigo);

		var listAbrangenciaDetalhes = scopeDetailsRepository.findByScopeId(entityScope.getId());

		listAbrangenciaDetalhes.forEach(map -> {
			Long abdcod = map.getId();
			if (abdcod != null) {
				scopeDetailsRepository.deleteById(abdcod);
			} else {
				log.warn("Detalhe da abrangência com ID nulo encontrado. Ignorando deleção.");
			}
		});

		scopeRepository.deleteById(codigo);
		return MessageResponse.noContent();

	}

	public ScopeDetailsDomain saveOrUpdateAbrangenciaDetalhes(ScopeDomain scopeDomain, AbrangenciaDetalhesModel recurso) {
		String recursoNome = Objects.requireNonNull(recurso.getRecurso().getNome(), "Nome do recurso está nulo.");
		Objects.requireNonNull(recurso.getHierarquia(), "Valor da Hierarquia está nulo.");

		ResourcesDomain resourcesDomain = recursoService.findByName(recursoNome).orElseThrow(() -> new EntityNotFoundException("Recurso não encontrado: " + recursoNome));

		ScopeDetailsDomain detalhesOpt = findByAbrangenciaAndRecursoContaining(scopeDomain.getId(), resourcesDomain.getName());

		JsonNodeConverter jsonNode = new JsonNodeConverter();
		ScopeDetailsDomain detalhes = new ScopeDetailsDomain(detalhesOpt == null ? null : detalhesOpt.getId(),
                scopeDomain, resourcesDomain, recurso.getHierarquia(), jsonNode.convertToDatabaseColumn(recurso.getDados()));

		return scopeDetailsRepository.save(detalhes);
	}

	public ScopeDetailsDomain saveOrUpdateAbrangenciaDetalhes(ScopeDomain scopeDomain, ScopeDetailsDomain detalhes) {
		Objects.requireNonNull(scopeDomain, "Abrangência está nula.");
		Objects.requireNonNull(detalhes, "Detalhes estão nulos.");
		Objects.requireNonNull(detalhes.getScopeDomain(), "Recurso está nulo.");
		Objects.requireNonNull(detalhes.getHierarchy(), "Hierarquia está nula.");
		Objects.requireNonNull(detalhes.getData(), "Dados estão nulos.");

		ResourcesDomain resourcesDomain = detalhes.getResource();

		ScopeDetailsDomain detalhesOpt = findByAbrangenciaAndRecursoContaining(scopeDomain.getId(), resourcesDomain.getName());

		ScopeDetailsDomain scopeDetails = new ScopeDetailsDomain(detalhesOpt == null ? null : detalhesOpt.getId(),
                scopeDomain, resourcesDomain, detalhes.getHierarchy(), detalhes.getData());

		return scopeDetailsRepository.save(scopeDetails);
	}

	public ScopeDomain createUpdateAbrangencia(ScopeDomain scopeDomain) {
		return scopeRepository.save(scopeDomain);
	}
}
