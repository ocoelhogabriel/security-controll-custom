package com.ocoelhogabriel.security_control_custom.application.rest;

import com.ocoelhogabriel.security_control_custom.application.dto.AbrangenciaListaDetalhesDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.AbrangenciaModel;
import com.ocoelhogabriel.security_control_custom.application.exception.ScopeNotFoundException;
import com.ocoelhogabriel.security_control_custom.application.port.in.IAbrangenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/abrangencia")
@Tag(name = "Abrangencia", description = "API para consulta e gerenciamento de Abrangências")
public class AbrangenciaController extends SecurityRestController {

	@Autowired
	private IAbrangenciaService abrangenciaServImpl;

	@PostMapping("/v1")
	@Operation(description = "Criar uma nova Abrangência. Envia um objeto de abrangência e armazena-o no sistema.")
	public ResponseEntity<AbrangenciaListaDetalhesDTO> criarAbrangencia(@RequestBody AbrangenciaModel cadastro) {
		AbrangenciaListaDetalhesDTO novaAbrangencia = abrangenciaServImpl.save(cadastro);
		return ResponseEntity.status(HttpStatus.CREATED).body(novaAbrangencia);
	}

	@GetMapping("/v1/{codigo}")
	@Operation(description = "Buscar uma abrangência pelo código. Retorna os detalhes de uma abrangência específica com base no código fornecido.")
	public ResponseEntity<AbrangenciaListaDetalhesDTO> buscarAbrangenciaPorCodigo(@PathVariable Long codigo) {
		try {
			AbrangenciaListaDetalhesDTO abrangencia = abrangenciaServImpl.findById(codigo);
			return ResponseEntity.ok(abrangencia);
		} catch (ScopeNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/v1")
	@Operation(description = "Listar todas as abrangências cadastradas. Retorna uma lista de todas as abrangências existentes.")
	public ResponseEntity<List<AbrangenciaListaDetalhesDTO>> buscarListarAbrangencia() {
		List<AbrangenciaListaDetalhesDTO> abrangencias = abrangenciaServImpl.findAll();
		return ResponseEntity.ok(abrangencias);
	}

	@GetMapping("/v1/paginado")
	@Operation(description = "Busca paginada de abrangências cadastradas. Fornece uma lista de abrangências com paginação, filtragem e ordenação.")
	public ResponseEntity<Page<AbrangenciaListaDetalhesDTO>> buscarAbrangenciaPaginado(@RequestParam(value = "pagina", defaultValue = "0") Integer pagina, @RequestParam(value = "tamanho", defaultValue = "10") Integer tamanho, @RequestParam(value = "filtro", required = false) String filtro) {
		Page<AbrangenciaListaDetalhesDTO> paginaAbrangencias = abrangenciaServImpl.findAll(filtro, PageRequest.of(pagina, tamanho));
		return ResponseEntity.ok(paginaAbrangencias);
	}

	@PutMapping("/v1/{codigo}")
	@Operation(description = "Atualizar uma abrangência existente. Atualiza os detalhes de uma abrangência com base no código fornecido.")
	public ResponseEntity<AbrangenciaListaDetalhesDTO> atualizarAbrangencia(@PathVariable Long codigo, @RequestBody AbrangenciaModel entity) {
		try {
			AbrangenciaListaDetalhesDTO abrangenciaAtualizada = abrangenciaServImpl.update(codigo, entity);
			return ResponseEntity.ok(abrangenciaAtualizada);
		} catch (ScopeNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/v1/{codigo}")
	@Operation(description = "Deletar uma abrangência pelo código. Remove uma abrangência específica com base no código fornecido.")
	public ResponseEntity<Void> deletarAbrangencia(@PathVariable Long codigo) {
		try {
			abrangenciaServImpl.delete(codigo);
			return ResponseEntity.noContent().build();
		} catch (ScopeNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
