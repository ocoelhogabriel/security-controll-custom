package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.mapper;

import com.ocoelhogabriel.security_control_custom.domain.entity.UserDomain;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // Mappers para os objetos aninhados que também precisaremos criar.
    private final CompanyMapper companyMapper = new CompanyMapper();
    private final ProfileMapper profileMapper = new ProfileMapper();
    private final ScopeMapper scopeMapper = new ScopeMapper();

    /**
     * Converte uma entidade JPA (User) para um modelo de domínio (UserDomain). Usado ao ler dados do banco de dados.
     *
     * @param entity A entidade vinda da camada de persistência.
     * @return O modelo de domínio puro.
     */
    public UserDomain toDomain(User entity) {
        if (entity == null) {
            return null;
        }

        return new UserDomain(entity.getId(),
                entity.getCpf(),
                entity.getName(),
                entity.getLogin(),
                entity.getPassword(),
                entity.getEmail(),
                companyMapper.toDomain(entity.getEmpresa()),
                profileMapper.toDomain(entity.getPerfil()),
                scopeMapper.toDomain(entity.getAbrangencia()));
    }

    /**
     * Converte um modelo de domínio (UserDomain) para uma entidade JPA (User). Usado ao salvar ou atualizar dados no banco de dados.
     *
     * @param domain O modelo de domínio puro.
     * @return A entidade JPA pronta para persistência.
     */
    public User toEntity(UserDomain domain) {
        if (domain == null) {
            return null;
        }

        return new User(domain.getId(),
                domain.getCpf(),
                domain.getName(),
                domain.getLogin(),
                domain.getPassword(),
                domain.getEmail(),
                companyMapper.toEntity(domain.getCompanyDomain()),
                profileMapper.toEntity(domain.getProfileDomain()),
                scopeMapper.toEntity(domain.getScopeDomain()));
    }
}
