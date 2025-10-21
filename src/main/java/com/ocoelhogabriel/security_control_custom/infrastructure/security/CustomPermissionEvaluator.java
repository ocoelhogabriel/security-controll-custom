package com.ocoelhogabriel.security_control_custom.infrastructure.security;

import com.ocoelhogabriel.security_control_custom.application.service.PerfilPermissaoServiceImpl;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private PerfilPermissaoServiceImpl perfilPermissaoService;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if ((authentication == null) || !(targetDomainObject instanceof String targetType) || !(permission instanceof String action)) {
            return false;
        }
        return hasPermission(authentication, null, targetType, action);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if ((authentication == null) || !(authentication.getPrincipal() instanceof User user) || !(permission instanceof String action)) {
            return false;
        }

        // targetType aqui será o nome do recurso (ex: "USUARIO", "EMPRESA", "PLANTAS")
        // action será a operação (ex: "list", "find", "create", "edit", "delete")

        // Implementar a lógica para verificar a permissão usando perfilPermissaoService
        // O PerfilPermissaoServiceImpl precisará de um método para verificar se o perfil do usuário
        // tem a permissão para a ação no recurso especificado.
        // Exemplo: perfilPermissaoService.checkPermission(user.getPerfil().getName(), targetType, action);

        // Por enquanto, um placeholder:
        return perfilPermissaoService.checkPermission(user.getPerfil().getName(), targetType, action);
    }
}
