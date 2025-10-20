package com.ocoelhogabriel.security_control_custom.domain.model.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum AcaoRecursoMapEnum {

	AUTH("/auth"), 
	VALIDATE("/validate"), 
	AUTHVALIDATE("/auth-validate"), 
	CODIGO("/codigo"),
	BUSCARCNPJ("/buscar-cnpj"),
	CNPJ("/cnpj"),
	DOWNLOAD("/download"),
	PAGINADO("/paginado"),
	PERMISSAO("/permissao"),
	MODULO("/modulo"),
	KEEPALIVE("/keepAlive"),
	LOGGER("/logger"),
	MEDICAO("/medicao"),
	MEDICAOAUDIO("/medicao-audio"),
	PENDENCIA("/pendencia"),
	FIRMWAREDOWNLOAD("/firmware-download"),
	LISTAITENSABRANGENTES("/lista-items-abrangentes");

	private static final Logger logger = LoggerFactory.getLogger(AcaoRecursoMapEnum.class);
	private final String action;

	AcaoRecursoMapEnum(String versao) {
		this.action = versao;
	}

	public String getAction() {
		return action;
	}

	public static String mapDescricaoToAction(String descricao) {
		for (AcaoRecursoMapEnum du : AcaoRecursoMapEnum.values()) {
			if (descricao.toUpperCase().startsWith(du.getAction().toUpperCase())) {
				return du.getAction();
			}
		}
		logger.debug("Descrição não mapeada: " + descricao);
		return null;
	}

}
