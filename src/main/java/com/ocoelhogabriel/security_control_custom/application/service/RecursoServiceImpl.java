package com.ocoelhogabriel.security_control_custom.application.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.ocoelhogabriel.security_control_custom.domain.entity.ResourcesDomain;
import com.ocoelhogabriel.security_control_custom.domain.repository.ResourcesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.ocoelhogabriel.security_control_custom.application.dto.RecursoModel;
import com.ocoelhogabriel.security_control_custom.application.dto.RecursoDTO;
import com.ocoelhogabriel.security_control_custom.domain.model.enums.RecursoMapEnum;
import com.ocoelhogabriel.security_control_custom.domain.service.IRecursoService;
import com.ocoelhogabriel.security_control_custom.infrastructure.utils.message.MessageResponse;
import jakarta.persistence.EntityNotFoundException;

@Service
public class RecursoServiceImpl implements IRecursoService {

	@Autowired
	private ResourcesRepository resourcesRepository; // Substituído ResourcesJpaRepository por ResourcesRepository

	public ResourcesDomain findByIdEntity(@NonNull Long codigo) {
		return resourcesRepository.findById(codigo).orElseThrow(() -> new EntityNotFoundException("Recurso não encontrado com o código: " + codigo));
	}

	public ResourcesDomain findByIdEntity(@NonNull String nome) {
		return resourcesRepository.findByName(nome).orElse(null); // Usando findByName do ResourcesRepository
	}

	public RecursoDTO findByIdApi(@NonNull String nome) throws EntityNotFoundException {
		ResourcesDomain resources = findByIdEntity(nome);
		return new RecursoDTO(resources);
	}

	public RecursoDTO findByIdApi(@NonNull Long codigo) throws EntityNotFoundException {
		ResourcesDomain resources = findByIdEntity(codigo);
		return new RecursoDTO(resources);
	}

	public ResourcesDomain saveEntity(RecursoModel recursoModel) {
		validateRecursoModelFields(recursoModel);
		ResourcesDomain resourcesDomain = new ResourcesDomain(null, RecursoMapEnum.mapDescricaoToNome(recursoModel.getNome().getNome()), recursoModel.getDescricao());
		return resourcesRepository.save(resourcesDomain);
	}

	public ResourcesDomain updateEntity(Long codigo, RecursoModel recursoModel) {
		Objects.requireNonNull(codigo, "Código do Recurso está nulo.");
		validateRecursoModelFields(recursoModel);
		ResourcesDomain existingResource = findByIdEntity(codigo);
		existingResource.setName(RecursoMapEnum.mapDescricaoToNome(recursoModel.getNome().getNome()));
		existingResource.setDescription(recursoModel.getDescricao());
		return resourcesRepository.save(existingResource);
	}

	public List<ResourcesDomain> findAllEntity() throws EntityNotFoundException {
		return resourcesRepository.findAll();
	}

	public void deleteEntity(@NonNull Long codigo) throws IOException {
		Objects.requireNonNull(codigo, "Código do Recurso está nulo.");
		try {
			resourcesRepository.deleteById(codigo);
		} catch (Exception e) {
			throw new IOException("Erro ao apagar o item. Mensagem: " + e.getMessage(), e);
		}
	}

	public Page<ResourcesDomain> findAllEntity(String nome, Pageable pageable) throws EntityNotFoundException {
		Objects.requireNonNull(pageable, "Pageable do Recurso está nulo.");
		if (nome == null) {
			return resourcesRepository.findAll(pageable);
		} else {
			return resourcesRepository.findByNameLike(nome, pageable);
		}
	}

	@Override
	public ResponseEntity<Page<RecursoDTO>> findAll(String nome, Pageable pageable) throws EntityNotFoundException, IOException {
		Page<ResourcesDomain> recursos = findAllEntity(nome, pageable);
		return MessageResponse.success(recursos.map(RecursoDTO::new));
	}

	@Override
	public ResponseEntity<RecursoDTO> save(RecursoModel recursoModel) {
		ResourcesDomain resources = saveEntity(recursoModel);
		return MessageResponse.success(new RecursoDTO(resources));
	}

	@Override
	public ResponseEntity<RecursoDTO> update(Long codigo, RecursoModel recursoModel) {
		ResourcesDomain resources = updateEntity(codigo, recursoModel);
		return MessageResponse.success(new RecursoDTO(resources));
	}

	@Override
	public ResponseEntity<List<RecursoDTO>> findAll() throws EntityNotFoundException, IOException {
		List<RecursoDTO> recursos = findAllEntity().stream().map(RecursoDTO::new).toList();
		return MessageResponse.success(recursos);
	}

	@Override
	public ResponseEntity<RecursoDTO> findById(@NonNull Long codigo) throws EntityNotFoundException, IOException {
		RecursoDTO recursoDTO = findByIdApi(codigo);
		return MessageResponse.success(recursoDTO);
	}

	@Override
	public ResponseEntity<RecursoDTO> findByString(@NonNull String nome) throws EntityNotFoundException, IOException {
		RecursoDTO recursoDTO = findByIdApi(nome);
		return MessageResponse.success(recursoDTO);
	}

	@Override
	public ResponseEntity<Void> delete(@NonNull Long codigo) throws IOException {
		deleteEntity(codigo);
		return MessageResponse.noContent();
	}

	private void validateRecursoModelFields(RecursoModel recursoModel) {
		Objects.requireNonNull(recursoModel.getNome(), "Nome do Recurso está nulo.");
		Objects.requireNonNull(recursoModel.getDescricao(), "Descrição do Recurso está nulo.");
	}

	/**
	 * Cria recursos padrão se eles não existirem no banco de dados.
	 */
	public void createDefaultResources() {
		List<String> defaultResourceNames = Arrays.asList(
				"USUARIO",
				"EMPRESA",
				"PERFIL",
				"ABRANGENCIA",
				"PLANTAS",
				"RECURSO",
				"PERMISSAO"
		);

		for (String resourceName : defaultResourceNames) {
			ResourcesDomain existingResource = resourcesRepository.findByName(resourceName).orElse(null);
			if (existingResource == null) {
				ResourcesDomain newResource = new ResourcesDomain(null, resourceName, "Recurso padrão: " + resourceName);
				resourcesRepository.save(newResource);
				System.out.println("Recurso padrão criado: " + resourceName);
			}
		}
	}
}
