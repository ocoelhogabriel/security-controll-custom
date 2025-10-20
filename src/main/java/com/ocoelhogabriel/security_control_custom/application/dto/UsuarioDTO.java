package com.ocoelhogabriel.security_control_custom.application.dto;

import com.ocoelhogabriel.security_control_custom.domain.entity.UserDomain;

public class UsuarioDTO extends CodigoExtends {

    private String nome;
    private Long cpf;
    private String login;
    private String senha;
    private String email;
    private EmpresaDTO empresa;
    private PerfilDTO perfil;
    private AbrangenciaDTO abrangencia;

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

    public EmpresaDTO getEmpresa() {
        return empresa;
    }

    public void setEmpresa(EmpresaDTO empresa) {
        this.empresa = empresa;
    }

    public PerfilDTO getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilDTO perfil) {
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
            case "codigo":
                return "usucod";
            case "nome":
                return "usunom";
            case "cpf":
                return "usucpf";
            case "login":
                return "usulog";
            case "senha":
                return "ususen";
            case "email":
                return "usuema";
            default:
                throw new IllegalArgumentException("Unexpected value: " + value);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UsuarioDTO [");
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

    public UsuarioDTO(Long codigo, String nome, Long cpf, String login, String senha, String email, EmpresaDTO empresa, PerfilDTO perfil,
            AbrangenciaDTO abrangencia) {
        super(codigo);
        this.nome = nome;
        this.cpf = cpf;
        this.login = login;
        this.senha = senha;
        this.email = email;
        this.empresa = empresa;
        this.perfil = perfil;
        this.abrangencia = abrangencia;
    }

    public UsuarioDTO(UserDomain user) {
        super();
        this.setCodigo(user.getId());
        this.nome = user.getName();
        this.cpf = user.getCpf();
        this.login = user.getLogin();
        this.senha = user.getPassword();
        this.email = user.getEmail();
        this.empresa = new EmpresaDTO(user.getCompanyDomain());
        this.perfil = new PerfilDTO(user.getProfileDomain());
        this.abrangencia = new AbrangenciaDTO(user.getScopeDomain());
    }

    public UsuarioDTO() {
        super();
    }

    public UsuarioDTO(Long codigo) {
        super(codigo);
    }

}
