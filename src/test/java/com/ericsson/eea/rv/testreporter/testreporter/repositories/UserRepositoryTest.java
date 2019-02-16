package com.ericsson.eea.rv.testreporter.testreporter.repositories;

import com.ericsson.eea.rv.testreporter.testreporter.domain.Role;
import com.ericsson.eea.rv.testreporter.testreporter.domain.RoleType;
import com.ericsson.eea.rv.testreporter.testreporter.domain.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("Find user by id 1 Found")
    public void getFindUserByIdFound() throws Exception {
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(RoleType.ROLE_ADMIN));
        User user = new User("First Name", "Last Name", "username", "user@gmail.com", "paA1_ssword", roles);

        entityManager.persist(user);

        Optional<User> userFound = userRepository.findUserById(1L);

        assertThat(userFound.isPresent()).isTrue().withFailMessage("User by id 1 has not been found!");
        assertThat(userFound.get().getId()).isEqualTo(1).withFailMessage("Id  of user 1 not match!");
        assertThat(userFound.get().isActive()).isFalse().withFailMessage("Active  of user not false!");
        assertThat(userFound.get().isEnabled()).isFalse().withFailMessage("Active  of user not false!");
        assertThat(userFound.get()).isEqualToComparingOnlyGivenFields(user,"firstname","lastname","username","email","password","roles").withFailMessage("User with id 1 not match!");


    }
}