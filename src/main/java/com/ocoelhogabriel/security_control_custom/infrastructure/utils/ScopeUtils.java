package com.ocoelhogabriel.security_control_custom.infrastructure.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ocoelhogabriel.security_control_custom.domain.entity.ResourcesDomain;
import com.ocoelhogabriel.security_control_custom.domain.entity.ScopeDetailsDomain;
import com.ocoelhogabriel.security_control_custom.domain.entity.UserDomain;
import com.ocoelhogabriel.security_control_custom.domain.repository.ResourcesRepository;
import com.ocoelhogabriel.security_control_custom.domain.repository.ScopeDetailsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ScopeUtils {

    private static final Logger log = LoggerFactory.getLogger(ScopeUtils.class);

    /**
     * Extrai os filtros de abrangência para um dado recurso e usuário.
     *
     * @param authenticatedUser      Optional<UserDomain> do usuário autenticado.
     * @param resourceName           O nome do recurso (ex: "USUARIO", "EMPRESA").
     * @param resourcesRepository    O repositório de recursos.
     * @param scopeDetailsRepository O repositório de detalhes de abrangência.
     * @param gson                   A instância do Gson para parsing de JSON.
     * @return Um mapa de filtros (ex: {"companyId": 1, "plantId": [1, 2]}).
     */
    public static Map<String, Object> getScopeFilters(
            Optional<UserDomain> authenticatedUser,
            String resourceName,
            ResourcesRepository resourcesRepository,
            ScopeDetailsRepository scopeDetailsRepository,
            Gson gson) {

        if (authenticatedUser.isEmpty() || authenticatedUser.get().getScopeDomain() == null || resourceName == null) {
            return Collections.emptyMap();
        }

        UserDomain user = authenticatedUser.get();

        ResourcesDomain resource = resourcesRepository.findByName(resourceName)
                .orElseThrow(() -> new EntityNotFoundException("Recurso não encontrado: " + resourceName));

        Optional<ScopeDetailsDomain> scopeDetailsOptional = scopeDetailsRepository.findByScopeIdAndResourceId(user.getScopeDomain().getId(),
                resource.getId());

        if (scopeDetailsOptional.isEmpty()) {
            return Collections.emptyMap();
        }

        ScopeDetailsDomain scopeDetails = scopeDetailsOptional.get();
        String abddatJson = scopeDetails.getData();

        if (abddatJson == null || abddatJson.isEmpty() || abddatJson.equals("[]")) {
            return Collections.emptyMap();
        }

        try {
            Type type = new TypeToken<Map<String, Object>>() {
            }.getType();
            return gson.fromJson(abddatJson, type);
        } catch (Exception e) {
            log.error("Erro ao parsear abddat JSON para filtros de abrangência: {}", abddatJson, e);
            return Collections.emptyMap();
        }
    }
}
