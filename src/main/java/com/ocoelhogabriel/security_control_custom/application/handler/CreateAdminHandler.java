package com.ocoelhogabriel.security_control_custom.application.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocoelhogabriel.security_control_custom.application.service.*;
import com.ocoelhogabriel.security_control_custom.application.dto.EmpresaModel;
import com.ocoelhogabriel.security_control_custom.application.dto.PermissaoModel;
import com.ocoelhogabriel.security_control_custom.application.dto.RecursoModel;
import com.ocoelhogabriel.security_control_custom.application.dto.UsuarioModel;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Scope;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.ScopeDetails;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Profile;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.Resources;
import com.ocoelhogabriel.security_control_custom.domain.model.enums.RecursoMapEnum;
import com.ocoelhogabriel.security_control_custom.infrastructure.utils.JsonNodeConverter;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class CreateAdminHandler {

    private static final Logger logs = LoggerFactory.getLogger(CreateAdminHandler.class);

    @Autowired
    private UsuarioServiceImpl usuarioService;
    @Autowired
    private PerfilPermissaoServiceImpl perfilPermissaoService;
    @Autowired
    private RecursoServiceImpl recursoService;
    @Autowired
    private AbrangenciaServiceImpl abrangenciaService;
    @Autowired
    private EmpresaServiceImpl empresaService;

    private static final Long CNPJTSI = Long.valueOf("44772937000150");

    private static final String[] listaRecursos = { "LOGGER", "USUARIO", "PERFIL", "RECURSO", "ABRANGENCIA", "EMPRESA", "PLANTA" };
    private static final String[] listaAbrangencia = { "EMPRESA", "PLANTA", };

    @PostConstruct
    public void createAdminHandler() {
        try {
            logs.info("CreateAdminHandler Start... ");
            var user = usuarioService.findLoginEntityNull("admin");
            createEmpresa();
            createRecurso();
            createPerfilPermissao();
            createAbrangencia();
            if (user == null) {
                logs.info("CreateAdminHandler Init Create... ");
                createUsuario();
            }
        } catch (Exception e) {
            logs.error("CreateAdminHandler: ", e);
        }
    }

    public void createEmpresa() {
        try {
            logs.info("createEmpresa Start... ");
            var empresa = empresaService.empresaFindByCnpjEntity(CNPJTSI);

            EmpresaModel empresaModel = new EmpresaModel(CNPJTSI, "Telemática - Sistemas Inteligentes", "TSI", "(99)99999-9999");
            if (empresa == null)
                empresaService.empresaSave(empresaModel);

        } catch (IOException e) {
            logs.error("Error createEmpresa: ", e);
        }
    }

    public void createPerfilPermissao() {
        try {
            logs.info("createPerfil Start... ");
            var perfil = perfilPermissaoService.findByIdPerfilEntity("ADMIN");
            if (perfil == null)
                perfil = perfilPermissaoService.createUpdatePerfil(new Profile(null, "ADMIN", "Perfil do Administrador."));
            else
                perfil = perfilPermissaoService.createUpdatePerfil(new Profile(perfil.getId(), "ADMIN", "Perfil do Administrador."));
            int listItem = listaRecursos.length;
            for (int i = 0; i < listItem; i++) {
                RecursoMapEnum recursoEnum = RecursoMapEnum.valueOf(listaRecursos[i]);
                var valueRecurso = perfilPermissaoService.findByPerfilAndRecurso(perfil, recursoService.findByIdEntity(recursoEnum.getNome()));
                PermissaoModel permissao = new PermissaoModel(recursoEnum, 1, 1, 1, 1, 1);
                if (valueRecurso == null)
                    perfilPermissaoService.saveEntityPermissao(perfil, permissao);

            }

        } catch (Exception e) {
            logs.error("createPerfil: ", e);
        }
    }

    public void createRecurso() {
        try {
            logs.info("createRecurso Start... ");
            int listItem = listaRecursos.length;
            for (int i = 0; i < listItem; i++) {
                RecursoMapEnum recursoEnum = RecursoMapEnum.valueOf(listaRecursos[i]);
                var valueRecurso = recursoService.findByIdEntity(recursoEnum.getNome());
                RecursoModel recurso = new RecursoModel(recursoEnum, "Descrição do Recurso " + listaRecursos[i]);
                if (valueRecurso == null)
                    recursoService.saveEntity(recurso);
            }
        } catch (Exception e) {
            logs.error("createRecurso: ", e);
        }
    }

    public void createAbrangencia() {
        try {
            logs.info("createAbrangencia Start...");

            Scope scope = abrangenciaService.findByIdEntity("ADMIN");

            if (scope == null) {
                scope = new Scope(null, "ADMIN", "Descrição Abrangencia ADMIN");
            } else {
                scope = new Scope(scope.getId(), "ADMIN", "Descrição Abrangencia ADMIN");
            }
            abrangenciaService.createUpdateAbrangencia(scope);

            int listItem = listaAbrangencia.length;
            for (int i = 0; i < listItem; i++) {
                RecursoMapEnum recursoEnum = RecursoMapEnum.valueOf(listaAbrangencia[i]);
                Resources resources = recursoService.findByIdEntity(recursoEnum.getNome());

                JsonNodeConverter jsonNode = new JsonNodeConverter();
                String data = jsonNode.convertToDatabaseColumn(new ObjectMapper().createArrayNode());

                ScopeDetails abd = new ScopeDetails(null, scope, resources, 0, data);

                if (abrangenciaService.findByAbrangenciaAndRecursoContainingAbrangencia(scope, resources) == null)
                    abrangenciaService.saveOrUpdateAbrangenciaDetalhes(scope, abd);
            }
        } catch (Exception e) {
            logs.error("createAbrangencia: ", e);
        }
    }

    public void createUsuario() {
        try {
            logs.info("createUsuario Start... ");
            var empresa = empresaService.empresaFindByCnpjEntity(CNPJTSI);
            var perfil = perfilPermissaoService.findByIdPerfilEntity("ADMIN");
            var abrangencia = abrangenciaService.findByIdEntity("ADMIN");
            UsuarioModel usuario = new UsuarioModel("ADMIN",
                    Long.valueOf(0),
                    "admin",
                    "admin",
                    "admin@admin.com",
                    empresa.getId(),
                    perfil.getId(),
                    abrangencia.getId());
            var userCheck = usuarioService.findLoginEntityNull("admin");
            if (userCheck == null)
                usuarioService.saveUpdateEntity(usuario);
        } catch (EntityNotFoundException |
                IOException e) {
            logs.error("createUsuario: ", e);
        }
    }


}
