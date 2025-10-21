package com.ocoelhogabriel.security_control_custom.application.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocoelhogabriel.security_control_custom.application.dto.CheckAbrangenciaRec;
import com.ocoelhogabriel.security_control_custom.application.service.AbrangenciaServiceImpl;
import com.ocoelhogabriel.security_control_custom.application.service.RecursoServiceImpl;
import com.ocoelhogabriel.security_control_custom.application.service.UsuarioServiceImpl;
import com.ocoelhogabriel.security_control_custom.domain.entity.UserDomain;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AbrangenciaHandler {

    private final Logger log = LoggerFactory.getLogger(AbrangenciaHandler.class);

    @Autowired
    @Lazy
    private AbrangenciaServiceImpl abrangenciaService;
    @Autowired
    @Lazy
    private RecursoServiceImpl recursoService;
    @Autowired
    @Lazy
    private UsuarioServiceImpl usuarioService;

    public CheckAbrangenciaRec checkAbrangencia(String text) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        UserDomain user = usuarioService.findLogin(currentUserName);

        var recurso = recursoService.findByIdEntity(text);
        var abrangencia = abrangenciaService.findByAbrangenciaAndRecursoContainingAbrangencia(user.getScopeDomain().getId(), recurso.getName());
        if (abrangencia == null)
            throw new IllegalArgumentException(
                    "Não foi encontrado nenhum detalhe de Abrangência para o usuário " + currentUserName + " no recurso " + text);

        List<Long> ids;
        try {
            ids = new ObjectMapper().readValue(abrangencia.getData(), new TypeReference<List<Long>>() {
            });
        } catch (JsonProcessingException e) {
            log.error("Error ao buscar o item da abrangencia: ", e);
            ids = null;
        }
        return new CheckAbrangenciaRec(ids, abrangencia.getHierarchy());
    }

    public Long findIdAbrangenciaPermi(CheckAbrangenciaRec checkAbrangencia, Long codigo) {
        return checkAbrangencia.isHier() == 0 ? codigo
                : checkAbrangencia.listAbrangencia().stream().filter(map -> map.equals(codigo)).findFirst().orElse(null);
    }

}
