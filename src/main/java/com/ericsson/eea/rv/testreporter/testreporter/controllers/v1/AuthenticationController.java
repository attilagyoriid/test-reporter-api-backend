package com.ericsson.eea.rv.testreporter.testreporter.controllers.v1;

import com.ericsson.eea.rv.testreporter.testreporter.domain.PasswordResetToken;
import com.ericsson.eea.rv.testreporter.testreporter.domain.Role;
import com.ericsson.eea.rv.testreporter.testreporter.domain.User;
import com.ericsson.eea.rv.testreporter.testreporter.domain.VerificationToken;
import com.ericsson.eea.rv.testreporter.testreporter.error.DetailedResponseMessage;
import com.ericsson.eea.rv.testreporter.testreporter.exceptions.AlreadyExitException;
import com.ericsson.eea.rv.testreporter.testreporter.security.email_verification.event.OnRegistrationCompleteEvent;
import com.ericsson.eea.rv.testreporter.testreporter.security.email_verification.event.OnResendRegistrationTokenEvent;
import com.ericsson.eea.rv.testreporter.testreporter.security.email_verification.event.OnResetPasswordEvent;
import com.ericsson.eea.rv.testreporter.testreporter.security.jwt.JwtProvider;
import com.ericsson.eea.rv.testreporter.testreporter.security.response.JwtResponse;
import com.ericsson.eea.rv.testreporter.testreporter.services.RoleService;
import com.ericsson.eea.rv.testreporter.testreporter.services.UserSecurityService;
import com.ericsson.eea.rv.testreporter.testreporter.services.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    public static final String HTTP_PREFIX = "http://";
    private UserService userService;
    private RoleService roleService;
    private AuthenticationManager authenticationManager;
    private JwtProvider jwtProvider;
    private PasswordEncoder encoder;
    private ApplicationEventPublisher applicationEventPublisher;
    private MessageSource messageSource;
    private UserSecurityService userSecurityService;


    public AuthenticationController(UserService userService, RoleService roleService,
                                    AuthenticationManager authenticationManager, JwtProvider jwtProvider,
                                    PasswordEncoder encoder, ApplicationEventPublisher applicationEventPublisher,
                                    MessageSource messageSource, UserSecurityService userSecurityService) {
        this.userService = userService;
        this.roleService = roleService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.encoder = encoder;
        this.applicationEventPublisher = applicationEventPublisher;
        this.messageSource = messageSource;
        this.userSecurityService = userSecurityService;
    }

    @PostMapping("/signup")
    public DetailedResponseMessage signUpUser(@Valid @RequestBody User user, HttpServletRequest request) {

        user.getRoles().stream().map(r -> this.roleService.findByRoleType(r.getRoleType())).collect(Collectors.toSet());

        if (this.userService.isEmailExist(user.getEmail())) {
            throw new AlreadyExitException("Email: " + user.getEmail() + " already reserved");
        }

        if (this.userService.isUsernameExist(user.getUsername())) {
            throw new AlreadyExitException("User Name: " + user.getUsername() + " already reserved");
        }

        User userToSave = new User(user.getFirstname(), user.getLastname(), user.getUsername(), user.getEmail(), encoder.encode(user.getPassword()), user.getRoles());
        userToSave.setActive(true);
        userToSave.setEnabled(false);

        User savedUser = this.userService.save(userToSave);

        this.applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(savedUser, request.getLocale(), HTTP_PREFIX + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()));

        return new DetailedResponseMessage(new Date(), "Email verification sent", Collections.emptyList());

    }

    @PostMapping("/signin")
    public JwtResponse signInUser(@Valid @RequestBody User user) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        return new JwtResponse(jwt);
    }

    @GetMapping("/registrationConfirm")
    public DetailedResponseMessage confirmRegistration
            (WebRequest request, @RequestParam("token") String token) {

        Locale locale = request.getLocale();

        final String result = userService.validateVerificationToken(token);
        String responseMessage = messageSource.getMessage("auth.message." + result, null, locale);

        return new DetailedResponseMessage(new Date(), responseMessage, Collections.emptyList());
    }

    @GetMapping("/user/resendRegistrationToken")
    public DetailedResponseMessage resendRegistrationToken(final HttpServletRequest request, @RequestParam("email") final String email) {
        final VerificationToken newToken = userService.generateNewVerificationToken(email);
        final User savedUser = userService.getUserByVerificationToken(newToken.getToken());
        this.applicationEventPublisher.publishEvent(new OnResendRegistrationTokenEvent(savedUser, request.getLocale(), HTTP_PREFIX + request.getServerName() + ":" + request.getServerPort() + request.getContextPath(), newToken));
        return new DetailedResponseMessage(new Date(), "Email verification re-sent", Collections.emptyList());
    }

    @GetMapping(value = "/user/resetPassword")
    public DetailedResponseMessage resetPassword(final HttpServletRequest request, @RequestParam("email") final String userEmail) {
        final User user = userService.findUserByEmail(userEmail);
        final String tokenNew = UUID.randomUUID().toString();
        PasswordResetToken passwordResetTokenForUser = userService.createPasswordResetTokenForUser(user, tokenNew);
        this.applicationEventPublisher.publishEvent(new OnResetPasswordEvent(user, request.getLocale(), HTTP_PREFIX + request.getServerName() + ":" + request.getServerPort() + request.getContextPath(), passwordResetTokenForUser));

        return new DetailedResponseMessage(new Date(), "Password reset sent", Collections.emptyList());
    }

    @GetMapping(value = "/user/changePassword")
    public JwtResponse showChangePasswordPage(final Locale locale, @RequestParam("id") final long id, @RequestParam("token") final String token) {
        userSecurityService.validatePasswordResetToken(id, token);
        userSecurityService.deletePasswordResetToken(token);

        String jwt = jwtProvider.generateJwtToken(SecurityContextHolder.getContext().getAuthentication());
        return new JwtResponse(jwt);
    }



}
