package com.ericsson.eea.rv.testreporter.testreporter.security.services;

import com.ericsson.eea.rv.testreporter.testreporter.security.model.UserPrinciple;
import com.ericsson.eea.rv.testreporter.testreporter.services.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        return UserPrinciple.build(userService.findUserByUsername(username));
    }
}