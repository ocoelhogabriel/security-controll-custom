package com.ocoelhogabriel.security_control_custom.application.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ocoelhogabriel.security_control_custom.domain.model.enums.LoggerEnum;
import com.ocoelhogabriel.security_control_custom.infrastructure.utils.Utils;

public class LoggerDTO {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private String data;
	private LoggerEnum tipoLogger;
	private String mensagem;

	public static String consultaPagable(String value) {
		switch (value.toUpperCase()) {
		case "DATA" -> {
			return "logdat";
		}
		case "TIPOLOGGER" -> {
			return "logtip";
		}
		default -> throw new IllegalArgumentException("Unexpected value: " + value);
		}
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public LoggerEnum getTipoLogger() {
		return tipoLogger;
	}

	public void setTipoLogger(LoggerEnum tipoLogger) {
		this.tipoLogger = tipoLogger;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LoggerDTO [");
		if (logger != null) {
			builder.append("logger=").append(logger).append(", ");
		}
		if (data != null) {
			builder.append("data=").append(data).append(", ");
		}
		if (tipoLogger != null) {
			builder.append("tipoLogger=").append(tipoLogger).append(", ");
		}
		if (mensagem != null) {
			builder.append("mensagem=").append(mensagem);
		}
		builder.append("]");
		return builder.toString();
	}

	public LoggerDTO(String data, LoggerEnum tipoLogger, String mensagem) {
		super();
		this.data = data;
		this.tipoLogger = tipoLogger;
		this.mensagem = mensagem;
	}

	public LoggerDTO(Loggers pend) {
		try {
			LoggerEnum enumLogger = LoggerEnum.valueOf(pend.getLogtip());
			this.data = Utils.dateToString(pend.getDateTime());
			this.tipoLogger = enumLogger;
			this.mensagem = pend.getLogmsg();
		} catch (Exception e) {
			logger.info("Error: ", e);
		}
	}

	public LoggerDTO() {
		super();

	}

}
