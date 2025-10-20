package com.ocoelhogabriel.security_control_custom.application.service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.ocoelhogabriel.security_control_custom.application.dto.RecursoModel;
import com.ocoelhogabriel.security_control_custom.application.dto.RecursoDTO;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Resources;
import com.ocoelhogabriel.security_control_custom.domain.model.enums.RecursoMapEnum;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository.ResourcesJpaRepository;
import com.ocoelhogabriel.security_control_custom.application.usecase.IRecursoService;
import com.ocoelhogabriel.security_control_custom.infrastructure.utils.message.MessageResponse;
import jakarta.persistence.EntityNotFoundException;

@Service
public class RecursoServiceImpl implements IRecursoService {

	@Autowired
	private ResourcesJpaRepository resourcesJpaRepository;

	public Resources findByIdEntity(@NonNull Long codigo) {
		return resourcesJpaRepository.findById(codigo).orElseThrow(() -> new EntityNotFoundException("Recurso não encontrado com o código: " + codigo));
	}

	public Resources findByIdEntity(@NonNull String nome) {
		return resourcesJpaRepository.findByRecnom(nome).orElse(null);
	}

	public RecursoDTO findByIdApi(@NonNull String nome) throws EntityNotFoundException {
		Resources resources = findByIdEntity(nome);
		return new RecursoDTO(resources);
	}

	public RecursoDTO findByIdApi(@NonNull Long codigo) throws EntityNotFoundException {
		Resources resources = findByIdEntity(codigo);
		return new RecursoDTO(resources);
	}

	public Resources saveEntity(RecursoModel recursoModel) {
		validateRecursoModelFields(recursoModel);
		Resources resources = new Resources(null, RecursoMapEnum.mapDescricaoToNome(recursoModel.getNome().getNome()), recursoModel.getDescricao());
		return resourcesJpaRepository.save(resources);
	}

	public Resources updateEntity(Long codigo, RecursoModel recursoModel) {
		Objects.requireNonNull(codigo, "Código do Recurso está nulo.");
		validateRecursoModelFields(recursoModel);
		Resources resources = new Resources(codigo, recursoModel.getNome().getNome(), recursoModel.getDescricao());
		return resourcesJpaRepository.save(resources);
	}

	public List<Resources> findAllEntity() throws EntityNotFoundException {
		return resourcesJpaRepository.findAll();
	}

	public void deleteEntity(@NonNull Long codigo) throws IOException {
		Objects.requireNonNull(codigo, "Código do Recurso está nulo.");
		try {
			resourcesJpaRepository.deleteById(codigo);
		} catch (Exception e) {
			throw new IOException("Erro ao apagar o item. Mensagem: " + e.getMessage(), e);
		}
	}

	public Page<Resources> findAllEntity(String nome, Pageable pageable) throws EntityNotFoundException {
		Objects.requireNonNull(pageable, "Pageable do Recurso está nulo.");
		if (nome == null) {
			return resourcesJpaRepository.findAll(pageable);
		} else {
			return resourcesJpaRepository.findByRecnomLike(nome, pageable);
		}
	}

	@Override
	public ResponseEntity<Page<RecursoDTO>> findAll(String nome, Pageable pageable) throws EntityNotFoundException, IOException {
		Page<Resources> recursos = findAllEntity(nome, pageable);
		return MessageResponse.success(recursos.map(RecursoDTO::new));
	}

	@Override
	public ResponseEntity<RecursoDTO> save(RecursoModel recursoModel) {
		Resources resources = saveEntity(recursoModel);
		return MessageResponse.success(new RecursoDTO(resources));
	}

	@Override
	public ResponseEntity<RecursoDTO> update(Long codigo, RecursoModel recursoModel) {
		Resources resources = updateEntity(codigo, recursoModel);
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
}
