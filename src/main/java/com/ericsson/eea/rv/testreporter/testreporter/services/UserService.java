package com.ericsson.eea.rv.testreporter.testreporter.services;

import com.ericsson.eea.rv.testreporter.testreporter.domain.User;
import com.ericsson.eea.rv.testreporter.testreporter.domain.VerificationToken;


public interface UserService {

    User findUserByEmail(String email);

    User findUserById(Long id);

    User save(User user);

    User findUserByUsername(String username);

    boolean isUsernameExist(String username);

    boolean isEmailExist(String email);


    User getUserByVerificationToken(String verificationToken);

    void createVerificationTokenForUser(User user, String token);

    VerificationToken getVerificationToken(String VerificationToken);

    String validateVerificationToken(String token);
}
