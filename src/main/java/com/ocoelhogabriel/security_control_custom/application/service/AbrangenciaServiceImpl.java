package com.ocoelhogabriel.security_control_custom.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.ocoelhogabriel.security_control_custom.application.dto.AbrangenciaDetalhesDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.AbrangenciaDetalhesModel;
import com.ocoelhogabriel.security_control_custom.application.dto.AbrangenciaListaDetalhesDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.AbrangenciaModel;
import com.ocoelhogabriel.security_control_custom.application.exception.ScopeNotFoundException;
import com.ocoelhogabriel.security_control_custom.application.port.in.IAbrangenciaService;
import com.ocoelhogabriel.security_control_custom.domain.entity.ResourcesDomain;
import com.ocoelhogabriel.security_control_custom.domain.entity.ScopeDetailsDomain;
import com.ocoelhogabriel.security_control_custom.domain.entity.ScopeDomain;
import com.ocoelhogabriel.security_control_custom.domain.repository.ResourcesRepository;
import com.ocoelhogabriel.security_control_custom.domain.repository.ScopeDetailsRepository;
import com.ocoelhogabriel.security_control_custom.domain.repository.ScopeRepository;
import com.ocoelhogabriel.security_control_custom.infrastructure.utils.JsonNodeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AbrangenciaServiceImpl implements IAbrangenciaService {

    @Autowired
    private ScopeRepository scopeRepository;
    @Autowired
    private ScopeDetailsRepository scopeDetailsRepository;
    @Autowired
    private ResourcesRepository resourcesRepository;

    @Override
    @Transactional
    public AbrangenciaListaDetalhesDTO update(@NonNull Long codigo, AbrangenciaModel abrangenciaModel) {
        ScopeDomain existingScope = findScopeByIdOrThrow(codigo);

        existingScope.setName(abrangenciaModel.getNome());
        existingScope.setDescription(abrangenciaModel.getDescricao());

        ScopeDomain updatedScope = scopeRepository.save(existingScope);

        List<AbrangenciaDetalhesDTO> abrangenciaDetalhesDTOList = abrangenciaModel.getRecursos() == null ? List.of()
                : abrangenciaModel.getRecursos().stream()
                .map(recurso -> new AbrangenciaDetalhesDTO(saveOrUpdateAbrangenciaDetalhes(updatedScope, recurso)))
                .collect(Collectors.toList());

        List<Long> currentDetailIds = abrangenciaDetalhesDTOList.stream()
                .map(AbrangenciaDetalhesDTO::getCodigo)
                .filter(Objects::nonNull)
                .toList();

        scopeDetailsRepository.findByScopeId(updatedScope.getId()).stream()
                .filter(detail -> !currentDetailIds.contains(detail.getId()))
                .forEach(detail -> scopeDetailsRepository.deleteById(detail.getId()));

        return new AbrangenciaListaDetalhesDTO(updatedScope, abrangenciaDetalhesDTOList);
    }

    @Override
    @Transactional
    public AbrangenciaListaDetalhesDTO save(AbrangenciaModel abrangenciaModel) {
        Objects.requireNonNull(abrangenciaModel.getNome(), "Nome da Abrangência está nulo.");
        Objects.requireNonNull(abrangenciaModel.getDescricao(), "Descrição da Abrangência está nula.");

        ScopeDomain scope = scopeRepository.save(new ScopeDomain(null, abrangenciaModel.getNome(), abrangenciaModel.getDescricao()));

        List<AbrangenciaDetalhesDTO> detalhes = abrangenciaModel.getRecursos() == null ? List.of()
                : abrangenciaModel.getRecursos().stream()
                .map(recurso -> new AbrangenciaDetalhesDTO(saveOrUpdateAbrangenciaDetalhes(scope, recurso)))
                .collect(Collectors.toList());

        return new AbrangenciaListaDetalhesDTO(scope, detalhes);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AbrangenciaListaDetalhesDTO> findAll() {
        return scopeRepository.findAll().stream()
                .map(this::mapToAbrangenciaListaDetalhesDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AbrangenciaListaDetalhesDTO findById(@NonNull Long codigo) {
        ScopeDomain scope = findScopeByIdOrThrow(codigo);
        return mapToAbrangenciaListaDetalhesDTO(scope);
    }

    @Override
    @Transactional
    public void delete(@NonNull Long codigo) {
        if (!scopeRepository.existsById(codigo)) {
            throw new ScopeNotFoundException("Abrangência não encontrada com o código: " + codigo);
        }
        scopeDetailsRepository.deleteByScopeId(codigo);
        scopeRepository.deleteById(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AbrangenciaListaDetalhesDTO> findAll(String nome, Pageable pageable) {
        Objects.requireNonNull(pageable, "Pageable da Abrangência está nulo.");

        Page<ScopeDomain> result = (nome == null || nome.isEmpty())
                ? scopeRepository.findAll(pageable)
                : scopeRepository.findByNameLike(nome, pageable);

        return result.map(this::mapToAbrangenciaListaDetalhesDTO);
    }

    private AbrangenciaListaDetalhesDTO mapToAbrangenciaListaDetalhesDTO(ScopeDomain abrangencia) {
        List<AbrangenciaDetalhesDTO> detailsDTOList = scopeDetailsRepository.findByScopeId(abrangencia.getId()).stream()
                .map(AbrangenciaDetalhesDTO::new)
                .collect(Collectors.toList());
        return new AbrangenciaListaDetalhesDTO(abrangencia, detailsDTOList);
    }

    private ScopeDomain findScopeByIdOrThrow(@NonNull Long codigo) {
        return scopeRepository.findById(codigo)
                .orElseThrow(() -> new ScopeNotFoundException("Abrangência não encontrada com o código: " + codigo));
    }

    private ScopeDetailsDomain saveOrUpdateAbrangenciaDetalhes(ScopeDomain scopeDomain, AbrangenciaDetalhesModel recurso) {
        String recursoNome = Objects.requireNonNull(recurso.getRecurso().getNome(), "Nome do recurso está nulo.");
        Objects.requireNonNull(recurso.getHierarquia(), "Valor da Hierarquia está nulo.");

        ResourcesDomain resourcesDomain = resourcesRepository.findByName(recursoNome)
                .orElseThrow(() -> new ScopeNotFoundException("Recurso não encontrado: " + recursoNome));

        ScopeDetailsDomain detalhesOpt = scopeDetailsRepository.findByScopeIdAndResourceName(scopeDomain.getId(), resourcesDomain.getName()).orElse(null);

        JsonNodeConverter jsonNodeConverter = new JsonNodeConverter();
        JsonNode dataNode = jsonNodeConverter.convertToDatabaseColumn(recurso.getDados());

        ScopeDetailsDomain detalhes = new ScopeDetailsDomain(
                detalhesOpt == null ? null : detalhesOpt.getId(),
                scopeDomain,
                resourcesDomain,
                recurso.getHierarquia(),
                dataNode
        );

        return scopeDetailsRepository.save(detalhes);
    }
}
