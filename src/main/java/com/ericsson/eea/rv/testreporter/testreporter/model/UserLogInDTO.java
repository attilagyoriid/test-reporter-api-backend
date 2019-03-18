package com.ericsson.eea.rv.testreporter.testreporter.model;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@ApiModel(description = "Information on Log in User")
public class UserLogInDTO implements Serializable {

    private String email;

    private String password;

    public UserLogInDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserLogInDTO() {

    }
}
