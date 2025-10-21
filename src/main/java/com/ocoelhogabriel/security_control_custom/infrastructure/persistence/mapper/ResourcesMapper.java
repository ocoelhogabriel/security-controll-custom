package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.mapper;

import com.ocoelhogabriel.security_control_custom.domain.entity.ResourcesDomain;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Resources;
import org.springframework.stereotype.Component;

@Component
public class ResourcesMapper {

    /**
     * Converte uma entidade JPA (Resources) para um modelo de domínio (ResourcesDomain). Usado ao ler dados do banco de dados.
     *
     * @param entity A entidade vinda da camada de persistência.
     * @return O modelo de domínio puro.
     */
    public ResourcesDomain toDomain(Resources entity) {
        if (entity == null) {
            return null;
        }

        return new ResourcesDomain(entity.getId(), entity.getName(), entity.getDescription());
    }

    /**
     * Converte um modelo de domínio (ResourcesDomain) para uma entidade JPA (Resources). Usado ao salvar ou atualizar dados no banco de dados.
     *
     * @param domain O modelo de domínio puro.
     * @return A entidade JPA pronta para persistência.
     */
    public Resources toEntity(ResourcesDomain domain) {
        if (domain == null) {
            return null;
        }

        return new Resources(domain.getId(), domain.getName(), domain.getDescription());
    }
}
