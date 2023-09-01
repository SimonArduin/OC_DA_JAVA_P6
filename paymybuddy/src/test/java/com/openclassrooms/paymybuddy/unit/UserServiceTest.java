package com.openclassrooms.paymybuddy.unit;

import com.openclassrooms.paymybuddy.dto.UserDto;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = UserService.class)
public class UserServiceTest extends TestVariables {
    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;

    Double amount;

    @BeforeEach
    private void setUp() {
        initializeVariables();
        amount = transaction.getAmount();

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
            assertEquals(new UserDto(user), userService.addUser(userDto));
            verify(userRepository, Mockito.times(1)).save(any(User.class));
        }

        @Test
        public void addUserTestIfEmpty() {
            assertThrows(IllegalArgumentException.class, () -> userService.addUser(new UserDto()));
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
            assertThrows(IllegalArgumentException.class, () -> userService.addUser(userDto));
            verify(userRepository, Mockito.times(1)).save(any(User.class));
        }
    }

    @Nested
    class addConnectionToUserTests {

        @Test
        public void addConnectionToUserTest() {
            assertEquals(new UserDto(user), userService.addConnectionToUser(userDto, userDtoOther));
            verify(userRepository, Mockito.times(1)).save(any(User.class));
        }

        @Test
        public void addConnectionToUserTestIfConnectionEmpty() {
            assertThrows(IllegalArgumentException.class, () -> userService.addConnectionToUser(userDto, new UserDto()));
            verify(userRepository, Mockito.times(0)).save(any(User.class));
        }

        @Test
        public void addConnectionToUserTestIfConnectionNull() {
            assertThrows(IllegalArgumentException.class, () -> userService.addConnectionToUser(userDto,null));
            verify(userRepository, Mockito.times(0)).save(any(User.class));
        }

        @Test
        public void addConnectionToUserTestIfUserNull() {
            assertThrows(IllegalArgumentException.class, () -> userService.addConnectionToUser(null,userDto));
            verify(userRepository, Mockito.times(0)).save(any(User.class));
        }

        @Test
        public void addConnectionToUserTestIfErrorOnSave() {
            when(userRepository.save(any(User.class))).thenThrow(new IllegalArgumentException());
            assertThrows(IllegalArgumentException.class, () -> userService.addConnectionToUser(userDto, userDtoOther));
            verify(userRepository, Mockito.times(1)).save(any(User.class));
        }

        @Test
        public void addConnectionToUserTestIfSame() {
            assertEquals(null, userService.addConnectionToUser(userDto, userDto));
            verify(userRepository, Mockito.times(0)).save(any(User.class));
        }
    }

    @Nested
    class findByIdTests {

        @Test
        public void findByIdTest() {
            assertEquals(userDto, userService.findById(user.getId()));
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
            assertEquals(userDto, userService.findByUsername(user.getUsername()));
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
            assertEquals(new UserDto(user), userService.removeFromAccountBalance(userDto, amount));
            verify(userRepository, Mockito.times(1)).save(any(User.class));
        }

        @Test
        public void removeFromAccountBalanceTestIfUserEmpty() {
            assertThrows(IllegalArgumentException.class, () -> userService.removeFromAccountBalance(new UserDto(), amount));
            verify(userRepository, Mockito.times(0)).save(any(User.class));
        }

        @Test
        public void removeFromAccountBalanceTestIfUserNull() {
            assertThrows(IllegalArgumentException.class, () -> userService.removeFromAccountBalance(null, amount));
            verify(userRepository, Mockito.times(0)).save(any(User.class));
        }
        @Test
        public void removeFromAccountBalanceTestIfAmountNegative() {
            assertThrows(IllegalArgumentException.class, () -> userService.removeFromAccountBalance(userDto, -amount));
            verify(userRepository, Mockito.times(0)).save(any(User.class));
        }
        @Test
        public void removeFromAccountBalanceTestIfAmountZero() {
            assertThrows(IllegalArgumentException.class, () -> userService.removeFromAccountBalance(userDto, 0.0));
            verify(userRepository, Mockito.times(0)).save(any(User.class));
        }
        @Test
        public void removeFromAccountBalanceTestIfAmountNull() {
            assertThrows(IllegalArgumentException.class, () -> userService.removeFromAccountBalance(userDto, null));
            verify(userRepository, Mockito.times(0)).save(any(User.class));
        }

        @Test
        public void removeFromAccountBalanceTestIfAmountMoreThanAccountBalance() {
            assertEquals(null, userService.removeFromAccountBalance(userDto, user.getAccountBalance() + amount));
            verify(userRepository, Mockito.times(0)).save(any(User.class));
        }

        @Test
        public void removeFromAccountBalanceTestIfErrorOnSave() {
            when(userRepository.save(any(User.class))).thenThrow(new IllegalArgumentException());
            assertThrows(IllegalArgumentException.class, () -> userService.removeFromAccountBalance(userDto, amount));
            verify(userRepository, Mockito.times(1)).save(any(User.class));
        }
    }

    @Nested
    class addToAccountBalanceTests {

        @Test
        public void addToAccountBalanceTest() {
            assertEquals(new UserDto(user), userService.addToAccountBalance(userDto, amount));
            verify(userRepository, Mockito.times(1)).save(any(User.class));
        }

        @Test
        public void addToAccountBalanceTestIfUserEmpty() {
            assertThrows(IllegalArgumentException.class, () -> userService.addToAccountBalance(new UserDto(), amount));
            verify(userRepository, Mockito.times(0)).save(any(User.class));
        }

        @Test
        public void addToAccountBalanceTestIfUserNull() {
            assertThrows(IllegalArgumentException.class, () -> userService.addToAccountBalance(null, amount));
            verify(userRepository, Mockito.times(0)).save(any(User.class));
        }
        @Test
        public void addToAccountBalanceTestIfAmountNegative() {
            assertThrows(IllegalArgumentException.class, () -> userService.addToAccountBalance(userDto, -amount));
            verify(userRepository, Mockito.times(0)).save(any(User.class));
        }
        @Test
        public void addToAccountBalanceTestIfAmountZero() {
            assertThrows(IllegalArgumentException.class, () -> userService.addToAccountBalance(userDto, 0.0));
            verify(userRepository, Mockito.times(0)).save(any(User.class));
        }
        @Test
        public void addToAccountBalanceTestIfAmountNull() {
            assertThrows(IllegalArgumentException.class, () -> userService.addToAccountBalance(userDto, null));
            verify(userRepository, Mockito.times(0)).save(any(User.class));
        }

        @Test
        public void addToAccountBalanceTestIfErrorOnSave() {
            when(userRepository.save(any(User.class))).thenThrow(new IllegalArgumentException());
            assertThrows(IllegalArgumentException.class, () -> userService.addToAccountBalance(userDto, amount));
            verify(userRepository, Mockito.times(1)).save(any(User.class));
        }
    }
}
