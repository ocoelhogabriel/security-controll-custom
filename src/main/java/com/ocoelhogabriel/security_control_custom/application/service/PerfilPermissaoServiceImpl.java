package com.ocoelhogabriel.security_control_custom.application.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.ocoelhogabriel.security_control_custom.domain.entity.PermissionDomain;
import com.ocoelhogabriel.security_control_custom.domain.entity.ProfileDomain;
import com.ocoelhogabriel.security_control_custom.domain.entity.ResourcesDomain;
import com.ocoelhogabriel.security_control_custom.domain.repository.PermissionRepository;
import com.ocoelhogabriel.security_control_custom.domain.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.ocoelhogabriel.security_control_custom.infrastructure.exception.ResponseGlobalModel;
import com.ocoelhogabriel.security_control_custom.application.dto.PerfilModel;
import com.ocoelhogabriel.security_control_custom.application.dto.PermissaoModel;
import com.ocoelhogabriel.security_control_custom.application.dto.PerfilPermissaoDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.PermissaoDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.RecursoDTO;
import com.ocoelhogabriel.security_control_custom.domain.service.IPerfilPermService;
import com.ocoelhogabriel.security_control_custom.infrastructure.utils.Utils;
import com.ocoelhogabriel.security_control_custom.infrastructure.utils.message.MessageResponse;
import jakarta.persistence.EntityNotFoundException;

@Service
public class PerfilPermissaoServiceImpl implements IPerfilPermService {

    @Autowired
    private PermissionRepository permissionRepository; // Substituído PermissionJpaRepository por PermissionRepository

    @Autowired
    private RecursoServiceImpl recursoService;

    @Autowired
    private ProfileRepository profileRepository; // Substituído ProfileJpaRepository por ProfileRepository

    @Override
    public ResponseEntity<PerfilPermissaoDTO> findById(@NonNull Long codigo) throws EntityNotFoundException, IOException {
        return MessageResponse.success(findByIdPerfil(codigo));
    }

    @Override
    public ResponseEntity<List<PerfilPermissaoDTO>> findAll() throws EntityNotFoundException, IOException {
        return MessageResponse.success(profileRepository.findAll().stream().map(profileDomain -> {
            List<PermissaoDTO> permissoes = permissionRepository.findByProfileId(profileDomain.getId())
                    .orElseGet(ArrayList::new)
                    .stream()
                    .map(PermissaoDTO::new)
                    .toList();
            return new PerfilPermissaoDTO(profileDomain, permissoes);
        }).toList());
    }

    @Override
    public ResponseEntity<Page<PerfilPermissaoDTO>> findAll(String nome, @NonNull Pageable pageable) throws EntityNotFoundException, IOException {
        Objects.requireNonNull(pageable, "Pageable do Perfil está nulo.");
        Page<ProfileDomain> result = profileRepository.findByNameLike(nome, pageable); // Usando findByNameLike
        return MessageResponse.success(result.map(profileDomain -> {
            List<PermissaoDTO> permissoes = permissionRepository.findByProfileId(profileDomain.getId())
                    .orElseGet(ArrayList::new)
                    .stream()
                    .map(PermissaoDTO::new)
                    .toList();
            return new PerfilPermissaoDTO(profileDomain, permissoes);
        }));
    }

