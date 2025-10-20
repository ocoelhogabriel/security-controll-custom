package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.mapper;

import com.ocoelhogabriel.security_control_custom.domain.entity.ScopeDomain;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Scope;

public class ScopeMapper {

    /**
     * Converte uma entidade JPA (Scope) para um modelo de domínio (ScopeDomain). Usado ao ler dados do banco de dados.
     *
     * @param entity A entidade vinda da camada de persistência.
     * @return O modelo de domínio puro.
     */
    public ScopeDomain toDomain(Scope entity) {
        if (entity == null) {
            return null;
        }

        return new ScopeDomain(entity.getId(), entity.getName(), entity.getDescription());
    }

    /**
     * Converte um modelo de domínio (ScopeDomain) para uma entidade JPA (Scope). Usado ao salvar ou atualizar dados no banco de dados.
     *
     * @param domain O modelo de domínio puro.
     * @return A entidade JPA pronta para persistência.
     */
    public Scope toEntity(ScopeDomain domain) {
        if (domain == null) {
            return null;
        }

        return new Scope(domain.getId(), domain.getName(), domain.getDescription());
    }
}
