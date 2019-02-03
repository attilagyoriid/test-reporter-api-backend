package com.ericsson.eea.rv.testreporter.testreporter.repositories;

import com.ericsson.eea.rv.testreporter.testreporter.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserById(Long id);

    Optional<User> findUserByUsername(String username);


}
