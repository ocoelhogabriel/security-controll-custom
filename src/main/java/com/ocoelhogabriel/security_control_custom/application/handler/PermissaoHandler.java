package com.ocoelhogabriel.security_control_custom.application.handler;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.ocoelhogabriel.security_control_custom.application.service.PerfilPermissaoServiceImpl;
import com.ocoelhogabriel.security_control_custom.application.service.RecursoServiceImpl;

@Configuration
public class PermissaoHandler {

	@Autowired
	private RecursoServiceImpl recursoService;
	@Autowired
	private PerfilPermissaoServiceImpl perfilPermissaoService;

	public boolean checkPermission(String perfil, URLValidator urlValidator, String method) {
		String nomeRecurso = urlValidator.getRecursoMapEnum().getNome();
		Objects.requireNonNull(nomeRecurso, "Nome do recurso está nulo");
		Objects.requireNonNull(perfil, "Perfil está nulo");
		Objects.requireNonNull(urlValidator, "UrlValidator está nulo");

		var recursoEntity = recursoService.findByIdEntity(nomeRecurso);
		var perfilEntity = perfilPermissaoService.findByIdPerfilEntity(perfil);
		var entity = perfilPermissaoService.findByPerfilAndRecurso(perfilEntity, recursoEntity);

		switch (method) {
		case "GET" -> {
			if (urlValidator.getMessage().equalsIgnoreCase("BUSCAR")) {
				if (entity.getFind() == 1)
					return true;
			} else {
				if (entity.getList() == 1)
					return true;
			}
		}
		case "POST" -> {
			if (entity.getCreate() == 1)
				return true;
		}
		case "PUT" -> {
			if (entity.getEdit() == 1)
				return true;
		}
		case "DELETE" -> {
			if (entity.getDelete() == 1)
				return true;
		}
		default -> {
			return false;
		}
		}
		return false;
	}

}
