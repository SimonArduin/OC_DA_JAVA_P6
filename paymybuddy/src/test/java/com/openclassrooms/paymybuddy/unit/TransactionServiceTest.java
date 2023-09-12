package com.openclassrooms.paymybuddy.unit;

import com.openclassrooms.paymybuddy.TestVariables;
import com.openclassrooms.paymybuddy.entity.Transaction;
import com.openclassrooms.paymybuddy.exception.TransactionNotFoundException;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TransactionService.class)
public class TransactionServiceTest extends TestVariables {
    @Autowired
    TransactionService transactionService;

    @MockBean
    TransactionRepository transactionRepository;
    @MockBean
    UserService userService;
    @MockBean
    CurrencyService currencyService;
    @MockBean
    CommissionService commissionService;

    @BeforeEach
    public void setUp() {
        initializeVariables();
        when(transactionRepository.findById(any(Integer.class))).thenReturn(new Transaction(internalTransactionDto));
        when(transactionRepository.findBySenderId(any(Integer.class))).thenReturn(transactionList);
        when(transactionRepository.findByReceiverId(any(Integer.class))).thenReturn(transactionList);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        when(userService.findById(any(Integer.class))).thenReturn(userDto);

        when(commissionService.findById(any(Integer.class))).thenReturn(commission);

        when(currencyService.findById(any(Integer.class))).thenReturn(currency);
    }

    @Test
    void contextLoads() {}
    @Nested
    class findByIdTests {

        @Test
        public void findByIdTestIfInternalTransaction() {
            assertEquals(internalTransactionDto, transactionService.findById(internalTransaction.getId()));
            verify(transactionRepository, Mockito.times(1)).findById(any(Integer.class));
        }

        @Test
        public void findByIdTestIfExternalTransaction() {
            when(transactionRepository.findById(any(Integer.class))).thenReturn(new Transaction(externalTransactionDto));
            assertEquals(externalTransactionDto, transactionService.findById(internalTransaction.getId()));
            verify(transactionRepository, Mockito.times(1)).findById(any(Integer.class));
        }

        @Test
        public void findByIdTestIfInvalidTransaction() {
            when(transactionRepository.findById(any(Integer.class))).thenReturn(transaction);
            assertThrows(IllegalArgumentException.class, () -> transactionService.findById(internalTransaction.getId()));
            verify(transactionRepository, Mockito.times(1)).findById(any(Integer.class));
        }

        @Test
        public void findByIdTestIfNotInDB() {
            when(transactionRepository.findById(any(Integer.class))).thenReturn(null);
            assertThrows(TransactionNotFoundException.class, () -> transactionService.findById(internalTransaction.getId()));
            verify(transactionRepository, Mockito.times(1)).findById(any(Integer.class));
        }

        @Test
        public void findByIdTestIfNull() {
            assertThrows(IllegalArgumentException.class, () -> transactionService.findById(null));
            verify(transactionRepository, Mockito.times(0)).findById(any(Integer.class));
        }
    }

    @Nested
    class findBySenderIdTests {

        @Test
        public void findBySenderIdTest() {
            assertEquals(transactionDtoList, transactionService.findBySenderId(internalTransaction.getId()));
            verify(transactionRepository, Mockito.times(1)).findBySenderId(any(Integer.class));
        }

        @Test
        public void findBySenderIdTestIfNotInDB() {
            when(transactionRepository.findBySenderId(any(Integer.class))).thenReturn(null);
            assertEquals(new ArrayList<>(), transactionService.findBySenderId(internalTransaction.getId()));
            verify(transactionRepository, Mockito.times(1)).findBySenderId(any(Integer.class));
        }

        @Test
        public void findBySenderIdTestIfInvalidTransaction() {
            when(transactionRepository.findBySenderId(any(Integer.class))).thenReturn(new ArrayList<>(List.of(transaction)));
            assertThrows(IllegalArgumentException.class, () -> transactionService.findBySenderId(internalTransaction.getId()));
            verify(transactionRepository, Mockito.times(1)).findBySenderId(any(Integer.class));
        }

        @Test
        public void findBySenderIdTestIfNull() {
            assertThrows(IllegalArgumentException.class, () -> transactionService.findBySenderId(null));
            verify(transactionRepository, Mockito.times(0)).findBySenderId(any(Integer.class));
        }
    }

    @Nested
    class findByReceiverIdTests {

        @Test
        public void findByReceiverIdTest() {
            assertEquals(transactionDtoList, transactionService.findByReceiverId(internalTransaction.getId()));
            verify(transactionRepository, Mockito.times(1)).findByReceiverId(any(Integer.class));
        }

        @Test
        public void findByReceiverIdTestIfNotInDB() {
            when(transactionRepository.findByReceiverId(any(Integer.class))).thenReturn(null);
            assertEquals(new ArrayList<>(), transactionService.findByReceiverId(internalTransaction.getId()));
            verify(transactionRepository, Mockito.times(1)).findByReceiverId(any(Integer.class));
        }

        @Test
        public void findByReceiverIdTestIfInvalidTransaction() {
            when(transactionRepository.findByReceiverId(any(Integer.class))).thenReturn(new ArrayList<>(List.of(transaction)));
            assertThrows(IllegalArgumentException.class, () -> transactionService.findByReceiverId(internalTransaction.getId()));
            verify(transactionRepository, Mockito.times(1)).findByReceiverId(any(Integer.class));
        }

        @Test
        public void findByReceiverIdTestIfNull() {
            assertThrows(IllegalArgumentException.class, () -> transactionService.findByReceiverId(null));
            verify(transactionRepository, Mockito.times(0)).findByReceiverId(any(Integer.class));
        }
    }
    @Nested
    class addTransactionTests {

        @Test
        public void addTransactionIfInternalTest() {
            when(transactionRepository.save(any(Transaction.class))).thenReturn(internalTransaction);
            assertEquals(internalTransactionDto, transactionService.addTransaction(internalTransactionDto));
            verify(transactionRepository, Mockito.times(1)).save(any(Transaction.class));
        }

        @Test
        public void addTransactionIfExternalTest() {
            when(transactionRepository.save(any(Transaction.class))).thenReturn(externalTransaction);
            assertEquals(externalTransactionDto, transactionService.addTransaction(externalTransactionDto));
            verify(transactionRepository, Mockito.times(1)).save(any(Transaction.class));
        }

        @Test
        public void addTransactionTestIfNull() {
            assertThrows(IllegalArgumentException.class, () -> transactionService.addTransaction(null));
            verify(transactionRepository, Mockito.times(0)).save(any(Transaction.class));
        }

        @Test
        public void addTransactionTestIfEmpty() {
            assertThrows(IllegalArgumentException.class, () -> transactionService.addTransaction(null));
            verify(transactionRepository, Mockito.times(0)).save(any(Transaction.class));
        }
    }
}
