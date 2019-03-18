package com.ericsson.eea.rv.testreporter.testreporter.services;

import com.ericsson.eea.rv.testreporter.testreporter.domain.User;

public interface UserSecurityService {
    void validatePasswordResetToken(String token);

    User getUserByPasswordResetToken(String token);

    void deletePasswordResetToken(String token);
}
