package com.ocoelhogabriel.security_control_custom.application.handler;

import java.util.List;

import com.ocoelhogabriel.security_control_custom.application.service.UsuarioServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.User;
import com.ocoelhogabriel.security_control_custom.application.dto.CheckAbrangenciaRec;
import com.ocoelhogabriel.security_control_custom.application.service.AbrangenciaServiceImpl;
import com.ocoelhogabriel.security_control_custom.application.service.RecursoServiceImpl;
import jakarta.persistence.EntityNotFoundException;

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
		User user = usuarioService.findLoginEntity(currentUserName);
		if (user == null)
			throw new EntityNotFoundException("Usuário não encontrado: " + currentUserName);

		var recurso = recursoService.findByIdEntity(text);
		var abrangencia = abrangenciaService.findByAbrangenciaAndRecursoContainingAbrangencia(user.getAbrangencia(), recurso);
		if (abrangencia == null)
			throw new IllegalArgumentException("Não foi encontrado nenhum detalhe de Abrangência para o usuário " + currentUserName + " no recurso " + text);

		List<Long> ids;
		try {
			ids = new ObjectMapper().readValue(abrangencia.getAbddat(), new TypeReference<List<Long>>() {
			});
		} catch (JsonProcessingException e) {
			log.error("Error ao buscar o item da abrangencia: ", e);
			ids = null;
		}
		return new CheckAbrangenciaRec(ids, abrangencia.getHierarchy());
	}

	public Long findIdAbrangenciaPermi(CheckAbrangenciaRec checkAbrangencia, Long codigo) {
		return checkAbrangencia.isHier() == 0 ? codigo : checkAbrangencia.listAbrangencia().stream().filter(map -> map.equals(codigo)).findFirst().orElse(null);
	}

}
