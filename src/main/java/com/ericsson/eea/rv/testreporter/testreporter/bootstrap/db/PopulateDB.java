package com.ericsson.eea.rv.testreporter.testreporter.bootstrap.db;

import com.ericsson.eea.rv.testreporter.testreporter.domain.Role;
import com.ericsson.eea.rv.testreporter.testreporter.domain.RoleType;
import com.ericsson.eea.rv.testreporter.testreporter.domain.User;
import com.ericsson.eea.rv.testreporter.testreporter.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@Profile({"default"})
public class PopulateDB implements ApplicationListener<ContextRefreshedEvent> {

    private UserRepository userRepository;

    public PopulateDB(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        this.userRepository.saveAll(loadUsers());
    }

    private List<User> loadUsers() {
        User user1 = new User();
        user1.setActive(true);
        user1.setEmail("majom@majom.com");
        user1.setFirstname("Attila");
        user1.setLastname("Gyori");
        user1.setPassword("xxxx1-Nqwe");
        user1.setUsername("seggfejke");

        User user2 = new User();
        user2.setActive(false);
        user2.setEmail("feri@majom.com");
        user2.setFirstname("Feri");
        user2.setLastname("Gyori");
        user2.setPassword("xxxx1-Nqwe");
        user2.setUsername("seggfejke2");

        User user3 = new User();
        user3.setActive(false);
        user3.setEmail("zoli@majom.com");
        user3.setFirstname("Zoli");
        user3.setLastname("Gyori");
        user3.setPassword("xxxx1-Nqwe");
        user3.setUsername("seggfejkex");

        Role role1 = new Role(RoleType.ROLE_ADMIN);
        Role role2 = new Role(RoleType.ROLE_EVALUATOR);
        Role role3 = new Role(RoleType.ROLE_READER);

        user1.setRoles(Stream.of(role1, role3)
                .collect(Collectors.toSet()));

        user2.setRoles(Stream.of(role1, role2)
                .collect(Collectors.toSet()));

        user3.setRoles(Stream.of(role2, role3)
                .collect(Collectors.toSet()));

        return Arrays.asList(user1, user2, user3);
    }
}
