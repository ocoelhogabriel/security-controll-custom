package com.ocoelhogabriel.security_control_custom.application.rest;

import com.ocoelhogabriel.security_control_custom.application.dto.UsuarioDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.UsuarioModel;
import com.ocoelhogabriel.security_control_custom.application.dto.UsuarioPermissaoDTO;
import com.ocoelhogabriel.security_control_custom.application.exception.AdminUserModificationException;
import com.ocoelhogabriel.security_control_custom.application.exception.UserNotFoundException;
import com.ocoelhogabriel.security_control_custom.application.port.in.IUsuarioService;
import com.ocoelhogabriel.security_control_custom.infrastructure.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/usuario")
@Tag(name = "Usuário", description = "API para gerenciamento de usuários")
public class UsuarioController extends SecurityRestController {

	@Autowired
	private IUsuarioService userServImpl;

	@PostMapping("/v1")
	@PreAuthorize("hasPermission('USUARIO', 'create')")
	@Operation(description = "Criar um novo usuário. Recebe os detalhes do usuário e o armazena no sistema.")
	public ResponseEntity<UsuarioDTO> criar(@RequestBody @NonNull UsuarioModel cadastro) {
		try {
			UsuarioDTO usuarioDTO = userServImpl.saveUpdateEncodePassword(cadastro);
			return ResponseEntity.status(HttpStatus.CREATED).body(usuarioDTO);
		} catch (AdminUserModificationException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		} catch (UserNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/v1/{codigo}")
	@PreAuthorize("hasPermission('USUARIO', 'find')")
	@Operation(description = "Buscar um usuário pelo código. Retorna os detalhes de um usuário específico com base no código fornecido.")
	public ResponseEntity<UsuarioDTO> buscarPorCodigo(@PathVariable @NonNull Long codigo) {
		try {
			UsuarioDTO usuarioDTO = userServImpl.findById(codigo);
			return ResponseEntity.ok(usuarioDTO);
		} catch (UserNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/v1/permissao/{codigo}")
	@PreAuthorize("hasPermission('USUARIO', 'find')")
	@Operation(description = "Buscar um usuário e suas permissões pelo código do usuário. Retorna os detalhes do usuário e suas permissões.")
	public ResponseEntity<UsuarioPermissaoDTO> buscarPorCodigoPermissaoUsuario(@PathVariable @NonNull Long codigo) {
		try {
			UsuarioPermissaoDTO usuarioPermissaoDTO = userServImpl.findByIdPermission(codigo);
			return ResponseEntity.ok(usuarioPermissaoDTO);
		} catch (UserNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/v1")
	@PreAuthorize("hasPermission('USUARIO', 'list')")
	@Operation(description = "Listar todos os usuários cadastrados. Retorna uma lista de todos os usuários existentes.")
	public ResponseEntity<List<UsuarioDTO>> listar() {
		try {
			List<UsuarioDTO> listaUsuarioDTO = userServImpl.findAll();
			return ResponseEntity.ok(listaUsuarioDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/v1/paginado")
	@PreAuthorize("hasPermission('USUARIO', 'list')")
	@Operation(description = "Busca paginada de usuários. Retorna uma lista paginada de usuários com opções de filtragem e ordenação. O campo 'ordenarPor' requer os seguintes dados: código, nome, cpf, login, senha, email.")
	public ResponseEntity<Page<UsuarioDTO>> buscarPaginado(@RequestParam(value = "pagina", defaultValue = "0") Integer pagina, @RequestParam(value = "tamanho", defaultValue = "10") Integer tamanho, @RequestParam(value = "filtro", required = false) String filtro,
			@RequestParam(value = "ordenarPor", defaultValue = "codigo") String ordenarPor, @RequestParam(value = "direcao", defaultValue = "ASC", required = false) String direcao) {
		String ordenarEntity = UsuarioDTO.consultaPagable(ordenarPor);
		if (ordenarEntity == null) {
			return ResponseEntity.badRequest().body(Page.empty());
		}
		try {
			Page<UsuarioDTO> pageUsuarioDTO = userServImpl.findAll(filtro, Utils.consultaPage(ordenarEntity, direcao, pagina, tamanho));
			return ResponseEntity.ok(pageUsuarioDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/v1/{codigo}")
	@PreAuthorize("hasPermission('USUARIO', 'edit')")
	@Operation(description = "Atualizar um usuário existente. Atualiza os detalhes de um usuário com base no código fornecido.")
	public ResponseEntity<UsuarioDTO> editar(@Valid @PathVariable @NonNull Long codigo, @Valid @RequestBody @NonNull UsuarioModel entity) {
		try {
			UsuarioDTO usuarioDTO = userServImpl.saveUpdateEncodePassword(codigo, entity);
			return ResponseEntity.ok(usuarioDTO);
		} catch (UserNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (AdminUserModificationException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/v1/{codigo}")
	@PreAuthorize("hasPermission('USUARIO', 'delete')")
	@Operation(description = "Deletar um usuário pelo código. Remove um usuário específico com base no código fornecido.")
	public ResponseEntity<Void> deletar(@Valid @PathVariable @NonNull Long codigo) {
		try {
			userServImpl.delete(codigo);
			return ResponseEntity.noContent().build();
		} catch (UserNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (AdminUserModificationException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
