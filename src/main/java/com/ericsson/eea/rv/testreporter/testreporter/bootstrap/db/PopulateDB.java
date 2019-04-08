package com.ericsson.eea.rv.testreporter.testreporter.bootstrap.db;

import com.ericsson.eea.rv.testreporter.testreporter.domain.Role;
import com.ericsson.eea.rv.testreporter.testreporter.domain.RoleType;
import com.ericsson.eea.rv.testreporter.testreporter.domain.User;
import com.ericsson.eea.rv.testreporter.testreporter.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@Profile({"default"})
public class PopulateDB implements ApplicationListener<ContextRefreshedEvent> {

    @Value("classpath:imgs/monkey_avatar.jpg")
    Resource resourceFile;
    @Autowired
    private PasswordEncoder encoder;

    private UserRepository userRepository;

    public PopulateDB(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        this.userRepository.saveAll(loadUsers());
    }

    private List<User> loadUsers() {

        byte[] image = convertResourceTo(resourceFile);

        User user1 = new User();
        user1.setActive(true);
        user1.setEmail("majom@majom.com");
        user1.setFirstname("Attila");
        user1.setLastname("Kedves");
        user1.setPassword(encoder.encode("test"));
        user1.setUsername("test");
        user1.setImage(image);
        user1.setEnabled(true);

        User user2 = new User();
        user2.setActive(false);
        user2.setEmail("feri@majom.com");
        user2.setFirstname("Feri");
        user2.setLastname("Keleti");
        user2.setPassword(encoder.encode("test"));
        user2.setUsername("test1");
        user2.setImage(image);

        User user3 = new User();
        user3.setActive(false);
        user3.setEmail("zoli@majom.com");
        user3.setFirstname("Zoli");
        user3.setLastname("Nyugati");
        user3.setPassword(encoder.encode("test"));
        user3.setUsername("test2");
        user3.setImage(null);

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

    private byte[] convertResourceTo(Resource resource)
    {
        byte[] fileBytes = null;
        try
        {
            fileBytes = Files.readAllBytes(resource.getFile().toPath());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return fileBytes;
    }
}
