package com.ericsson.eea.rv.testreporter.testreporter.security.model;

import com.ericsson.eea.rv.testreporter.testreporter.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class UserPrinciple implements UserDetails {
    private static final long serialVersionUID = 1L;
    private final Boolean active;
    private String username;

    private Long id;

    private String firstname;

    private String lastname;

    private String email;

    @JsonIgnore
    private String password;

    private Boolean enabled;

    private Collection<? extends GrantedAuthority> authorities;

    public UserPrinciple(Long id, String username, String firstname,
                         String lastname, String email, String password,
                         Collection<? extends GrantedAuthority> authorities, Boolean active, Boolean enabled) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.active = active;
        this.enabled = enabled;

    }

    public static UserPrinciple build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getRoleType().name())
        ).collect(Collectors.toList());

        return new UserPrinciple(
                user.getId(),
                user.getUsername(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getPassword(),
                authorities,
                user.isActive(),
                user.isEnabled()
        );
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
        return enabled;
    }

    @Override
    public int hashCode() {
        return Objects.hash(active, username, firstname, lastname, email, password, enabled, authorities);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserPrinciple user = (UserPrinciple) o;
        return Objects.equals(id, user.id);
    }
}