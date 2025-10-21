package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.mapper;

import com.ocoelhogabriel.security_control_custom.domain.entity.ProfileDomain;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Profile;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

    /**
     * Converte uma entidade JPA (Profile) para um modelo de domínio (ProfileDomain). Usado ao ler dados do banco de dados.
     *
     * @param entity A entidade vinda da camada de persistência.
     * @return O modelo de domínio puro.
     */
    public ProfileDomain toDomain(Profile entity) {
        if (entity == null) {
            return null;
        }

        return new ProfileDomain(entity.getId(), entity.getName(), entity.getDescription());
    }

    /**
     * Converte um modelo de domínio (ProfileDomain) para uma entidade JPA (Profile). Usado ao salvar ou atualizar dados no banco de dados.
     *
     * @param domain O modelo de domínio puro.
     * @return A entidade JPA pronta para persistência.
     */
    public Profile toEntity(ProfileDomain domain) {
        if (domain == null) {
            return null;
        }

        return new Profile(domain.getId(), domain.getName(), domain.getDescription());
    }
}
