package com.ericsson.eea.rv.testreporter.testreporter.services;

public interface UserSecurityService {
    void validatePasswordResetToken(long id, String token);

    void deletePasswordResetToken(String token);
}
