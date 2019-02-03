package com.ericsson.eea.rv.testreporter.testreporter.services;

import com.ericsson.eea.rv.testreporter.testreporter.domain.User;
import com.ericsson.eea.rv.testreporter.testreporter.domain.VerificationToken;
import com.ericsson.eea.rv.testreporter.testreporter.exceptions.NotFoundException;
import com.ericsson.eea.rv.testreporter.testreporter.repositories.UserRepository;
import com.ericsson.eea.rv.testreporter.testreporter.repositories.VerificationTokenRepository;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class UserServiceImpl implements UserService {

    private static final String TOKEN_INVALID = "invalidToken";
    private static final String TOKEN_EXPIRED = "expired";
    private static final String TOKEN_VALID = "valid";

    private UserRepository userRepository;

    private VerificationTokenRepository tokenRepository;
    private MessageSource messageSource;

    public UserServiceImpl(UserRepository userRepository, VerificationTokenRepository tokenRepository, MessageSource messageSource) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.messageSource = messageSource;
    }

    @Override
    public User findUserByEmail(String email) {
        return this.userRepository.findUserByEmail(email).orElseThrow(() -> new NotFoundException("User Not found with email: " + email));
    }

    @Override
    public User findUserById(Long id) {
        return this.userRepository.findUserById(id).orElseThrow(() -> new NotFoundException("User Not found with id: " + id));
    }

    @Override
    public User save(User user) {

        return this.userRepository.save(user);
    }

    @Override
    public User findUserByUsername(String username) {
        return this.userRepository.findUserByUsername(username).orElseThrow(() -> new NotFoundException("User Name Not found: " + username));
    }

    @Override
    public boolean isUsernameExist(String username) {
        return this.userRepository.findUserByUsername(username).isPresent();
    }

    @Override
    public boolean isEmailExist(String email) {
        return this.userRepository.findUserByEmail(email).isPresent();
    }

    @Override
    public User getUserByVerificationToken(final String verificationToken) {
        final VerificationToken token = tokenRepository.findByToken(verificationToken);
        if (token != null) {
            return token.getUser();
        }
        return null;
    }

    @Override
    public void createVerificationTokenForUser(final User user, final String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    @Override
    public VerificationToken getVerificationToken(final String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

    @Override
    public String validateVerificationToken(String token) {
        final VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            throw new BadCredentialsException(messageSource.getMessage("auth.message." + TOKEN_INVALID, null, LocaleContextHolder.getLocale()));
        }

        final User user = verificationToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate()
                .getTime()
                - cal.getTime()
                .getTime()) <= 0) {
            tokenRepository.delete(verificationToken);
            throw new BadCredentialsException(messageSource.getMessage("auth.message." + TOKEN_EXPIRED, null, LocaleContextHolder.getLocale()));
        }

        user.setEnabled(true);
        // tokenRepository.delete(verificationToken);
        userRepository.save(user);
        return TOKEN_VALID;
    }
}
