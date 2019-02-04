package com.ericsson.eea.rv.testreporter.testreporter.controllers.v1;

import com.ericsson.eea.rv.testreporter.testreporter.domain.User;
import com.ericsson.eea.rv.testreporter.testreporter.security.model.UserPrinciple;
import com.ericsson.eea.rv.testreporter.testreporter.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping(UserController.BASE_URL)
public class UserController {
    public static final String BASE_URL = "/api/v1/users";
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("email/{email}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserByEmail(@PathVariable("email") String email) {
        return this.userService.findUserByEmail(email);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable("id") Long id) {
        return this.userService.findUserById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("username/{username}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable("username") String username) {
        return this.userService.findUserByUsername(username);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('READER') or hasRole('EVALUATOR')")
    @GetMapping("/account")
    @ResponseStatus(HttpStatus.OK)
    public User getUserDetails(Authentication principal) {
        if (principal.getPrincipal() == null) {
            throw new RuntimeException("Not signed in");
        }
        return this.userService.findUserByEmail(((UserPrinciple) principal.getPrincipal()).getEmail());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/users")
    public Set<User> listUsers() {
        return this.userService.getUsers();
    }



}
