package com.openclassrooms.paymybuddy.unit;

import com.openclassrooms.paymybuddy.entity.Transaction;
import com.openclassrooms.paymybuddy.entity.User;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

    final Transaction transaction = new Transaction(1, 10.0, 1.0, 1, "description", "iban", 1, 2, new Timestamp(0), false);
    @BeforeEach
    private void setUp() {
        when(transactionRepository.findById(any(Integer.class))).thenReturn(transaction);
        when(transactionRepository.findBySender(any(Integer.class))).thenReturn(transaction);
        when(transactionRepository.findByReceiver(any(Integer.class))).thenReturn(transaction);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
    }

    @Test
    void contextLoads() {}

    @Nested
    class addTransactionTests {

        @Test
        public void addTransactionTest() {
            assertEquals(transaction, transactionService.addTransaction(transaction));
            verify(transactionRepository, Mockito.times(1)).save(any(Transaction.class));
        }

        @Test
        public void addTransactionTestIfEmpty() {
            assertThrows(IllegalArgumentException.class, () -> transactionService.addTransaction(new Transaction()));
            verify(transactionRepository, Mockito.times(0)).save(any(Transaction.class));
        }

        @Test
        public void addTransactionTestIfNull() {
            assertThrows(IllegalArgumentException.class, () -> transactionService.addTransaction(null));
            verify(transactionRepository, Mockito.times(0)).save(any(Transaction.class));
        }

        @Test
        public void addTransactionTestIfErrorOnSave() {
            when(transactionRepository.save(any(Transaction.class))).thenThrow(new IllegalArgumentException());
            assertThrows(IllegalArgumentException.class, () -> transactionService.addTransaction(transaction));
            verify(transactionRepository, Mockito.times(1)).save(any(Transaction.class));
        }
    }

    @Nested
    class findByIdTests {

        @Test
        public void findByIdTest() {
            assertEquals(transaction, transactionService.findById(transaction.getId()));
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
            assertEquals(transaction, transactionService.findBySenderId(transaction.getId()));
            verify(transactionRepository, Mockito.times(1)).findBySender(any(Integer.class));
        }

        @Test
        public void findBySenderTestIfNotInDB() {
            when(transactionRepository.findBySender(any(Integer.class))).thenReturn(null);
            assertEquals(null, transactionService.findBySenderId(transaction.getId()));
            verify(transactionRepository, Mockito.times(1)).findBySender(any(Integer.class));
        }

        @Test
        public void findBySenderTestIfNull() {
            assertThrows(IllegalArgumentException.class, () -> transactionService.findBySenderId(null));
            verify(transactionRepository, Mockito.times(0)).findBySender(any(Integer.class));
        }
    }

    @Nested
    class findByReceiverTests {

        @Test
        public void findByReceiverTest() {
            assertEquals(transaction, transactionService.findByReceiverId(transaction.getId()));
            verify(transactionRepository, Mockito.times(1)).findByReceiver(any(Integer.class));
        }

        @Test
        public void findByReceiverTestIfNotInDB() {
            when(transactionRepository.findByReceiver(any(Integer.class))).thenReturn(null);
            assertEquals(null, transactionService.findByReceiverId(transaction.getId()));
            verify(transactionRepository, Mockito.times(1)).findByReceiver(any(Integer.class));
        }

        @Test
        public void findByReceiverTestIfNull() {
            assertThrows(IllegalArgumentException.class, () -> transactionService.findByReceiverId(null));
            verify(transactionRepository, Mockito.times(0)).findByReceiver(any(Integer.class));
        }
    }
}
