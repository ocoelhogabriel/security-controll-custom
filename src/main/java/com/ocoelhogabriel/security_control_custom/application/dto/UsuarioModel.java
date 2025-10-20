package com.ocoelhogabriel.security_control_custom.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Modelo de Usuário")
public class UsuarioModel {

	@NotBlank(message = "O campo 'nome' é obrigatório e não pode estar em branco.")
	@Schema(name = "nome", description = "Cadastro do nome do usuário.", example = "Administrador", format = "String")
	private String nome;

	@NotBlank(message = "O campo 'cpf' é obrigatório e não pode estar em branco. Informe apenas números.")
	@Schema(name = "cpf", description = "Cadastro do CPF do usuário. (Somente números)", example = "12332123212", format = "Long")
	private Long cpf;

	@NotBlank(message = "O campo 'login' é obrigatório e não pode estar em branco.")
	@Schema(name = "login", description = "Cadastro do login do usuário.", example = "admin", format = "String")
	private String login;

	@NotBlank(message = "O campo 'senha' é obrigatório e não pode estar em branco.")
	@Schema(name = "senha", description = "Cadastro do senha do usuário.", example = "admin", format = "String")
	private String senha;

	@NotBlank(message = "O campo 'email' é obrigatório e não pode estar em branco.")
	@Schema(name = "email", description = "Cadastro do email do usuário.", example = "admin@admin.com", format = "String")
	private String email;

	@NotBlank(message = "O campo 'empresa' é obrigatório e não pode estar em branco.")
	@Schema(name = "empresa", description = "Código da empresa do usuário.", example = "1", format = "Long")
	private Long empresa;

	@NotBlank(message = "O campo 'perfil' é obrigatório e não pode estar em branco.")
	@Schema(name = "perfil", description = "Código do perfil do usuário.", example = "1", format = "Long")
	private Long perfil;

	@NotBlank(message = "O campo 'abrangencia' é obrigatório e não pode estar em branco.")
	@Schema(name = "abrangencia", description = "Código da abrangência do usuário.", example = "1", format = "Long")
	private Long abrangencia;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getCpf() {
		return cpf;
	}

	public void setCpf(Long cpf) {
		this.cpf = cpf;
	}

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

	public Long getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Long empresa) {
		this.empresa = empresa;
	}

	public Long getPerfil() {
		return perfil;
	}

	public void setPerfil(Long perfil) {
		this.perfil = perfil;
	}

	public Long getAbrangencia() {
		return abrangencia;
	}

	public void setAbrangencia(Long abrangencia) {
		this.abrangencia = abrangencia;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserDomain [");
		if (nome != null) {
			builder.append("nome=").append(nome).append(", ");
		}
		if (cpf != null) {
			builder.append("cpf=").append(cpf).append(", ");
		}
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
		if (perfil != null) {
			builder.append("perfil=").append(perfil).append(", ");
		}
		if (abrangencia != null) {
			builder.append("abrangencia=").append(abrangencia);
		}
		builder.append("]");
		return builder.toString();
	}

	public UsuarioModel(String nome, Long cpf, String login, String senha, String email, Long empresa, Long perfil, Long abrangencia) {
		super();
		this.nome = nome;
		this.cpf = cpf;
		this.login = login;
		this.senha = senha;
		this.email = email;
		this.empresa = empresa;
		this.perfil = perfil;
		this.abrangencia = abrangencia;
	}

	public UsuarioModel() {
		super();

	}

}