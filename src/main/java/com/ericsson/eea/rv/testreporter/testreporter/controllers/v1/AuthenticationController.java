package com.ericsson.eea.rv.testreporter.testreporter.controllers.v1;

import com.ericsson.eea.rv.testreporter.testreporter.domain.Role;
import com.ericsson.eea.rv.testreporter.testreporter.domain.User;
import com.ericsson.eea.rv.testreporter.testreporter.error.DetailedResponseMessage;
import com.ericsson.eea.rv.testreporter.testreporter.exceptions.AlreadyExitException;
import com.ericsson.eea.rv.testreporter.testreporter.security.emailVerification.event.OnRegistrationCompleteEvent;
import com.ericsson.eea.rv.testreporter.testreporter.security.jwt.JwtProvider;
import com.ericsson.eea.rv.testreporter.testreporter.security.response.JwtResponse;
import com.ericsson.eea.rv.testreporter.testreporter.services.RoleService;
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
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private UserService userService;
    private RoleService roleService;
    private AuthenticationManager authenticationManager;
    private JwtProvider jwtProvider;
    private PasswordEncoder encoder;
    private ApplicationEventPublisher applicationEventPublisher;
    private MessageSource messageSource;


    public AuthenticationController(UserService userService, RoleService roleService,
                                    AuthenticationManager authenticationManager, JwtProvider jwtProvider,
                                    PasswordEncoder encoder, ApplicationEventPublisher applicationEventPublisher,
                                    MessageSource messageSource) {
        this.userService = userService;
        this.roleService = roleService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.encoder = encoder;
        this.applicationEventPublisher = applicationEventPublisher;
        this.messageSource = messageSource;
    }

    @PostMapping("/signup")
    public DetailedResponseMessage signUpUser(@Valid @RequestBody User user, HttpServletRequest request) {

        Set<Role> roles = user.getRoles().stream().map(r -> this.roleService.findByRoleType(r.getRoleType())).collect(Collectors.toSet());

        if (this.userService.isEmailExist(user.getEmail())) {
            throw new AlreadyExitException("Email: " + user.getEmail() + " already reserved");
        }

        if (this.userService.isUsernameExist(user.getUsername())) {
            throw new AlreadyExitException("User Name: " + user.getUsername() + " already reserved");
        }

        User userToSave = new User(user.getFirstname(), user.getLastname(), user.getUsername(), user.getEmail(), encoder.encode(user.getPassword()), roles);
        userToSave.setActive(true);
        userToSave.setEnabled(false);

        User savedUser = this.userService.save(userToSave);

        this.applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(savedUser, request.getLocale(), "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()));

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

    @GetMapping("/regitrationConfirm")
    public DetailedResponseMessage confirmRegistration
            (WebRequest request, @RequestParam("token") String token) {

        Locale locale = request.getLocale();

        final String result = userService.validateVerificationToken(token);
        String responseMessage = messageSource.getMessage("auth.message." + result, null, locale);
        DetailedResponseMessage detailedResponseMessage = new DetailedResponseMessage(new Date(), responseMessage, Collections.emptyList());


        return detailedResponseMessage;


    }


}
