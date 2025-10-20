package com.ocoelhogabriel.security_control_custom.domain.model.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum VersaoMapEnum {
	V1("/v1"), V2("/v2"), V3("/v3"), V4("/v4"), V5("/v5"), V6("/v6");

	private static final Logger logger = LoggerFactory.getLogger(VersaoMapEnum.class);
	private final String versao;

	VersaoMapEnum(String versao) {
		this.versao = versao;
	}

	public String getVersao() {
		return versao;
	}

	public static String mapDescricaoToVersao(String descricao) {
		for (VersaoMapEnum du : VersaoMapEnum.values()) {
			if (du.getVersao().equalsIgnoreCase(descricao)) {
				return du.getVersao();
			}
		}
		logger.error("Descrição não mapeada: " + descricao);
		return null;
	}

}
