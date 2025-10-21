package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.mapper;

import com.ocoelhogabriel.security_control_custom.domain.entity.PermissionDomain;
import com.ocoelhogabriel.security_control_custom.domain.entity.ProfileDomain;
import com.ocoelhogabriel.security_control_custom.domain.entity.ResourcesDomain;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Permission;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Profile;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Resources;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper {

    private final ProfileMapper profileMapper;
    private final ResourcesMapper resourcesMapper;

    public PermissionMapper(ProfileMapper profileMapper, ResourcesMapper resourcesMapper) {
        this.profileMapper = profileMapper;
        this.resourcesMapper = resourcesMapper;
    }

    public PermissionDomain toDomain(Permission entity) {
        if (entity == null) {
            return null;
        }
        ProfileDomain profileDomain = profileMapper.toDomain(entity.getPerfil());
        ResourcesDomain resourcesDomain = resourcesMapper.toDomain(entity.getResource());
        return new PermissionDomain(entity.getId(), profileDomain, resourcesDomain, entity.getList(), entity.getFind(), entity.getCreate(), entity.getEdit(), entity.getDelete());
    }

    public Permission toEntity(PermissionDomain domain) {
        if (domain == null) {
            return null;
        }
        Profile profile = profileMapper.toEntity(domain.getProfileDomain());
        Resources resources = resourcesMapper.toEntity(domain.getResource());
        return new Permission(domain.getId(), profile, resources, domain.getList(), domain.getFind(), domain.getCreate(), domain.getEdit(), domain.getDelete());
    }
}
