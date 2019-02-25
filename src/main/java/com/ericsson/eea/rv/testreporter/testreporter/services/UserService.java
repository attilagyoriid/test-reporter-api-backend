package com.ericsson.eea.rv.testreporter.testreporter.services;

import com.ericsson.eea.rv.testreporter.testreporter.domain.PasswordResetToken;
import com.ericsson.eea.rv.testreporter.testreporter.domain.User;
import com.ericsson.eea.rv.testreporter.testreporter.domain.VerificationToken;

import java.util.Set;


public interface UserService {

    User findUserByEmail(String email);

    User findUserById(Long id);

    User save(User user);

    User findUserByUsername(String username);

    Set<User> getUsers();

    boolean isUsernameExist(String username);

    boolean isEmailExist(String email);


    User getUserByVerificationToken(String verificationToken);

    VerificationToken createVerificationTokenForUser(User user, String token);

    VerificationToken getVerificationToken(String verificationToken);

    String validateVerificationToken(String token);

    VerificationToken generateNewVerificationToken(String email);

    PasswordResetToken createPasswordResetTokenForUser(User user, String token);
}
