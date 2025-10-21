package com.ocoelhogabriel.security_control_custom.domain.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;

import com.ocoelhogabriel.security_control_custom.application.dto.AbrangenciaModel;
import com.ocoelhogabriel.security_control_custom.application.dto.AbrangenciaListaDetalhesDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.ItensAbrangentes;
import jakarta.persistence.EntityNotFoundException;

public interface IAbrangenciaService {

    ResponseEntity<AbrangenciaListaDetalhesDTO> update(@NonNull Long codigo, AbrangenciaModel abrangenciaModel);

    ResponseEntity<AbrangenciaListaDetalhesDTO> save(AbrangenciaModel abrangenciaModel) throws IOException;

    ResponseEntity<List<AbrangenciaListaDetalhesDTO>> findAll() throws EntityNotFoundException, IOException;

    ResponseEntity<AbrangenciaListaDetalhesDTO> findById(@NonNull Long codigo) throws EntityNotFoundException, IOException;

    ResponseEntity<Void> delete(@NonNull Long perfil) throws IOException;

    ResponseEntity<Page<AbrangenciaListaDetalhesDTO>> findAll(String nome, Pageable pageable) throws EntityNotFoundException, IOException;

}
