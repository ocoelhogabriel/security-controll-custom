package com.ocoelhogabriel.security_control_custom.application.dto;

public class CodigoExtends {
	private Long codigo;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Codigo [");
		if (codigo != null) {
			builder.append("codigo=").append(codigo);
		}
		builder.append("]");
		return builder.toString();
	}

	public CodigoExtends(Long codigo) {
		super();
		this.codigo = codigo;
	}

	public CodigoExtends() {
		super();

	}

}
