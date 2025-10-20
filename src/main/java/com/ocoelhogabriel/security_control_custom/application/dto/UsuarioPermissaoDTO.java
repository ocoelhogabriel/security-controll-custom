package com.ocoelhogabriel.security_control_custom.application.dto;

import com.ocoelhogabriel.security_control_custom.domain.entity.UserDomain;
import com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity.User;

public class UsuarioPermissaoDTO extends CodigoExtends {

	private String login;
	private String senha;
	private String email;
	private EmpresaDTO empresa;
	private AbrangenciaDTO abrangencia;
	private PerfilPermissaoDTO perfil;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public EmpresaDTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaDTO empresa) {
		this.empresa = empresa;
	}

	public PerfilPermissaoDTO getPerfil() {
		return perfil;
	}

	public void setPerfil(PerfilPermissaoDTO perfil) {
		this.perfil = perfil;
	}

	public AbrangenciaDTO getAbrangencia() {
		return abrangencia;
	}

	public void setAbrangencia(AbrangenciaDTO abrangencia) {
		this.abrangencia = abrangencia;
	}

	public static String consultaPagable(String value) {
		switch (value) {
		case "codigo" -> {
			return "usucod";
		}
		case "login" -> {
			return "usulog";
		}
		case "senha" -> {
			return "ususen";
		}
		case "email" -> {
			return "usuema";
		}
		default -> throw new IllegalArgumentException("Unexpected value: " + value);
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UsuarioPermissaoDTO [");
		if (login != null) {
			builder.append("login=").append(login).append(", ");
		}
		if (senha != null) {
			builder.append("senha=").append(senha).append(", ");
		}
		if (email != null) {
			builder.append("email=").append(email).append(", ");
		}
		if (empresa != null) {
			builder.append("empresa=").append(empresa).append(", ");
		}
		if (abrangencia != null) {
			builder.append("abrangencia=").append(abrangencia).append(", ");
		}
		if (perfil != null) {
			builder.append("perfil=").append(perfil).append(", ");
		}
		builder.append("]");
		return builder.toString();
	}

	public UsuarioPermissaoDTO(Long codigo, String login, String senha, String email, EmpresaDTO empresa, PerfilPermissaoDTO perfil, AbrangenciaDTO abrangencia) {
		super(codigo);
		this.login = login;
		this.senha = senha;
		this.email = email;
		this.empresa = empresa;
		this.perfil = perfil;
		this.abrangencia = abrangencia;
	}

	public UsuarioPermissaoDTO(UserDomain user, PerfilPermissaoDTO perfil) {
		super();
		this.setCodigo(user.getId());
		this.login = user.getLogin();
		this.senha = user.getPassword();
		this.email = user.getEmail();
		this.empresa = new EmpresaDTO(user.getCompanyDomain());
		this.abrangencia = new AbrangenciaDTO(user.getScopeDomain());
		this.perfil = perfil;
	}

	public UsuarioPermissaoDTO() {
		super();

	}

	public UsuarioPermissaoDTO(Long codigo) {
		super(codigo);

	}

}
