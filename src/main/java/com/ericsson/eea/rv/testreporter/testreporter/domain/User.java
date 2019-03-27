package com.ericsson.eea.rv.testreporter.testreporter.domain;


import com.ericsson.eea.rv.testreporter.testreporter.validation.ValidPassword;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "users")
@ApiModel(description = "Information on User")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(notes = "First name of the User", required = true)
    @NotEmpty(message = "{user.firstname.notempty}")
    @Size(min = 2, message = "{user.firstname.size}")
    private String firstname;

    @ApiModelProperty(notes = "Last name of the User", required = true)
    @NotEmpty(message = "{user.lastname.notempty}")
    @Size(min = 3, message = "{user.lastname.size}")
    private String lastname;

    @NotEmpty(message = "{user.username.notempty}")
    @Size(min = 3, message = "{user.username.size}")
    private String username;

    @NotEmpty(message = "{user.email.notempty}")
    @Email
    @Size(min = 8, message = "{user.email.size}")
    private String email;

    @Lob
    private byte[] image;

    @ValidPassword
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    private boolean active;

    private boolean enabled;


    public User(String firstname, String lastname,String username, String email, String password, Set<Role> roles) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public User() {

    }
}
