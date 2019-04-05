package com.ericsson.eea.rv.testreporter.testreporter.security.jwt;


import com.ericsson.eea.rv.testreporter.testreporter.exceptions.JWTTokenValidationException;
import com.ericsson.eea.rv.testreporter.testreporter.security.model.UserPrinciple;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtProvider {


    @Value("${testreporter.app.jwtSecret}")
    private String jwtSecret;

    @Value("${testreporter.app.jwtExpiration}")
    private int jwtExpiration;

    public String generateJwtToken(Authentication authentication) {

        UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration * 1000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateJwtToken(String authToken) {
        boolean returnState = false;
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            returnState =  true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature");
            throw new JWTTokenValidationException("Invalid JWT signature", e);
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token");
            throw new JWTTokenValidationException("Invalid JWT token", e);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token");
            throw new JWTTokenValidationException("Expired JWT token", e);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token");
            throw new JWTTokenValidationException("Unsupported JWT token", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty");
            throw new JWTTokenValidationException("JWT claims string is empty", e);
        }
        return returnState;

    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }
}
