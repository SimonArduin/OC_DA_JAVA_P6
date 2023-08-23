package com.openclassrooms.paymybuddy.unit;

import com.openclassrooms.paymybuddy.entity.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = UserService.class)
public class UserServiceTest {
    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;

    final User user = new User(1, 0.00,1,"email","iban","password",1,"username",null);
    @BeforeEach
    private void setUp() {
        when(userRepository.findById(any(Integer.class))).thenReturn(user);
        when(userRepository.findByUsername(any(String.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
    }

    @Test
    void contextLoads() {}

    @Nested
    class addUserTests {

        @Test
        public void addUserTest() {
            User userWithEncryptedPassword = user;
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            userWithEncryptedPassword.setPassword(passwordEncoder.encode(userWithEncryptedPassword.getPassword()));
            assertEquals(userWithEncryptedPassword, userService.addUser(user));
            verify(userRepository, Mockito.times(1)).save(any(User.class));
        }

        @Test
        public void addUserTestIfEmpty() {
            assertThrows(IllegalArgumentException.class, () -> userService.addUser(new User()));
            verify(userRepository, Mockito.times(0)).save(any(User.class));
        }

        @Test
        public void addUserTestIfNull() {
            assertThrows(IllegalArgumentException.class, () -> userService.addUser(null));
            verify(userRepository, Mockito.times(0)).save(any(User.class));
        }

        @Test
        public void addUserTestIfErrorOnSave() {
            when(userRepository.save(any(User.class))).thenThrow(new IllegalArgumentException());
            assertThrows(IllegalArgumentException.class, () -> userService.addUser(user));
            verify(userRepository, Mockito.times(1)).save(any(User.class));
        }
    }

    @Nested
    class findByIdTests {

        @Test
        public void findByIdTest() {
            assertEquals(user, userService.findById(user.getId()));
            verify(userRepository, Mockito.times(1)).findById(any(Integer.class));
        }

        @Test
        public void findByIdTestIfNotInDB() {
            when(userRepository.findById(any(Integer.class))).thenReturn(null);
            assertEquals(null, userService.findById(user.getId()));
            verify(userRepository, Mockito.times(1)).findById(any(Integer.class));
        }

        @Test
        public void findByIdTestIfNull() {
            assertThrows(IllegalArgumentException.class, () -> userService.findById(null));
            verify(userRepository, Mockito.times(0)).findById(any(Integer.class));
        }
    }

    @Nested
    class findByUsernameTests {

        @Test
        public void findByUsernameTest() {
            assertEquals(user, userService.findByUsername(user.getUsername()));
            verify(userRepository, Mockito.times(1)).findByUsername(any(String.class));
        }

        @Test
        public void findByUsernameTestIfNotInDB() {
            when(userRepository.findByUsername(any(String.class))).thenReturn(null);
            assertEquals(null, userService.findByUsername(user.getUsername()));
            verify(userRepository, Mockito.times(1)).findByUsername(any(String.class));
        }

        @Test
        public void findByUsernameTestIfNull() {
            assertThrows(IllegalArgumentException.class, () -> userService.findByUsername(null));
            verify(userRepository, Mockito.times(0)).findByUsername(any(String.class));
        }
    }
}
