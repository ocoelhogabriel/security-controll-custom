package com.ocoelhogabriel.security_control_custom.domain.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UserAuthDetails implements UserDetails {

    private final UserDomain userDomain;

    public UserAuthDetails(UserDomain userDomain) {
        this.userDomain = userDomain;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Assuming ProfileDomain has a name that can be used as a role
        if (userDomain.getProfileDomain() != null) {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userDomain.getProfileDomain().getName().toUpperCase()));
        }
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return userDomain.getPassword();
    }

    @Override
    public String getUsername() {
        return userDomain.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Or implement actual logic from UserDomain
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Or implement actual logic from UserDomain
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Or implement actual logic from UserDomain
    }

    @Override
    public boolean isEnabled() {
        return true; // Or implement actual logic from UserDomain
    }

    public UserDomain getUserDomain() {
        return userDomain;
    }
}
