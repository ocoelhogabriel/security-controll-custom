package com.ocoelhogabriel.security_control_custom.application.rest;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ocoelhogabriel.security_control_custom.application.dto.PlantaModel;
import com.ocoelhogabriel.security_control_custom.application.dto.PlantaDTO;
import com.ocoelhogabriel.security_control_custom.application.usecase.IPlantaService;
import com.ocoelhogabriel.security_control_custom.infrastructure.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/planta")
@Tag(name = "Planta", description = "API para gerenciamento de plantas")
public class PlantaController extends SecurityRestController {

	@Autowired
	private IPlantaService planta;

	@GetMapping("/v1")
	@Operation(description = "Listar todas as plantas cadastradas. Retorna uma lista de todas as plantas existentes.")
	public ResponseEntity<List<PlantaDTO>> getPlanta() throws IOException {
		return planta.findAllPlantaDTO();
	}

	@GetMapping("/v1/{codigo}")
	@Operation(description = "Buscar pela planta cadastrado. Retorna uma planta existente.")
	public ResponseEntity<PlantaDTO> getIdPlanta(@PathVariable Long codigo) throws EmptyResultDataAccessException, IOException {
		return planta.findById(codigo);
	}

	@PostMapping("/v1")
	@Operation(description = "Criar uma nova planta. Recebe os detalhes da planta e a armazena no sistema.")
	public ResponseEntity<PlantaDTO> createPlanta(@RequestBody PlantaModel plantaDto) throws IOException {
		return planta.save(plantaDto);
	}

	@PutMapping("/v1/{codigo}")
	@Operation(description = "Atualizar uma planta existente. Atualiza os detalhes de uma planta com base no código fornecido.")
	public ResponseEntity<PlantaDTO> updatePlanta(@PathVariable Long codigo, @RequestBody PlantaModel plantaDto) throws IOException {
		return planta.update(codigo, plantaDto);
	}

	@DeleteMapping("/v1/{codigo}")
	@Operation(description = "Deletar uma planta existente. Remove uma planta específica com base no código fornecido.")
	public ResponseEntity<Void> deletePlanta(@PathVariable Long codigo) throws IOException {
		return planta.deleteByPlacod(codigo);
	}

	@Operation(description = "Recupera uma lista paginada de objetos PlantaDTO com filtragem e ordenação opcionais.")
	@Parameter(name = "filtro", description = "Termo de filtro opcional para buscar Plantas.")
	@Parameter(name = "pagina", description = "Número da página a ser recuperada, começando em 0.")
	@Parameter(name = "tamanho", description = "Número de itens por página.")
	@Parameter(name = "ordenarPor", description = "Campo pelo qual os resultados serão ordenados. (codigo, empresa, nome)")
	@Parameter(name = "direcao", description = "Direção da ordenação, podendo ser ASC (ascendente) ou DESC (descendente).")
	@GetMapping("/v1/paginado")
	public ResponseEntity<Page<PlantaDTO>> findAllPaginado(@RequestParam(value = "filtro", required = false) String filtro, @RequestParam(value = "pagina", defaultValue = "0") int pagina, @RequestParam(value = "tamanho", defaultValue = "10") int tamanho,
			@RequestParam(value = "ordenarPor", defaultValue = "codigo") String ordenarPor, @RequestParam(value = "direcao", defaultValue = "ASC") String direcao) throws EntityNotFoundException, IOException {

		return planta.plantaFindAllPaginado(filtro, Utils.consultaPage(PlantaDTO.filtrarDirecao(ordenarPor), direcao, pagina, tamanho));
	}

}
