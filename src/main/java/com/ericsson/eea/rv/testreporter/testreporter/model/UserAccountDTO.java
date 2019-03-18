package com.ericsson.eea.rv.testreporter.testreporter.model;


import com.ericsson.eea.rv.testreporter.testreporter.domain.Role;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Data
public class UserAccountDTO implements Serializable {

    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String password;
    private Set<Role> roles = new HashSet<>();
    private boolean active;
    private boolean enabled;


    public UserAccountDTO(String firstname, String lastname, String username, String email, String password, Set<Role> roles) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public UserAccountDTO() {

    }
}
