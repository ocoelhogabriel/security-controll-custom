package com.ocoelhogabriel.security_control_custom.application.usecase;

import java.io.IOException;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.ocoelhogabriel.security_control_custom.application.dto.PlantaModel;
import com.ocoelhogabriel.security_control_custom.application.dto.PlantaDTO;
import jakarta.persistence.EntityNotFoundException;

public interface IPlantaService {

	ResponseEntity<PlantaDTO> save(PlantaModel planta) throws IOException;

	ResponseEntity<Void> deleteByPlacod(Long codigo) throws IOException;

	ResponseEntity<PlantaDTO> update(Long codigo, PlantaModel planta) throws IOException;

	List<PlantaDTO> sendListAbrangenciaPlantaDTO() throws IOException;

	ResponseEntity<List<PlantaDTO>> findAllPlantaDTO() throws IOException;

	ResponseEntity<PlantaDTO> findById(Long codigo) throws IOException, EmptyResultDataAccessException;

	ResponseEntity<Page<PlantaDTO>> plantaFindAllPaginado(String searchTerm, Pageable pageable) throws EntityNotFoundException, IOException;
}
