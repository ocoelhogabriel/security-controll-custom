package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.mapper;

import com.ocoelhogabriel.security_control_custom.domain.entity.ScopeDetailsDomain;
import com.ocoelhogabriel.security_control_custom.domain.entity.ScopeDomain;
import com.ocoelhogabriel.security_control_custom.domain.entity.ResourcesDomain;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.ScopeDetails;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Scope;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Resources;
import org.springframework.stereotype.Component;

@Component
public class ScopeDetailsMapper {

    private final ScopeMapper scopeMapper;
    private final ResourcesMapper resourcesMapper;

    public ScopeDetailsMapper(ScopeMapper scopeMapper, ResourcesMapper resourcesMapper) {
        this.scopeMapper = scopeMapper;
        this.resourcesMapper = resourcesMapper;
    }

    public ScopeDetailsDomain toDomain(ScopeDetails entity) {
        if (entity == null) {
            return null;
        }
        ScopeDomain scopeDomain = scopeMapper.toDomain(entity.getScope());
        ResourcesDomain resourcesDomain = resourcesMapper.toDomain(entity.getResource());
        return new ScopeDetailsDomain(entity.getId(), scopeDomain, resourcesDomain, entity.getHierarchy(), entity.getAbddat());
    }

    public ScopeDetails toEntity(ScopeDetailsDomain domain) {
        if (domain == null) {
            return null;
        }
        Scope scope = scopeMapper.toEntity(domain.getScopeDomain());
        Resources resources = resourcesMapper.toEntity(domain.getResource());
        return new ScopeDetails(domain.getId(), scope, resources, domain.getHierarchy(), domain.getData());
    }
}
