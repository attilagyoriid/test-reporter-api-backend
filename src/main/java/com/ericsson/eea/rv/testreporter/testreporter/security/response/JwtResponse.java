package com.ericsson.eea.rv.testreporter.testreporter.security.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private Collection<? extends GrantedAuthority> authorities;
    private Long id;

    public JwtResponse(String token, String username, Collection<? extends GrantedAuthority> authorities, Long id) {

        this.token = token;
        this.username = username;
        this.authorities = authorities;
        this.id = id;
    }


}
