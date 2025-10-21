package com.ocoelhogabriel.security_control_custom.domain.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;

import com.ocoelhogabriel.security_control_custom.application.dto.PerfilModel;
import com.ocoelhogabriel.security_control_custom.application.dto.PerfilPermissaoDTO;
import jakarta.persistence.EntityNotFoundException;

public interface IPerfilPermService {

	ResponseEntity<PerfilPermissaoDTO> save(PerfilModel perModel) throws IOException;

	ResponseEntity<PerfilPermissaoDTO> update(@NonNull Long codigo, PerfilModel perModel) throws EntityNotFoundException, IOException;

	ResponseEntity<List<PerfilPermissaoDTO>> findAll() throws EntityNotFoundException, IOException;

	ResponseEntity<PerfilPermissaoDTO> findById(@NonNull Long codigo) throws EntityNotFoundException, IOException;

	ResponseEntity<Void> delete(@NonNull Long perfil) throws IOException;

	ResponseEntity<Page<PerfilPermissaoDTO>> findAll(String nome, @NonNull Pageable pageable) throws EntityNotFoundException, IOException;

}
