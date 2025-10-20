package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.adapter;

import com.ocoelhogabriel.security_control_custom.application.dto.*;
import com.ocoelhogabriel.security_control_custom.application.usecase.IPerfilPermService;
import com.ocoelhogabriel.security_control_custom.infrastructure.exception.ResponseGlobalModel;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Permission;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Profile;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Resources;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository.PermissionJpaRepository;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository.ProfileJpaRepository;
import com.ocoelhogabriel.security_control_custom.infrastructure.utils.Utils;
import com.ocoelhogabriel.security_control_custom.infrastructure.utils.message.MessageResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PerfilPermissaoServiceImpl implements IPerfilPermService {

	@Autowired
	private PermissionJpaRepository permissionJpaRepository;

	@Autowired
	private RecursoServiceImpl recursoService;

	@Autowired
	private ProfileJpaRepository profileJpaRepository;

	@Override
	public ResponseEntity<PerfilPermissaoDTO> findById(@NonNull Long codigo) throws EntityNotFoundException, IOException {
		return MessageResponse.success(findByIdPerfil(codigo));
	}

	@Override
	public ResponseEntity<List<PerfilPermissaoDTO>> findAll() throws EntityNotFoundException, IOException {
		return MessageResponse.success(profileJpaRepository.findAll().stream().map(perfil -> {
			List<PermissaoDTO> permissoes = permissionJpaRepository.findByPerfil_percod(perfil.getId()).orElseGet(ArrayList::new).stream().map(PermissaoDTO::new).toList();
			return new PerfilPermissaoDTO(perfil, permissoes);
		}).toList());
	}

	@Override
	public ResponseEntity<Page<PerfilPermissaoDTO>> findAll(String nome, @NonNull Pageable pageable) throws EntityNotFoundException, IOException {
		Objects.requireNonNull(pageable, "Pageable do Perfil está nulo.");
		Specification<Profile> spec = Profile.filterByFields(nome);
		Page<Profile> result = profileJpaRepository.findAll(spec, pageable);
		return MessageResponse.success(result.map(perfil -> {
			List<PermissaoDTO> permissoes = permissionJpaRepository.findByPerfil_percod(perfil.getId()).orElseGet(ArrayList::new).stream().map(PermissaoDTO::new).toList();
			return new PerfilPermissaoDTO(perfil, permissoes);
		}));
	}

	@Override
	public ResponseEntity<PerfilPermissaoDTO> save(PerfilModel perModel) throws IOException {
		try {
			Objects.requireNonNull(perModel.getNome(), "Nome do Perfil está nulo.");
			Profile profile = createUpdatePerfil(new Profile(null, perModel.getNome().toUpperCase(), perModel.getDescricao()));

			List<PermissaoDTO> permissaoList = perModel.getPermissoes().stream().map(permissao -> saveEntityPermissao(profile, permissao)).toList();

			return MessageResponse.create(new PerfilPermissaoDTO(profile, permissaoList));
		} catch (Exception e) {
			throw new IOException("Erro ao criar um Perfil. " + e.getMessage(), e);
		}
	}

	@Override
	public ResponseEntity<PerfilPermissaoDTO> update(@NonNull Long codigo, PerfilModel perModel) throws EntityNotFoundException, IOException {
		Objects.requireNonNull(codigo, "Código do Perfil está nulo.");
		Objects.requireNonNull(perModel.getNome(), "Nome do Perfil está nulo.");

		Profile profile = findByIdPerfilEntity(codigo);
		profile.setName(perModel.getNome().toUpperCase());
		profile.setDescription(perModel.getDescricao());
		Profile profileUp = profileJpaRepository.save(profile);

		List<PermissaoDTO> permissaoList = perModel.getPermissoes().stream().map(permissao -> updateEntityPermissao(profileUp, permissao)).toList();

		return MessageResponse.success(new PerfilPermissaoDTO(profileUp, permissaoList));
	}

	@Override
	public ResponseEntity<Void> delete(@NonNull Long perfil) throws IOException {
		try {
			deleteEntityPermissao(perfil);
			profileJpaRepository.deleteById(perfil);
			return MessageResponse.noContent();
		} catch (IOException e) {
			throw new IOException("Erro ao apagar o perfil. Mensagem: " + e.getMessage(), e);
		}
	}

	public PerfilPermissaoDTO findByIdPerfil(Long codigo) throws EntityNotFoundException {
		Objects.requireNonNull(codigo, "Código do Perfil está nulo.");
		Profile profile = findByIdPerfilEntity(codigo);
		List<PermissaoDTO> permissoes = permissionJpaRepository.findByPerfil_percod(profile.getId()).orElseThrow(() -> new EntityNotFoundException("Permissões não encontradas para o perfil com código: " + codigo)).stream().map(PermissaoDTO::new).toList();
		return new PerfilPermissaoDTO(profile, permissoes);
	}

	public Profile findByIdPerfilEntity(@NonNull Long codigo) throws EntityNotFoundException {
		return profileJpaRepository.findById(codigo).orElse(null);
	}

	public Profile findByIdPerfilEntity(@NonNull String nome) {
		return profileJpaRepository.findByPernom(nome).orElse(null);
	}

	public Permission findByPerfilAndRecurso(Profile profile, Resources resources) {
		return permissionJpaRepository.findByPerfil_percodAndRecurso_recnom(profile.getId(), resources.getName()).orElse(null);
	}

	public PermissaoDTO findByIdApi(@NonNull Long codigo) throws EntityNotFoundException {
		Permission permission = findByEntity(codigo);
		return new PermissaoDTO(permission);
	}

	public Permission findByEntity(@NonNull Long codigo) {
		return permissionJpaRepository.findById(codigo).orElseThrow(() -> new EntityNotFoundException("Permissão não encontrada com o código: " + codigo));
	}

	public Profile createUpdatePerfil(@NonNull Profile profile) {
		return profileJpaRepository.save(profile);
	}

	public PermissaoDTO saveEntityPermissao(Profile profile, @NonNull PermissaoModel permissaoModel) {
		validatePermissaoFields(profile, permissaoModel);

		Resources resources = recursoService.findByIdEntity(permissaoModel.getRecurso().getNome());
		Permission permission = new Permission(null,
                profile,
                resources, permissaoModel.getListar(), permissaoModel.getBuscar(), permissaoModel.getCriar(), permissaoModel.getEditar(), permissaoModel.getDeletar());

		return new PermissaoDTO(permissionJpaRepository.save(permission));
	}

	public PermissaoDTO updateEntityPermissao(@NonNull Profile profile, @NonNull PermissaoModel permissaoModel) {
		validatePermissaoFields(profile, permissaoModel);

		Resources resources = recursoService.findByIdEntity(permissaoModel.getRecurso().getNome());
		Permission permissionExistente = findByPerfilAndRecurso(profile, resources);

		Permission permissionAtualizada = new Permission(permissionExistente.getId(),
                profile,
                resources, permissaoModel.getListar(), permissaoModel.getBuscar(), permissaoModel.getCriar(), permissaoModel.getEditar(), permissaoModel.getDeletar());

		return new PermissaoDTO(permissionJpaRepository.save(permissionAtualizada));
	}

	private void validatePermissaoFields(Profile profile, PermissaoModel permissaoModel) {
		Objects.requireNonNull(profile, "Perfil da Permissão está nulo.");
		Objects.requireNonNull(permissaoModel.getRecurso(), "Recurso da Permissão está nulo.");
		Objects.requireNonNull(permissaoModel.getCriar(), "Campo 'Criar' da Permissão está nulo.");
		Objects.requireNonNull(permissaoModel.getEditar(), "Campo 'Editar' da Permissão está nulo.");
		Objects.requireNonNull(permissaoModel.getDeletar(), "Campo 'Deletar' da Permissão está nulo.");
		Objects.requireNonNull(permissaoModel.getBuscar(), "Campo 'Buscar' da Permissão está nulo.");
		Objects.requireNonNull(permissaoModel.getListar(), "Campo 'Listar' da Permissão está nulo.");
	}

	public ResponseGlobalModel deleteEntityPermissao(@NonNull Long codigo) throws IOException {
		Objects.requireNonNull(codigo, "Código da Permissão está nulo.");
		try {
			permissionJpaRepository.deleteByPerfil_Percod(codigo);
			return Utils.responseMessageSucess("Apagado com Sucesso.");
		} catch (Exception e) {
			throw new IOException("Erro ao apagar as permissões. Mensagem: " + e.getMessage(), e);
		}
	}

	public RecursoDTO getNomeRecursoDTO(@NonNull String nome) throws EntityNotFoundException {
		return new RecursoDTO(recursoService.findByIdEntity(nome));
	}

}
