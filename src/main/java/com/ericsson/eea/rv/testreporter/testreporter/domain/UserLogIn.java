package com.ericsson.eea.rv.testreporter.testreporter.domain;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode()
@ApiModel(description = "Information on Log in User")
public class UserLogIn implements Serializable {

    private String email;

    private String password;

    public UserLogIn(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserLogIn() {

    }
}
