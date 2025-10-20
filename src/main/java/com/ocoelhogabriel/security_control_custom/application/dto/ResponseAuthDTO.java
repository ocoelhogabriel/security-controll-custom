package com.ocoelhogabriel.security_control_custom.application.dto;

public class ResponseAuthDTO {

	private String token;
	private String data;
	private String expiryIn;
	private Long codigoUsuario;
	private PerfilDTO perfil;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getExpiryIn() {
		return expiryIn;
	}

	public void setExpiryIn(String expiryIn) {
		this.expiryIn = expiryIn;
	}

	public Long getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(Long idUser) {
		this.codigoUsuario = idUser;
	}

	public PerfilDTO getPerfil() {
		return perfil;
	}

	public void setPerfil(PerfilDTO perfil) {
		this.perfil = perfil;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ResponseAuthDTO [");
		if (token != null) {
			builder.append("token=").append(token).append(", ");
		}
		if (data != null) {
			builder.append("data=").append(data).append(", ");
		}
		if (expiryIn != null) {
			builder.append("expiryIn=").append(expiryIn).append(", ");
		}
		if (codigoUsuario != null) {
			builder.append("idUser=").append(codigoUsuario).append(", ");
		}
		if (perfil != null) {
			builder.append("perfil=").append(perfil);
		}
		builder.append("]");
		return builder.toString();
	}

	public ResponseAuthDTO(String token, String data, String expiryIn, Long idUser, PerfilDTO perfil) {
		super();
		this.token = token;
		this.data = data;
		this.expiryIn = expiryIn;
		this.codigoUsuario = idUser;
		this.perfil = perfil;
	}

	public ResponseAuthDTO() {
		super();

	}

}
