package com.ocoelhogabriel.security_control_custom.application.rest;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
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

import com.ocoelhogabriel.security_control_custom.application.dto.PerfilModel;
import com.ocoelhogabriel.security_control_custom.application.dto.PerfilPermissaoDTO;
import com.ocoelhogabriel.security_control_custom.application.usecase.IPerfilPermService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/perfil")
@Tag(name = "Perfil", description = "API para gerenciamento de perfis de usuário e permissões de acesso.")
public class PerfilController extends SecurityRestController {

	@Autowired
	private IPerfilPermService perfilServImpl;

	@PostMapping("/v1")
	@Operation(description = "Criar um novo perfil de usuário com permissões de acesso. Recebe os detalhes do perfil e suas permissões e armazena no sistema.")
	public ResponseEntity<PerfilPermissaoDTO> criarPerfil(@Validated @RequestBody PerfilModel cadastro) throws IOException {
		return perfilServImpl.save(cadastro);
	}

	@GetMapping("/v1/{codigo}")
	@Operation(description = "Buscar perfil pelo código. Retorna os detalhes de um perfil específico com base no código fornecido.")
	public ResponseEntity<PerfilPermissaoDTO> buscarPerfilPorCodigo(@PathVariable @NonNull Long codigo) throws EntityNotFoundException, IOException {
		return perfilServImpl.findById(codigo);
	}

	@GetMapping("/v1")
	@Operation(description = "Listar todos os perfis cadastrados. Retorna uma lista de todos os perfis de acesso existentes.")
	public ResponseEntity<List<PerfilPermissaoDTO>> buscarListarPerfil() throws EntityNotFoundException, IOException {
		return perfilServImpl.findAll();
	}

	@GetMapping("/v1/paginado")
	@Operation(description = "Busca paginada de perfis de acesso. Fornece uma lista paginada de perfis com opções de filtragem e ordenação.")
	public ResponseEntity<Page<PerfilPermissaoDTO>> buscarPerfilPaginado(@RequestParam(value = "pagina", defaultValue = "0") Integer pagina, @RequestParam(value = "tamanho", defaultValue = "10") Integer tamanho, @RequestParam(value = "filtro", required = false) String filtro)
			throws EntityNotFoundException, IOException {
		return perfilServImpl.findAll(filtro, PageRequest.of(pagina, tamanho));
	}

	@PutMapping("/v1/{codigo}")
	@Operation(description = "Atualizar um perfil de acesso existente. Atualiza os detalhes de um perfil com base no código fornecido.")
	public ResponseEntity<PerfilPermissaoDTO> atualizarPerfil(@Valid @PathVariable @NonNull Long codigo, @Valid @RequestBody PerfilModel entity) throws EntityNotFoundException, IOException {
		return perfilServImpl.update(codigo, entity);
	}

	@DeleteMapping("/v1/{codigo}")
	@Operation(description = "Deletar um perfil de acesso. Remove um perfil específico com base no código fornecido.")
	public ResponseEntity<Void> deletarPerfil(@Valid @PathVariable @NonNull Long codigo) throws IOException {
		return perfilServImpl.delete(codigo);
	}
}
