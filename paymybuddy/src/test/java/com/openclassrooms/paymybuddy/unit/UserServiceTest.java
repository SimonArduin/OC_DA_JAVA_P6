package com.openclassrooms.paymybuddy.unit;

import com.openclassrooms.paymybuddy.entity.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.service.UserService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

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

    final User user = new User(1, 100.00,1,"email","iban","password",1,"username",new ArrayList<>());
    final Double amount = 10.0;

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
    
    @Nested
    class removeFromAccountBalanceTests {

        @Test
        public void removeFromAccountBalanceTest() {
            assertEquals(user, userService.removeFromAccountBalance(user, amount));
            verify(userRepository, Mockito.times(1)).save(any(User.class));
        }

        @Test
        public void removeFromAccountBalanceTestIfUserEmpty() {
            assertThrows(IllegalArgumentException.class, () -> userService.removeFromAccountBalance(new User(), amount));
            verify(userRepository, Mockito.times(0)).save(any(User.class));
        }

        @Test
        public void removeFromAccountBalanceTestIfUserNull() {
            assertThrows(IllegalArgumentException.class, () -> userService.removeFromAccountBalance(null, amount));
            verify(userRepository, Mockito.times(0)).save(any(User.class));
        }
        @Test
        public void removeFromAccountBalanceTestIfAmountNegative() {
            assertThrows(IllegalArgumentException.class, () -> userService.removeFromAccountBalance(user, -amount));
            verify(userRepository, Mockito.times(0)).save(any(User.class));
        }
        @Test
        public void removeFromAccountBalanceTestIfAmountZero() {
            assertThrows(IllegalArgumentException.class, () -> userService.removeFromAccountBalance(user, 0.0));
            verify(userRepository, Mockito.times(0)).save(any(User.class));
        }
        @Test
        public void removeFromAccountBalanceTestIfAmountNull() {
            assertThrows(IllegalArgumentException.class, () -> userService.removeFromAccountBalance(user, null));
            verify(userRepository, Mockito.times(0)).save(any(User.class));
        }

        @Test
        public void removeFromAccountBalanceTestIfAmountMoreThanAccountBalance() {
            assertEquals(null, userService.removeFromAccountBalance(user, user.getAccount_balance() + amount));
            verify(userRepository, Mockito.times(0)).save(any(User.class));
        }

        @Test
        public void removeFromAccountBalanceTestIfErrorOnSave() {
            when(userRepository.save(any(User.class))).thenThrow(new IllegalArgumentException());
            assertThrows(IllegalArgumentException.class, () -> userService.removeFromAccountBalance(user, amount));
            verify(userRepository, Mockito.times(1)).save(any(User.class));
        }
    }

    @Nested
    class addToAccountBalanceTests {

        @Test
        public void addToAccountBalanceTest() {
            assertEquals(user, userService.addToAccountBalance(user, amount));
            verify(userRepository, Mockito.times(1)).save(any(User.class));
        }

        @Test
        public void addToAccountBalanceTestIfUserEmpty() {
            assertThrows(IllegalArgumentException.class, () -> userService.addToAccountBalance(new User(), amount));
            verify(userRepository, Mockito.times(0)).save(any(User.class));
        }

        @Test
        public void addToAccountBalanceTestIfUserNull() {
            assertThrows(IllegalArgumentException.class, () -> userService.addToAccountBalance(null, amount));
            verify(userRepository, Mockito.times(0)).save(any(User.class));
        }
        @Test
        public void addToAccountBalanceTestIfAmountNegative() {
            assertThrows(IllegalArgumentException.class, () -> userService.addToAccountBalance(user, -amount));
            verify(userRepository, Mockito.times(0)).save(any(User.class));
        }
        @Test
        public void addToAccountBalanceTestIfAmountZero() {
            assertThrows(IllegalArgumentException.class, () -> userService.addToAccountBalance(user, 0.0));
            verify(userRepository, Mockito.times(0)).save(any(User.class));
        }
        @Test
        public void addToAccountBalanceTestIfAmountNull() {
            assertThrows(IllegalArgumentException.class, () -> userService.addToAccountBalance(user, null));
            verify(userRepository, Mockito.times(0)).save(any(User.class));
        }

        @Test
        public void addToAccountBalanceTestIfErrorOnSave() {
            when(userRepository.save(any(User.class))).thenThrow(new IllegalArgumentException());
            assertThrows(IllegalArgumentException.class, () -> userService.addToAccountBalance(user, amount));
            verify(userRepository, Mockito.times(1)).save(any(User.class));
        }
    }
}
