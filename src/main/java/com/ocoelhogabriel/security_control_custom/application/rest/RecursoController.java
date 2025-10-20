package com.ocoelhogabriel.security_control_custom.application.rest;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import com.ocoelhogabriel.security_control_custom.application.dto.RecursoModel;
import com.ocoelhogabriel.security_control_custom.application.dto.RecursoDTO;
import com.ocoelhogabriel.security_control_custom.application.service.RecursoServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/recurso")
@Tag(name = "Recurso", description = "API para consulta e gerenciamento de recursos")
public class RecursoController extends SecurityRestController {

	@Autowired
	private RecursoServiceImpl recursoServImpl;

	@PostMapping("/v1")
	@Operation(description = "Criar um novo recurso. Recebe os detalhes do recurso e o armazena no sistema. Obs: Gerado automaticamente ao iniciar a aplicação.")
	public ResponseEntity<RecursoDTO> criarRecurso(@RequestBody RecursoModel cadastro) {
		return recursoServImpl.save(cadastro);
	}

	@GetMapping("/v1/{codigo}")
	@Operation(description = "Buscar recurso pelo código. Retorna os detalhes de um recurso específico com base no código fornecido.")
	public ResponseEntity<RecursoDTO> buscarRecursoPorCodigo(@PathVariable Long codigo) throws EntityNotFoundException, IOException {
		return recursoServImpl.findById(codigo);
	}

	@GetMapping("/v1")
	@Operation(description = "Listar todos os recursos cadastrados. Retorna uma lista de todos os recursos existentes.")
	public ResponseEntity<List<RecursoDTO>> buscarListarRecurso() throws EntityNotFoundException, IOException {
		return recursoServImpl.findAll();
	}

	@GetMapping("/v1/paginado")
	@Operation(description = "Busca paginada de recursos. Retorna uma lista paginada de recursos com opções de filtragem e ordenação. Obs: O campo 'ordenarPor' requer os seguintes dados: código, nome, descrição.")
	public ResponseEntity<Page<RecursoDTO>> buscarRecursoPaginado(@RequestParam(value = "pagina", defaultValue = "0") Integer pagina, @RequestParam(value = "tamanho", defaultValue = "10") Integer tamanho, @RequestParam(value = "nome", required = false) String nome)
			throws EntityNotFoundException, IOException {
		return recursoServImpl.findAll(nome, PageRequest.of(pagina, tamanho));
	}

	@PutMapping("/v1/{codigo}")
	@Operation(description = "Atualizar um recurso existente. Atualiza os detalhes de um recurso com base no código fornecido. Obs: Como recurso é gerado automaticamente, se alterado pode ocasionar algum problema nas funcionalidades.")
	public ResponseEntity<RecursoDTO> atualizarRecurso(@Valid @PathVariable Long codigo, @Valid @RequestBody RecursoModel entity) {
		return recursoServImpl.update(codigo, entity);
	}

	@DeleteMapping("/v1/{codigo}")
	@Operation(description = "Deletar um recurso pelo código. Remove um recurso específico com base no código fornecido. Obs: Como recurso é gerado automaticamente, se alterado pode ocasionar algum problema nas funcionalidades.")
	public ResponseEntity<Void> deletarRecurso(@Valid @PathVariable Long codigo) throws IOException {
		return recursoServImpl.delete(codigo);
	}
}
