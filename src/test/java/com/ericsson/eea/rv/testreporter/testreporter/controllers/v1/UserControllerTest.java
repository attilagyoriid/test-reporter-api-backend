package com.ericsson.eea.rv.testreporter.testreporter.controllers.v1;

import com.ericsson.eea.rv.testreporter.testreporter.domain.Role;
import com.ericsson.eea.rv.testreporter.testreporter.domain.RoleType;
import com.ericsson.eea.rv.testreporter.testreporter.domain.User;
import com.ericsson.eea.rv.testreporter.testreporter.services.UserService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean
    private UserService mockUserService;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /users/1 - Found")
    public void getUserByIDFound() throws Exception {
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(RoleType.ROLE_ADMIN));
        User user = new User("First Name", "Last Name", "username", "user@user@gmail.com", "password", roles);
        doReturn(user).when(mockUserService).findUserById(1L);
        mockMvc.perform(get( UserController.BASE_URL + "/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id", is(nullValue())))
            .andExpect(jsonPath("$.firstname", is("First Name")))
            .andExpect(jsonPath("$.lastname", is("Last Name")))
            .andExpect(jsonPath("$.username", is("username")))
            .andExpect(jsonPath("$.email", is("user@user@gmail.com")))
            .andExpect(jsonPath("$.password", is("password")))
            .andExpect(jsonPath("$.roles.length()", is(1)))
            .andExpect(jsonPath("$.roles[0].roleType", is("ROLE_ADMIN")));

    }
}