    @Override
    public ResponseEntity<PerfilPermissaoDTO> save(PerfilModel perModel) throws IOException {
        try {
            Objects.requireNonNull(perModel.getNome(), "Nome do Perfil está nulo.");
            ProfileDomain profileDomain = createUpdatePerfil(new ProfileDomain(null, perModel.getNome().toUpperCase(), perModel.getDescricao()));

            List<PermissaoDTO> permissaoList = perModel.getPermissoes()
                    .stream()
                    .map(permissao -> saveEntityPermissao(profileDomain, permissao))
                    .toList();

            return MessageResponse.create(new PerfilPermissaoDTO(profileDomain, permissaoList));
        } catch (Exception e) {
            throw new IOException("Erro ao criar um Perfil. " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<PerfilPermissaoDTO> update(@NonNull Long codigo, PerfilModel perModel) throws EntityNotFoundException, IOException {
        Objects.requireNonNull(codigo, "Código do Perfil está nulo.");
        Objects.requireNonNull(perModel.getNome(), "Nome do Perfil está nulo.");

        ProfileDomain profileDomain = findByIdPerfilEntity(codigo);
        profileDomain.setName(perModel.getNome().toUpperCase());
        profileDomain.setDescription(perModel.getDescricao());
        ProfileDomain profileUp = profileRepository.save(profileDomain);

        List<PermissaoDTO> permissaoList = perModel.getPermissoes().stream().map(permissao -> updateEntityPermissao(profileUp, permissao)).toList();

        return MessageResponse.success(new PerfilPermissaoDTO(profileUp, permissaoList));
    }

    @Override
    public ResponseEntity<Void> delete(@NonNull Long perfil) throws IOException {
        try {
            deleteEntityPermissao(perfil);
            profileRepository.deleteById(perfil);
            return MessageResponse.noContent();
        } catch (IOException e) {
            throw new IOException("Erro ao apagar o perfil. Mensagem: " + e.getMessage(), e);
        }
    }

    public PerfilPermissaoDTO findByIdPerfil(Long codigo) throws EntityNotFoundException {
        Objects.requireNonNull(codigo, "Código do Perfil está nulo.");
        ProfileDomain profileDomain = findByIdPerfilEntity(codigo);
        List<PermissaoDTO> permissoes = permissionRepository.findByProfileId(profileDomain.getId())
                .orElseThrow(() -> new EntityNotFoundException("Permissões não encontradas para o perfil com código: " + codigo))
                .stream()
                .map(PermissaoDTO::new)
                .toList();
        return new PerfilPermissaoDTO(profileDomain, permissoes);
    }

    public ProfileDomain findByIdPerfilEntity(@NonNull Long codigo) throws EntityNotFoundException {
        return profileRepository.findById(codigo).orElse(null);
    }

    public ProfileDomain findByIdPerfilEntity(@NonNull String nome) {
        return profileRepository.findByName(nome).orElse(null);
    }

    public PermissionDomain findByPerfilAndRecurso(ProfileDomain profileDomain, ResourcesDomain resourcesDomain) {
        return permissionRepository.findByProfileIdAndResourceName(profileDomain.getId(), resourcesDomain.getName()).orElse(null);
    }

    public PermissaoDTO findByIdApi(@NonNull Long codigo) throws EntityNotFoundException {
        PermissionDomain permissionDomain = findByEntity(codigo);
        return new PermissaoDTO(permissionDomain);
    }

    public PermissionDomain findByEntity(@NonNull Long codigo) {
        return permissionRepository.findById(codigo)
                .orElseThrow(() -> new EntityNotFoundException("Permissão não encontrada com o código: " + codigo));
    }

    public ProfileDomain createUpdatePerfil(@NonNull ProfileDomain profileDomain) {
        return profileRepository.save(profileDomain);
    }

    public PermissaoDTO saveEntityPermissao(ProfileDomain profileDomain, @NonNull PermissaoModel permissaoModel) {
        validatePermissaoFields(profileDomain, permissaoModel);

        ResourcesDomain resourcesDomain = recursoService.findByIdEntity(permissaoModel.getRecurso().getNome());
        PermissionDomain permissionDomain = new PermissionDomain(null,
                profileDomain,
                resourcesDomain,
                permissaoModel.getListar(),
                permissaoModel.getBuscar(),
                permissaoModel.getCriar(),
                permissaoModel.getEditar(),
                permissaoModel.getDeletar());

        return new PermissaoDTO(permissionRepository.save(permissionDomain));
    }

    public PermissaoDTO updateEntityPermissao(@NonNull ProfileDomain profileDomain, @NonNull PermissaoModel permissaoModel) {
        validatePermissaoFields(profileDomain, permissaoModel);

        ResourcesDomain resourcesDomain = recursoService.findByIdEntity(permissaoModel.getRecurso().getNome());
        PermissionDomain permissionExistente = findByPerfilAndRecurso(profileDomain, resourcesDomain);

        PermissionDomain permissionAtualizada = new PermissionDomain(permissionExistente.getId(),
                profileDomain,
                resourcesDomain,
                permissaoModel.getListar(),
                permissaoModel.getBuscar(),
                permissaoModel.getCriar(),
                permissaoModel.getEditar(),
                permissaoModel.getDeletar());

        return new PermissaoDTO(permissionRepository.save(permissionAtualizada));
    }

    private void validatePermissaoFields(ProfileDomain profileDomain, PermissaoModel permissaoModel) {
        Objects.requireNonNull(profileDomain, "Perfil da Permissão está nulo.");
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
            permissionRepository.deleteByProfileId(codigo);
            return Utils.responseMessageSucess("Apagado com Sucesso.");
        } catch (Exception e) {
            throw new IOException("Erro ao apagar as permissões. Mensagem: " + e.getMessage(), e);
        }
    }

    public RecursoDTO getNomeRecursoDTO(@NonNull String nome) throws EntityNotFoundException {
        return new RecursoDTO(recursoService.findByIdEntity(nome));
    }

    public boolean checkPermission(String profileName, String resourceName, String action) {
        ProfileDomain profile = profileRepository.findByName(profileName).orElse(null);
        if (profile == null) {
            return false;
        }

        ResourcesDomain resource = recursoService.findByIdEntity(resourceName);
        if (resource == null) {
            return false;
        }

        Optional<PermissionDomain> permissionOptional = permissionRepository.findByProfileIdAndResourceName(profile.getId(), resource.getName());

        if (permissionOptional.isEmpty()) {
            return false;
        }

        PermissionDomain permission = permissionOptional.get();

        return switch (action.toLowerCase()) {
            case "list" -> permission.getList();
            case "find" -> permission.getFind();
            case "create" -> permission.getCreate();
            case "edit" -> permission.getEdit();
            case "delete" -> permission.getDelete();
            default -> false;
        };
    }
}
