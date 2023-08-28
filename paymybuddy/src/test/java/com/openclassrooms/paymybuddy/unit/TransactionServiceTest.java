package com.openclassrooms.paymybuddy.unit;

import com.openclassrooms.paymybuddy.dto.TransactionDto;
import com.openclassrooms.paymybuddy.dto.UserDto;
import com.openclassrooms.paymybuddy.entity.Transaction;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TransactionService.class)
public class TransactionServiceTest {
    @Autowired
    TransactionService transactionService;

    @MockBean
    TransactionRepository transactionRepository;
    @MockBean
    UserService userService;
    final UserDto userDto = new UserDto(1, 100.00,1,"email","iban","password",1,"username",new ArrayList<>());

    final Transaction transaction = new Transaction(1, 10.0, 0.05*10.0, 1, "description", "iban", 1, 2, new Timestamp(0), false);

    final TransactionDto transactionDto = new TransactionDto(transaction);
    TransactionDto transactionDtoTest = new TransactionDto(transactionDto);
    @BeforeEach
    private void setUp() {
        transactionDtoTest = new TransactionDto(transactionDto);

        when(transactionRepository.findById(any(Integer.class))).thenReturn(transaction);
        when(transactionRepository.findBySenderId(any(Integer.class))).thenReturn(transaction);
        when(transactionRepository.findByReceiverId(any(Integer.class))).thenReturn(transaction);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        when(userService.findById(any(Integer.class))).thenReturn(userDto);
    }

    @Test
    void contextLoads() {}

    @Nested
    class addTransactionTests {

        @Test
        public void addTransactionTest() {
            assertEquals(transactionDtoTest, transactionService.addTransaction(transactionDto));
            verify(transactionRepository, Mockito.times(1)).save(any(Transaction.class));
            verify(userService, Mockito.times(1)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(1)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addTransactionTestIfEmpty() {
            assertThrows(IllegalArgumentException.class, () -> transactionService.addTransaction(new TransactionDto()));
            verify(transactionRepository, Mockito.times(0)).save(any(Transaction.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addTransactionTestIfNull() {
            assertThrows(IllegalArgumentException.class, () -> transactionService.addTransaction(null));
            verify(transactionRepository, Mockito.times(0)).save(any(Transaction.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addTransactionTestIfErrorOnSave() {
            when(transactionRepository.save(any(Transaction.class))).thenThrow(new IllegalArgumentException());
            assertThrows(IllegalArgumentException.class, () -> transactionService.addTransaction(transactionDto));
            verify(transactionRepository, Mockito.times(1)).save(any(Transaction.class));
            verify(userService, Mockito.times(1)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(1)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addTransactionTestIfNoSenderFound() {
            when(userService.findById(transactionDto.getSenderId())).thenReturn(null);
            assertEquals(null, transactionService.addTransaction(transactionDto));
            verify(transactionRepository, Mockito.times(0)).save(any(Transaction.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }
        @Test
        public void addTransactionTestIfNoReceiverFound() {
            when(userService.findById(transactionDto.getReceiverId())).thenReturn(null);
            assertEquals(null, transactionService.addTransaction(transactionDto));
            verify(transactionRepository, Mockito.times(0)).save(any(Transaction.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addTransactionTestIfNotEnoughOnAccount() {
            UserDto userTest = userDto;
            userTest.setAccountBalance(0.00);
            when(userService.findById(any(Integer.class))).thenReturn(userTest);
            assertEquals(null, transactionService.addTransaction(transactionDto));
            verify(transactionRepository, Mockito.times(0)).save(any(Transaction.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }
    }

    @Nested
    class findByIdTests {

        @Test
        public void findByIdTest() {
            assertEquals(transactionDtoTest, transactionService.findById(transaction.getId()));
            verify(transactionRepository, Mockito.times(1)).findById(any(Integer.class));
        }

        @Test
        public void findByIdTestIfNotInDB() {
            when(transactionRepository.findById(any(Integer.class))).thenReturn(null);
            assertEquals(null, transactionService.findById(transaction.getId()));
            verify(transactionRepository, Mockito.times(1)).findById(any(Integer.class));
        }

        @Test
        public void findByIdTestIfNull() {
            assertThrows(IllegalArgumentException.class, () -> transactionService.findById(null));
            verify(transactionRepository, Mockito.times(0)).findById(any(Integer.class));
        }
    }

    @Nested
    class findBySenderTests {

        @Test
        public void findBySenderTest() {
            assertEquals(transactionDtoTest, transactionService.findBySenderId(transaction.getId()));
            verify(transactionRepository, Mockito.times(1)).findBySenderId(any(Integer.class));
        }

        @Test
        public void findBySenderTestIfNotInDB() {
            when(transactionRepository.findBySenderId(any(Integer.class))).thenReturn(null);
            assertEquals(null, transactionService.findBySenderId(transaction.getId()));
            verify(transactionRepository, Mockito.times(1)).findBySenderId(any(Integer.class));
        }

        @Test
        public void findBySenderTestIfNull() {
            assertThrows(IllegalArgumentException.class, () -> transactionService.findBySenderId(null));
            verify(transactionRepository, Mockito.times(0)).findBySenderId(any(Integer.class));
        }
    }

    @Nested
    class findByReceiverTests {

        @Test
        public void findByReceiverTest() {
            assertEquals(transactionDtoTest, transactionService.findByReceiverId(transaction.getId()));
            verify(transactionRepository, Mockito.times(1)).findByReceiverId(any(Integer.class));
        }

        @Test
        public void findByReceiverTestIfNotInDB() {
            when(transactionRepository.findByReceiverId(any(Integer.class))).thenReturn(null);
            assertEquals(null, transactionService.findByReceiverId(transaction.getId()));
            verify(transactionRepository, Mockito.times(1)).findByReceiverId(any(Integer.class));
        }

        @Test
        public void findByReceiverTestIfNull() {
            assertThrows(IllegalArgumentException.class, () -> transactionService.findByReceiverId(null));
            verify(transactionRepository, Mockito.times(0)).findByReceiverId(any(Integer.class));
        }
    }
}
