package com.ocoelhogabriel.security_control_custom.application.service;

import com.ocoelhogabriel.security_control_custom.application.dto.PerfilModel;
import com.ocoelhogabriel.security_control_custom.application.dto.PerfilPermissaoDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.PermissaoDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.PermissaoModel;
import com.ocoelhogabriel.security_control_custom.application.exception.ProfileNotFoundException;
import com.ocoelhogabriel.security_control_custom.application.port.in.IPerfilPermService;
import com.ocoelhogabriel.security_control_custom.domain.entity.PermissionDomain;
import com.ocoelhogabriel.security_control_custom.domain.entity.ProfileDomain;
import com.ocoelhogabriel.security_control_custom.domain.entity.ResourcesDomain;
import com.ocoelhogabriel.security_control_custom.domain.repository.PermissionRepository;
import com.ocoelhogabriel.security_control_custom.domain.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PerfilPermissaoServiceImpl implements IPerfilPermService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RecursoServiceImpl recursoService; // Assumindo que este serviço é simples ou também será refatorado

    @Autowired
    private ProfileRepository profileRepository;

    @Override
    @Transactional(readOnly = true)
    public PerfilPermissaoDTO findById(@NonNull Long codigo) {
        ProfileDomain profileDomain = findProfileByIdOrThrow(codigo);
        List<PermissaoDTO> permissoes = findPermissionsForProfile(profileDomain.getId());
        return new PerfilPermissaoDTO(profileDomain, permissoes);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PerfilPermissaoDTO> findAll() {
        return profileRepository.findAll().stream()
                .map(profileDomain -> {
                    List<PermissaoDTO> permissoes = findPermissionsForProfile(profileDomain.getId());
                    return new PerfilPermissaoDTO(profileDomain, permissoes);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PerfilPermissaoDTO> findAll(String nome, @NonNull Pageable pageable) {
        Objects.requireNonNull(pageable, "Pageable do Perfil está nulo.");
        Page<ProfileDomain> result = profileRepository.findByNameLike(nome, pageable);
        return result.map(profileDomain -> {
            List<PermissaoDTO> permissoes = findPermissionsForProfile(profileDomain.getId());
            return new PerfilPermissaoDTO(profileDomain, permissoes);
        });
    }

    @Override
    @Transactional
    public PerfilPermissaoDTO save(PerfilModel perModel) {
        Objects.requireNonNull(perModel.getNome(), "Nome do Perfil está nulo.");

        ProfileDomain profileDomain = new ProfileDomain(null, perModel.getNome().toUpperCase(), perModel.getDescricao());
        ProfileDomain savedProfile = profileRepository.save(profileDomain);

        List<PermissaoDTO> permissaoList = perModel.getPermissoes()
                .stream()
                .map(permissaoModel -> savePermission(savedProfile, permissaoModel))
                .toList();

        return new PerfilPermissaoDTO(savedProfile, permissaoList);
    }

    @Override
    @Transactional
    public PerfilPermissaoDTO update(@NonNull Long codigo, PerfilModel perModel) {
        Objects.requireNonNull(perModel.getNome(), "Nome do Perfil está nulo.");

        ProfileDomain profileDomain = findProfileByIdOrThrow(codigo);
        profileDomain.setName(perModel.getNome().toUpperCase());
        profileDomain.setDescription(perModel.getDescricao());
        ProfileDomain updatedProfile = profileRepository.save(profileDomain);

        List<PermissaoDTO> permissaoList = perModel.getPermissoes()
                .stream()
                .map(permissaoModel -> updatePermission(updatedProfile, permissaoModel))
                .toList();

        return new PerfilPermissaoDTO(updatedProfile, permissaoList);
    }

    @Override
    @Transactional
    public void delete(@NonNull Long codigo) {
        if (!profileRepository.existsById(codigo)) {
            throw new ProfileNotFoundException("Perfil não encontrado com o código: " + codigo);
        }
        permissionRepository.deleteByProfileId(codigo);
        profileRepository.deleteById(codigo);
    }

    @Transactional(readOnly = true)
    public boolean checkPermission(String profileName, String resourceName, String action) {
        Optional<ProfileDomain> profileOpt = profileRepository.findByName(profileName);
        if (profileOpt.isEmpty()) {
            return false;
        }

        ResourcesDomain resource = recursoService.findByIdEntity(resourceName);
        if (resource == null) {
            return false;
        }

        Optional<PermissionDomain> permissionOptional = permissionRepository.findByProfileIdAndResourceName(profileOpt.get().getId(), resource.getName());

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

    private ProfileDomain findProfileByIdOrThrow(@NonNull Long codigo) {
        return profileRepository.findById(codigo)
                .orElseThrow(() -> new ProfileNotFoundException("Perfil não encontrado com o código: " + codigo));
    }

    private List<PermissaoDTO> findPermissionsForProfile(Long profileId) {
        return permissionRepository.findByProfileId(profileId)
                .orElseGet(ArrayList::new)
                .stream()
                .map(PermissaoDTO::new)
                .toList();
    }

    private PermissaoDTO savePermission(ProfileDomain profileDomain, @NonNull PermissaoModel permissaoModel) {
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

    private PermissaoDTO updatePermission(@NonNull ProfileDomain profileDomain, @NonNull PermissaoModel permissaoModel) {
        validatePermissaoFields(profileDomain, permissaoModel);

        ResourcesDomain resourcesDomain = recursoService.findByIdEntity(permissaoModel.getRecurso().getNome());
        PermissionDomain existingPermission = permissionRepository.findByProfileIdAndResourceName(profileDomain.getId(), resourcesDomain.getName())
                .orElse(new PermissionDomain());

        PermissionDomain permissionToUpdate = new PermissionDomain(existingPermission.getId(),
                profileDomain,
                resourcesDomain,
                permissaoModel.getListar(),
                permissaoModel.getBuscar(),
                permissaoModel.getCriar(),
                permissaoModel.getEditar(),
                permissaoModel.getDeletar());

        return new PermissaoDTO(permissionRepository.save(permissionToUpdate));
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
}
