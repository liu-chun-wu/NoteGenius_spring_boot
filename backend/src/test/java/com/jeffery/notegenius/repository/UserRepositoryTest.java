package com.jeffery.notegenius.repository;

import com.jeffery.notegenius.model.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUsername() {
        User user = new User();
        user.setUsername("jeffery");
        user.setEmail("jeffery@example.com");
        user.setPassword("123456");
        userRepository.save(user);

        Optional<User> result = userRepository.findByUsername("jeffery");
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("jeffery@example.com");
    }

    @Test
    public void testFindByEmail() {
        User user = new User();
        user.setUsername("tester");
        user.setEmail("tester@example.com");
        user.setPassword("password");
        userRepository.save(user);

        Optional<User> result = userRepository.findByEmail("tester@example.com");
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("tester");
    }
}
