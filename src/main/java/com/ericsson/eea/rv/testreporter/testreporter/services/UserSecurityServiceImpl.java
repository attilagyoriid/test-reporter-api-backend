package com.ericsson.eea.rv.testreporter.testreporter.services;

import com.ericsson.eea.rv.testreporter.testreporter.domain.PasswordResetToken;
import com.ericsson.eea.rv.testreporter.testreporter.domain.User;
import com.ericsson.eea.rv.testreporter.testreporter.repositories.PasswordResetTokenRepository;
import com.ericsson.eea.rv.testreporter.testreporter.repositories.UserRepository;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Collections;

@Service
@Transactional
public class UserSecurityServiceImpl implements UserSecurityService {


    private UserRepository userRepository;
    private PasswordResetTokenRepository passwordTokenRepository;
    private MessageSource messageSource;

    public UserSecurityServiceImpl(UserRepository userRepository, PasswordResetTokenRepository passwordTokenRepository, MessageSource messageSource) {
        this.userRepository = userRepository;
        this.passwordTokenRepository = passwordTokenRepository;
        this.messageSource = messageSource;
    }

    @Override
    public void validatePasswordResetToken(String token) {
        final PasswordResetToken passToken = passwordTokenRepository.findByToken(token);
        if ((passToken == null)) {
            throw new BadCredentialsException(messageSource.getMessage("auth.message.toke.invalid", null, LocaleContextHolder.getLocale()));
        }

        final Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            this.passwordTokenRepository.delete(passToken);
            throw new BadCredentialsException(messageSource.getMessage("auth.message.toke.expired", null, LocaleContextHolder.getLocale()));
        }
//
//        final User user = passToken.getUser();
//        final Authentication auth = new UsernamePasswordAuthenticationToken(user, null, Collections.singleton(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
//        SecurityContextHolder.getContext().setAuthentication(auth);

    }

    @Override
    public User getUserByPasswordResetToken(String token) {
        final PasswordResetToken passToken = passwordTokenRepository.findByToken(token);
        if ((passToken == null)) {
            throw new BadCredentialsException(messageSource.getMessage("auth.message.toke.invalid", null, LocaleContextHolder.getLocale()));
        }
        final User user = passToken.getUser();
        return user;
    }

    @Override
    public void deletePasswordResetToken(String token) {
        final PasswordResetToken passToken = passwordTokenRepository.findByToken(token);
        this.passwordTokenRepository.delete(passToken);
    }



}
