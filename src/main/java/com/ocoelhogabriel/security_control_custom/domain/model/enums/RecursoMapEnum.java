package com.ocoelhogabriel.security_control_custom.domain.model.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum RecursoMapEnum {
	PENDENCIA("PENDENCIA", "/api/pendencia"), FIRMWARE("FIRMWARE", "/api/pendencia-firmware"), MEDICAO("MEDICAO", "/api/medicao"), LOGGER("LOGGER", "/api/logger"), EMPRESA("EMPRESA", "/api/empresa"), USUARIO("USUARIO", "/api/usuario"), PERFIL("PERFIL", "/api/perfil"),
	RECURSO("RECURSO", "/api/recurso"), ABRANGENCIA("ABRANGENCIA", "/api/abrangencia"), MODULODEVICE("MODULODEVICE", "/api/modulo-device"), PLANTA("PLANTA", "/api/planta"), SILO("SILO", "/api/silo"), MODULO("MODULO", "/api/silo-modulo"), TIPOSILO("TIPOSILO", "/api/tipo-silo");

	private static final Logger logger = LoggerFactory.getLogger(RecursoMapEnum.class);
	private final String nome;
	private final String url;

	RecursoMapEnum(String nome, String url) {
		this.nome = nome;
		this.url = url;
	}

	public static Logger getLogger() {
		return logger;
	}

	public String getNome() {
		return nome;
	}

	public String getUrl() {
		return url;
	}

	public static String mapDescricaoToNome(String descricao) {
		for (RecursoMapEnum du : RecursoMapEnum.values()) {
			if (du.getNome().equalsIgnoreCase(descricao)) {
				return du.getNome();
			}
		}
		logger.error("Descrição não mapeada: " + descricao);
		return null;
	}

	public static String mapUrlToUrl(String url) {
		for (RecursoMapEnum du : RecursoMapEnum.values()) {
			if (du.getUrl().equalsIgnoreCase(url)) {
				return du.url;
			}
		}
		logger.error("URL não mapeada: " + url);
		return null;
	}

	public static RecursoMapEnum mapUrlToRecursoMapEnum(String url) {
		for (RecursoMapEnum du : RecursoMapEnum.values()) {
			if (du.getUrl().equalsIgnoreCase(url)) {
				return du;
			}
		}
		logger.error("URL não mapeada: " + url);
		return null;
	}
}
