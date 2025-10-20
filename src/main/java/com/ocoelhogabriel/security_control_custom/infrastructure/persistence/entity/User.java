package com.ocoelhogabriel.security_control_custom.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "user")
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long cpf;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @ManyToOne
    @JoinColumn(name = "id_company", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "id_profile", nullable = false)
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "id_scope", nullable = false)
    private Scope scope;

    public User(Long id, Long cpf, String name, String login, String password, String email, Company company, Profile profile, Scope scope) {
        super();
        this.id = id;
        this.cpf = cpf;
        this.name = name;
        this.login = login;
        this.password = password;
        this.email = email;
        this.company = company;
        this.profile = profile;
        this.scope = scope;
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
        if (company != null) {
            builder.append("company=").append(company).append(", ");
        }
        if (profile != null) {
            builder.append("perfil=").append(profile).append(", ");
        }
        if (scope != null) {
            builder.append("scope=").append(scope);
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

    public Long getCpf() {
        return cpf;
    }

    public void setCpf(Long cpf) {
        this.cpf = cpf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHas() {
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

    public Company getEmpresa() {
        return company;
    }

    public void setEmpresa(Company company) {
        this.company = company;
    }

    public Profile getPerfil() {
        return profile;
    }

    public void setPerfil(Profile profile) {
        this.profile = profile;
    }

    public Scope getAbrangencia() {
        return scope;
    }

    public void setAbrangencia(Scope scope) {
        this.scope = scope;
    }

    public User() {
        super();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(this.profile.getName()));
    }

    @Override
    public String getPassword() {
        return this.getPassword();
    }

    @Override
    public String getUsername() {
        return this.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}