package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.mapper;

import com.ocoelhogabriel.security_control_custom.domain.entity.CompanyDomain;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Company;

public class CompanyMapper {

    /**
     * Converte uma entidade JPA (Company) para um modelo de domínio (CompanyDomain). Usado ao ler dados do banco de dados.
     *
     * @param entity A entidade vinda da camada de persistência.
     * @return O modelo de domínio puro.
     */
    public CompanyDomain toDomain(Company entity) {
        if (entity == null) {
            return null;
        }

        return new CompanyDomain(entity.getId(), entity.getDocument(), entity.getName(), entity.getTradeName(), entity.getContact());
    }

    /**
     * Converte um modelo de domínio (CompanyDomain) para uma entidade JPA (Company). Usado ao salvar ou atualizar dados no banco de dados.
     *
     * @param domain O modelo de domínio puro.
     * @return A entidade JPA pronta para persistência.
     */
    public Company toEntity(CompanyDomain domain) {
        if (domain == null) {
            return null;
        }

        return new Company(domain.getId(), domain.getName(), domain.getTradeName(), domain.getContact());
    }
}
