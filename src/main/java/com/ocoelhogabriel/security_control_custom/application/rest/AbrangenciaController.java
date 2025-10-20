package com.ocoelhogabriel.security_control_custom.application.rest;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ocoelhogabriel.security_control_custom.application.dto.AbrangenciaModel;
import com.ocoelhogabriel.security_control_custom.application.dto.AbrangenciaListaDetalhesDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.ItensAbrangentes;
import com.ocoelhogabriel.security_control_custom.application.usecase.IAbrangenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;

@CrossOrigin
@RestController
@RequestMapping("/api/abrangencia")
@Tag(name = "Abrangencia", description = "API para consulta e gerenciamento de Abrangências")
public class AbrangenciaController extends SecurityRestController {

	@Autowired
	private IAbrangenciaService abrangenciaServImpl;

	@PostMapping("/v1")
	@Operation(description = "Criar uma nova Abrangência. Envia um objeto de abrangência e armazena-o no sistema.")
	public ResponseEntity<AbrangenciaListaDetalhesDTO> criarAbrangencia(@RequestBody AbrangenciaModel cadastro) throws IOException {
		return abrangenciaServImpl.save(cadastro);
	}

	@GetMapping("/v1/{codigo}")
	@Operation(description = "Buscar uma abrangência pelo código. Retorna os detalhes de uma abrangência específica com base no código fornecido.")
	public ResponseEntity<AbrangenciaListaDetalhesDTO> buscarAbrangenciaPorCodigo(@PathVariable Long codigo) throws EntityNotFoundException, IOException {
		return abrangenciaServImpl.findById(codigo);
	}

	@GetMapping("/v1")
	@Operation(description = "Listar todas as abrangências cadastradas. Retorna uma lista de todas as abrangências existentes.")
	public ResponseEntity<List<AbrangenciaListaDetalhesDTO>> buscarListarAbrangencia() throws EntityNotFoundException, IOException {
		return abrangenciaServImpl.findAll();
	}

	@GetMapping("/v1/lista-items-abrangentes")
	@Operation(description = "Listar todos os itens abrangentes por recurso. Retorna uma lista detalhada dos itens abrangentes organizados por recurso.")
	public ResponseEntity<ItensAbrangentes> buscarListarItemsAbrangentes() throws EntityNotFoundException, IOException {
		return abrangenciaServImpl.findByItemAbrangence();
	}

	@GetMapping("/v1/paginado")
	@Operation(description = "Busca paginada de abrangências cadastradas. Fornece uma lista de abrangências com paginação, filtragem e ordenação.")
	public ResponseEntity<Page<AbrangenciaListaDetalhesDTO>> buscarAbrangenciaPaginado(@RequestParam(value = "pagina", defaultValue = "0") Integer pagina, @RequestParam(value = "tamanho", defaultValue = "10") Integer tamanho, @RequestParam(value = "filtro", required = false) String filtro)
			throws EntityNotFoundException, IOException {
		return abrangenciaServImpl.findAll(filtro, PageRequest.of(pagina, tamanho));
	}

	@PutMapping("/v1/{codigo}")
	@Operation(description = "Atualizar uma abrangência existente. Atualiza os detalhes de uma abrangência com base no código fornecido.")
	public ResponseEntity<AbrangenciaListaDetalhesDTO> atualizarAbrangencia(@PathVariable Long codigo, @RequestBody AbrangenciaModel entity) {
		return abrangenciaServImpl.update(codigo, entity);
	}

	@DeleteMapping("/v1/{codigo}")
	@Operation(description = "Deletar uma abrangência pelo código. Remove uma abrangência específica com base no código fornecido.")
	public ResponseEntity<Void> deletarAbrangencia(@PathVariable Long codigo) throws IOException {
		return abrangenciaServImpl.delete(codigo);
	}
}