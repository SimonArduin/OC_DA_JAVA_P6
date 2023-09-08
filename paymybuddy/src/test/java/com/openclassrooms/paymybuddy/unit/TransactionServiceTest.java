package com.openclassrooms.paymybuddy.unit;

import com.openclassrooms.paymybuddy.entity.Transaction;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.service.CommissionService;
import com.openclassrooms.paymybuddy.service.CurrencyService;
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
    private void setUp() {
        initializeVariables();
        when(transactionRepository.findById(any(Integer.class))).thenReturn(new Transaction(internalTransactionDto));
        when(transactionRepository.findBySenderId(any(Integer.class))).thenReturn(transactionList);
        when(transactionRepository.findByReceiverId(any(Integer.class))).thenReturn(transactionList);

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
            assertEquals(null, transactionService.findById(internalTransaction.getId()));
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
            assertEquals(transactionDtoList, transactionService.findBySenderId(internalTransaction.getId()));
            verify(transactionRepository, Mockito.times(1)).findBySenderId(any(Integer.class));
        }

        @Test
        public void findBySenderTestIfNotInDB() {
            when(transactionRepository.findBySenderId(any(Integer.class))).thenReturn(null);
            assertEquals(new ArrayList<>(), transactionService.findBySenderId(internalTransaction.getId()));
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
            assertEquals(transactionDtoList, transactionService.findByReceiverId(internalTransaction.getId()));
            verify(transactionRepository, Mockito.times(1)).findByReceiverId(any(Integer.class));
        }

        @Test
        public void findByReceiverTestIfNotInDB() {
            when(transactionRepository.findByReceiverId(any(Integer.class))).thenReturn(null);
            assertEquals(new ArrayList<>(), transactionService.findByReceiverId(internalTransaction.getId()));
            verify(transactionRepository, Mockito.times(1)).findByReceiverId(any(Integer.class));
        }

        @Test
        public void findByReceiverTestIfNull() {
            assertThrows(IllegalArgumentException.class, () -> transactionService.findByReceiverId(null));
            verify(transactionRepository, Mockito.times(0)).findByReceiverId(any(Integer.class));
        }
    }
}
