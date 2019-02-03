package com.ericsson.eea.rv.testreporter.testreporter.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@AllArgsConstructor
@Table(name = "roles")
@EqualsAndHashCode()
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "role.roletype.notnull")
    private RoleType roleType;


    public Role(RoleType roleType) {
        this.roleType = roleType;

    }
}