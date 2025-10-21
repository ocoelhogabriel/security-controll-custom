package com.ocoelhogabriel.security_control_custom.application.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocoelhogabriel.security_control_custom.application.dto.EmpresaModel;
import com.ocoelhogabriel.security_control_custom.application.dto.PermissaoModel;
import com.ocoelhogabriel.security_control_custom.application.dto.PlantaModel;
import com.ocoelhogabriel.security_control_custom.application.dto.RecursoModel;
import com.ocoelhogabriel.security_control_custom.application.dto.UsuarioModel;
import com.ocoelhogabriel.security_control_custom.application.service.*;
import com.ocoelhogabriel.security_control_custom.domain.entity.*;
import com.ocoelhogabriel.security_control_custom.domain.model.enums.RecursoMapEnum;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    private PlantaServiceImpl plantaService;

    private static final Long CNPJTSI = 44772937000150L;

    private static final String[] listaRecursos = { "USUARIO", "PERFIL", "RECURSO", "ABRANGENCIA", "EMPRESA", "PLANTA" };
    private static final String[] listaAbrangencia = { "EMPRESA", "PLANTA" };
    private static final String[] listaPlantas = { "Matriz", "Filial SP", "Filial RJ" };


    @PostConstruct
    public void createAdminHandler() {
        try {
            logs.info("CreateAdminHandler Start... ");
            createEmpresa();
            createPlantas();
            createRecurso();
            createPerfilPermissao();
            createAbrangencia();

            var user = usuarioService.findLoginInternal("admin");
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
            var empresa = empresaService.findByDocumentInternal(CNPJTSI);

            EmpresaModel empresaModel = new EmpresaModel(CNPJTSI, "Telemática - Sistemas Inteligentes", "TSI", "(99)99999-9999");
            if (empresa == null) {
                empresaService.empresaSave(empresaModel);
            }

        } catch (IOException e) {
            logs.error("Error createEmpresa: ", e);
        }
    }

    public void createPlantas() {
        try {
            logs.info("createPlantas Start...");
            CompanyDomain empresa = empresaService.findByDocumentInternal(CNPJTSI);
            if (empresa == null) {
                logs.error("Empresa padrão não encontrada. Não é possível criar plantas.");
                return;
            }
            for (String nomePlanta : listaPlantas) {
                PlanDomain plantaExistente = plantaService.findByName(nomePlanta);
                if (plantaExistente == null) {
                    PlantaModel novaPlanta = new PlantaModel(empresa.getId(), nomePlanta);
                    plantaService.save(novaPlanta);
                }
            }
        } catch (Exception e) {
            logs.error("Error createPlantas: ", e);
        }
    }

    public void createPerfilPermissao() {
        try {
            logs.info("createPerfil Start... ");
            var perfil = perfilPermissaoService.findByIdPerfilEntity("ADMIN");
            if (perfil == null) {
                perfil = perfilPermissaoService.createUpdatePerfil(new ProfileDomain(null, "ADMIN", "Perfil do Administrador."));
            } else {
                perfil = perfilPermissaoService.createUpdatePerfil(new ProfileDomain(perfil.getId(), "ADMIN", "Perfil do Administrador."));
            }
            for (String listaRecurso : listaRecursos) {
                RecursoMapEnum recursoEnum = RecursoMapEnum.valueOf(listaRecurso);
                var valueRecurso = perfilPermissaoService.findByPerfilAndRecurso(perfil, recursoService.findByIdEntity(recursoEnum.getNome()));
                PermissaoModel permissao = new PermissaoModel(recursoEnum, true, true, true, true, true);
                if (valueRecurso == null) {
                    perfilPermissaoService.saveEntityPermissao(perfil, permissao);
                }
            }
        } catch (Exception e) {
            logs.error("createPerfil: ", e);
        }
    }

    public void createRecurso() {
        try {
            logs.info("createRecurso Start... ");
            for (String listaRecurso : listaRecursos) {
                RecursoMapEnum recursoEnum = RecursoMapEnum.valueOf(listaRecurso);
                var valueRecurso = recursoService.findByIdEntity(recursoEnum.getNome());
                RecursoModel recurso = new RecursoModel(recursoEnum, "Descrição do Recurso " + listaRecurso);
                if (valueRecurso == null) {
                    recursoService.saveEntity(recurso);
                }
            }
        } catch (Exception e) {
            logs.error("createRecurso: ", e);
        }
    }

    public void createAbrangencia() {
        try {
            logs.info("createAbrangencia Start...");

            ScopeDomain scope = abrangenciaService.findByIdEntity("ADMIN");

            if (scope == null) {
                scope = new ScopeDomain(null, "ADMIN", "Descrição Abrangencia ADMIN");
            } else {
                scope = new ScopeDomain(scope.getId(), "ADMIN", "Descrição Abrangencia ADMIN");
            }
            scope = abrangenciaService.createUpdateAbrangencia(scope);

            ObjectMapper objectMapper = new ObjectMapper();

            for (String s : listaAbrangencia) {
                RecursoMapEnum recursoEnum = RecursoMapEnum.valueOf(s);
                ResourcesDomain resources = recursoService.findByIdEntity(recursoEnum.getNome());

                String data;

                if ("PLANTA".equals(recursoEnum.getNome())) {
                    List<Long> plantIds = new ArrayList<>();
                    for (String nomePlanta : listaPlantas) {
                        PlanDomain planta = plantaService.findByName(nomePlanta);
                        if (planta != null) {
                            plantIds.add(planta.getId());
                        }
                    }
                    data = objectMapper.writeValueAsString(plantIds);
                } else if ("EMPRESA".equals(recursoEnum.getNome())) {
                    CompanyDomain empresa = empresaService.findByDocumentInternal(CNPJTSI);
                    List<Long> empresaIds = new ArrayList<>();
                    if (empresa != null) {
                        empresaIds.add(empresa.getId());
                    }
                    data = objectMapper.writeValueAsString(empresaIds);
                } else {
                    data = objectMapper.writeValueAsString(new ArrayList<>());
                }

                ScopeDetailsDomain abd = new ScopeDetailsDomain(null, scope, resources, 0, data);

                ScopeDetailsDomain existingDetail = abrangenciaService.findByAbrangenciaAndRecursoContainingAbrangencia(scope.getId(), resources.getName());
                if (existingDetail == null) {
                    abrangenciaService.saveOrUpdateAbrangenciaDetalhes(scope, abd);
                } else {
                    existingDetail.setData(data);
                    abrangenciaService.saveOrUpdateAbrangenciaDetalhes(scope, existingDetail);
                }
            }
        } catch (Exception e) {
            logs.error("createAbrangencia: ", e);
        }
    }

    public void createUsuario() {
        try {
            logs.info("createUsuario Start... ");
            var empresa = empresaService.findByDocumentInternal(CNPJTSI);
            var perfil = perfilPermissaoService.findByIdPerfilEntity("ADMIN");
            var abrangencia = abrangenciaService.findByIdEntity("ADMIN");
            UsuarioModel usuario = new UsuarioModel("ADMIN", 0L,
                    "admin",
                    "admin",
                    "admin@admin.com",
                    empresa.getId(),
                    perfil.getId(),
                    abrangencia.getId());
            var userCheck = usuarioService.findLoginInternal("admin");
            if (userCheck == null) {
                usuarioService.saveUpdateEntity(usuario);
            }
        } catch (EntityNotFoundException |
                 IOException e) {
            logs.error("createUsuario: ", e);
        }
    }
}
