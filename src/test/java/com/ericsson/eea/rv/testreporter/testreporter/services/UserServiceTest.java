package com.ericsson.eea.rv.testreporter.testreporter.services;

import com.ericsson.eea.rv.testreporter.testreporter.domain.Role;
import com.ericsson.eea.rv.testreporter.testreporter.domain.RoleType;
import com.ericsson.eea.rv.testreporter.testreporter.domain.User;
import com.ericsson.eea.rv.testreporter.testreporter.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    UserService userService;
    @Mock
    UserRepository mockUserRepository;


    @BeforeEach
    public void setUp() throws Exception {

        userService = new UserServiceImpl(mockUserRepository);
    }
    @Test
    @DisplayName("Find user by id 1 Found")
    public void getFindUserByIdFound() throws Exception {
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(RoleType.ROLE_ADMIN));
        User user = new User("First Name", "Last Name", "username", "user@user@gmail.com", "password", roles);
        when(mockUserRepository.findUserById(1L)).thenReturn(Optional.of(user));

        User userFound = userService.findUserById(1L);
        assertThat(userFound).isNotNull().withFailMessage("User by id 1 has not been found!");
        assertThat(userFound).isEqualTo(user).withFailMessage("User by id 1 does not match!");
    }

}