package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.adapter;

import com.ocoelhogabriel.security_control_custom.application.dto.AbrangenciaDTO;
import com.ocoelhogabriel.security_control_custom.application.dto.AbrangenciaDetalhesModel;
import com.ocoelhogabriel.security_control_custom.application.dto.ItensAbrangentes;
import com.ocoelhogabriel.security_control_custom.application.usecase.IAbrangenciaService;
import com.ocoelhogabriel.security_control_custom.application.usecase.IEmpresaService;
import com.ocoelhogabriel.security_control_custom.application.usecase.IPlantaService;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Resources;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Scope;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.ScopeDetails;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository.ScopeDetailsJpaRepository;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.repository.ScopeJpaRepository;
import com.ocoelhogabriel.security_control_custom.infrastructure.utils.JsonNodeConverter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NonUniqueResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class AbrangenciaServiceImpl{

    private static final Logger log = LoggerFactory.getLogger(AbrangenciaServiceImpl.class);

    @Autowired
    private ScopeJpaRepository scopeJpaRepository;
    @Autowired
    private ScopeDetailsJpaRepository abrangenciaDetalhesRepository;
    @Autowired
    private RecursoServiceImpl recursoService;
    @Autowired
    private IEmpresaService empresaService;
    @Autowired
    private IPlantaService IPlantaService;

    public ScopeDetails findByAbrangenciaAndRecursoContaining(Scope codigo, Resources nome) {
        List<ScopeDetails> results = abrangenciaDetalhesRepository.findByAbrangencia_abrcodAndRecurso_recnomContaining(codigo.getId(),
                nome.getName());

        if (results.size() > 1) {
            throw new NonUniqueResultException("Query did not return a unique result: " + results.size() + " results were returned");
        }

        return results.isEmpty() ? null : results.get(0);
    }

    public ScopeDetails findByAbrangenciaAndRecursoContainingAbrangencia(Scope codigo, Resources nome) {
        List<ScopeDetails> results = abrangenciaDetalhesRepository.findByAbrangencia_abrcodAndRecurso_recnomContaining(codigo.getId(),
                nome.getName());
        if (results.isEmpty())
            return null;
        return results.get(0);
    }

    public Scope findByIdEntity(@NonNull Long codigo) throws EntityNotFoundException {
        return scopeJpaRepository.findById(codigo).orElse(null);
    }

    public Scope findByIdEntity(String nome) {
        return scopeJpaRepository.findByAbrnomLike(nome).orElse(null);
    }

    public AbrangenciaDTO findByIdSimples(@NonNull Long codigo) throws EntityNotFoundException {
        Scope scope = findByIdEntity(codigo);
        return new AbrangenciaDTO(scope);
    }

    public ItensAbrangentes findByItemAbrangenceEntity() throws IOException {
        var empresaList = empresaService.sendListAbrangenciaEmpresaDTO();
        var planta = IPlantaService.sendListAbrangenciaPlantaDTO();

        return new ItensAbrangentes(empresaList, planta);
    }

    public ScopeDetails saveOrUpdateAbrangenciaDetalhes(Scope scope, AbrangenciaDetalhesModel recurso) {
        String recursoNome = Objects.requireNonNull(recurso.getRecurso().getNome(), "Nome do recurso está nulo.");
        Objects.requireNonNull(recurso.getHierarquia(), "Valor da Hierarquia está nulo.");

        Resources resourcesEntity = recursoService.findByIdEntity(recursoNome);

        ScopeDetails detalhesOpt = findByAbrangenciaAndRecursoContaining(scope, resourcesEntity);

        JsonNodeConverter jsonNode = new JsonNodeConverter();
        ScopeDetails detalhes = new ScopeDetails(detalhesOpt == null ? null : detalhesOpt.getId(),
                scope,
                resourcesEntity,
                recurso.getHierarquia(),
                jsonNode.convertToDatabaseColumn(recurso.getDados()));

        return abrangenciaDetalhesRepository.save(detalhes);
    }

    public ScopeDetails saveOrUpdateAbrangenciaDetalhes(Scope scope, ScopeDetails detalhes) {
        Objects.requireNonNull(scope, "Abrangência está nula.");
        Objects.requireNonNull(detalhes, "Detalhes estão nulos.");
        Objects.requireNonNull(detalhes.getResource(), "Recurso está nulo.");
        Objects.requireNonNull(detalhes.getHierarchy(), "Hierarquia está nula.");
        Objects.requireNonNull(detalhes.getAbddat(), "Dados estão nulos.");

        Resources resources = detalhes.getResource();

        ScopeDetails detalhesOpt = findByAbrangenciaAndRecursoContaining(scope, resources);

        ScopeDetails scopeDetails = new ScopeDetails(detalhesOpt == null ? null : detalhesOpt.getId(),
                scope,
                resources,
                detalhes.getHierarchy(),
                detalhes.getAbddat());

        return abrangenciaDetalhesRepository.save(scopeDetails);
    }

    public Scope createUpdateAbrangencia(Scope scope) {
        return scopeJpaRepository.save(scope);
    }
}
