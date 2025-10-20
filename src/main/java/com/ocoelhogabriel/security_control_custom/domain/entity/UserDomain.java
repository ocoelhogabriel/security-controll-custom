package com.ocoelhogabriel.security_control_custom.domain.entity;

public class UserDomain {

    private Long id;
    private String name;
    private Long cpf;
    private String login;
    private String password;
    private String email;
    private CompanyDomain companyDomain;
    private ProfileDomain profileDomain;
    private ScopeDomain scopeDomain;

    public UserDomain(Long id, Long cpf, String name, String login, String password, String email, CompanyDomain companyDomain,
            ProfileDomain profileDomain, ScopeDomain scopeDomain) {
        super();
        this.id = id;
        this.cpf = cpf;
        this.name = name;
        this.login = login;
        this.password = password;
        this.email = email;
        this.companyDomain = companyDomain;
        this.profileDomain = profileDomain;
        this.scopeDomain = scopeDomain;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UserDomain [");
        if (id != null) {
            builder.append("id=").append(id).append(", ");
        }
        if (cpf != null) {
            builder.append("cpf=").append(cpf).append(", ");
        }
        if (name != null) {
            builder.append("name=").append(name).append(", ");
        }
        if (login != null) {
            builder.append("login=").append(login).append(", ");
        }
        if (password != null) {
            builder.append("password=").append(password).append(", ");
        }
        if (email != null) {
            builder.append("email=").append(email).append(", ");
        }
        if (companyDomain != null) {
            builder.append("companyDomain=").append(companyDomain).append(", ");
        }
        if (profileDomain != null) {
            builder.append("perfil=").append(profileDomain).append(", ");
        }
        if (scopeDomain != null) {
            builder.append("scopeDomain=").append(scopeDomain);
        }
        builder.append("]");
        return builder.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CompanyDomain getCompanyDomain() {
        return companyDomain;
    }

    public void setCompanyDomain(CompanyDomain companyDomain) {
        this.companyDomain = companyDomain;
    }

    public ProfileDomain getProfileDomain() {
        return profileDomain;
    }

    public void setProfileDomain(ProfileDomain profileDomain) {
        this.profileDomain = profileDomain;
    }

    public ScopeDomain getScopeDomain() {
        return scopeDomain;
    }

    public void setScopeDomain(ScopeDomain scopeDomain) {
        this.scopeDomain = scopeDomain;
    }
}