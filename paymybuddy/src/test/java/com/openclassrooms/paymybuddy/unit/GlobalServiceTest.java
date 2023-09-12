package com.openclassrooms.paymybuddy.unit;

import com.openclassrooms.paymybuddy.TestVariables;
import com.openclassrooms.paymybuddy.dto.*;
import com.openclassrooms.paymybuddy.entity.Transaction;
import com.openclassrooms.paymybuddy.exception.UserNotFoundException;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = GlobalService.class)
public class GlobalServiceTest extends TestVariables {
    @Autowired
    GlobalService globalService;
    
    @MockBean
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
        
        when(transactionService.findBySenderId(any(Integer.class))).thenReturn(transactionDtoList);
        when(transactionService.findByReceiverId(any(Integer.class))).thenReturn(transactionDtoList);
        when(transactionService.addTransaction(any(ExternalTransactionDto.class))).thenReturn(externalTransactionDto);
        when(transactionService.addTransaction(any(InternalTransactionDto.class))).thenReturn(internalTransactionDto);
        
        when(userService.findById(any(Integer.class))).thenReturn(userDto);

        when(commissionService.findById(any(Integer.class))).thenReturn(commission);

        when(currencyService.findById(any(Integer.class))).thenReturn(currency);
    }

    @Test
    void contextLoads() {}

    @Nested
    class addInternalTransactionTests {

        @Test
        public void addInternalTransactionTest() {
            InternalTransactionDto expectedResult = new InternalTransactionDto(internalTransaction);
            InternalTransactionDto actualResult = globalService.addInternalTransaction(internalTransactionDto);
            actualResult.setTimestamp(expectedResult.getTimestamp());
            assertEquals(expectedResult, actualResult);
            verify(transactionService, Mockito.times(1)).addTransaction(any(InternalTransactionDto.class));
            verify(userService, Mockito.times(2)).findById(any(Integer.class));
            verify(userService, Mockito.times(1)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(1)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addInternalTransactionTestIfEmpty() {
            assertThrows(IllegalArgumentException.class, () -> globalService.addInternalTransaction(new InternalTransactionDto()));
            verify(transactionService, Mockito.times(0)).addTransaction(any(InternalTransactionDto.class));
            verify(userService, Mockito.times(0)).findById(any(Integer.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addInternalTransactionTestIfNull() {
            assertThrows(IllegalArgumentException.class, () -> globalService.addInternalTransaction(null));
            verify(transactionService, Mockito.times(0)).addTransaction(any(InternalTransactionDto.class));
            verify(userService, Mockito.times(0)).findById(any(Integer.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addInternalTransactionTestIfNoSenderFound() {
            when(userService.findById(internalTransactionDto.getSenderId())).thenReturn(null);
            assertThrows(UserNotFoundException.class, () -> globalService.addInternalTransaction(internalTransactionDto));
            verify(transactionService, Mockito.times(0)).addTransaction(any(InternalTransactionDto.class));
            verify(userService, Mockito.times(1)).findById(any(Integer.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }
        @Test
        public void addInternalTransactionTestIfNoReceiverFound() {
            when(userService.findById(internalTransactionDto.getReceiverId())).thenReturn(null);
            assertThrows(UserNotFoundException.class, () -> globalService.addInternalTransaction(internalTransactionDto));
            verify(transactionService, Mockito.times(0)).addTransaction(any(InternalTransactionDto.class));
            verify(userService, Mockito.times(2)).findById(any(Integer.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addInternalTransactionTestIfNotEnoughOnAccount() {
            userDto.setAccountBalance(0.00);
            when(userService.findById(any(Integer.class))).thenReturn(userDto);
            assertThrows(IllegalArgumentException.class, () -> globalService.addInternalTransaction(internalTransactionDto));
            verify(transactionService, Mockito.times(0)).addTransaction(any(InternalTransactionDto.class));
            verify(userService, Mockito.times(2)).findById(any(Integer.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }
    }

    @Nested
    class addExternalTransactionTests {

        @Test
        public void addExternalTransactionTestIfToIban() {
            externalTransaction.setToIban(true);
            externalTransactionDto.setToIban(true);
            ExternalTransactionDto expectedResult = new ExternalTransactionDto(externalTransaction);
            ExternalTransactionDto actualResult = globalService.addExternalTransaction(externalTransactionDto);
            actualResult.setTimestamp(expectedResult.getTimestamp());
            assertEquals(expectedResult, actualResult);
            verify(transactionService, Mockito.times(1)).addTransaction(any(ExternalTransactionDto.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(1)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addExternalTransactionTestIfNotToIban() {
            ExternalTransactionDto expectedResult = new ExternalTransactionDto(externalTransaction);
            ExternalTransactionDto actualResult = globalService.addExternalTransaction(externalTransactionDto);
            actualResult.setTimestamp(expectedResult.getTimestamp());
            assertEquals(expectedResult, actualResult);
            verify(transactionService, Mockito.times(1)).addTransaction(any(ExternalTransactionDto.class));
            verify(userService, Mockito.times(1)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addExternalTransactionTestIfEmpty() {
            assertThrows(IllegalArgumentException.class, () -> globalService.addExternalTransaction(new ExternalTransactionDto()));
            verify(transactionService, Mockito.times(0)).addTransaction(any(ExternalTransactionDto.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addExternalTransactionTestIfNull() {
            assertThrows(IllegalArgumentException.class, () -> globalService.addExternalTransaction(null));
            verify(transactionService, Mockito.times(0)).addTransaction(any(ExternalTransactionDto.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }
        @Test
        public void addExternalTransactionTestIfNoSenderFound() {
            when(userService.findById(externalTransactionDto.getSenderId())).thenReturn(null);
            assertThrows(UserNotFoundException.class, () -> globalService.addExternalTransaction(externalTransactionDto));
            verify(transactionService, Mockito.times(0)).addTransaction(any(ExternalTransactionDto.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addExternalTransactionTestIfToIbanAndNotEnoughOnAccount() {
            externalTransactionDto.setToIban(true);
            userDto.setAccountBalance(0.00);
            when(userService.findById(any(Integer.class))).thenReturn(userDto);
            assertThrows(IllegalArgumentException.class, () -> globalService.addExternalTransaction(externalTransactionDto));
            verify(transactionRepository, Mockito.times(0)).save(any(Transaction.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }
    }

    @Nested
    class getPastTransactionsTests {

        @Test
        public void getPastTransactionsTest() {
            pastTransactionDtoList = new ArrayList<>(Arrays.asList(
                    new PastTransactionDto(internalTransactionDto.getId(),
                            userDto.getUsername(),
                            internalTransactionDto.getDescription(),
                            -internalTransactionDto.getAmount(),
                            currency.getName()),
                    new PastTransactionDto(externalTransactionDto.getId(),
                            userDto.getUsername(),
                            externalTransactionDto.getDescription(),
                            externalTransactionDto.getAmount(),
                            currency.getName()),
                    new PastTransactionDto(internalTransactionDto.getId(),
                            userDto.getUsername(),
                            internalTransactionDto.getDescription(),
                            internalTransactionDto.getAmount(),
                            currency.getName())
            ));
            pastTransactionDtoList.sort(Comparator.comparing(PastTransactionDto::getId).reversed());
            assertEquals(pastTransactionDtoList, globalService.getPastTransactions(userDto));
            verify(userService, Mockito.times(pastTransactionDtoList.size())).findById(any(Integer.class));
            verify(currencyService, Mockito.times(pastTransactionDtoList.size())).findById(any(Integer.class));
        }

        @Test
        public void getPastTransactionsTestIfSenderAndInternal() {
            pastTransactionDtoList = new ArrayList<>(List.of(
                    new PastTransactionDto(internalTransactionDto.getId(),
                            userDto.getUsername(),
                            internalTransactionDto.getDescription(),
                            -internalTransactionDto.getAmount(),
                            currency.getName())
            ));
            when(transactionService.findBySenderId(any(Integer.class))).thenReturn(new ArrayList<>(List.of(internalTransactionDto)));
            when(transactionService.findByReceiverId(any(Integer.class))).thenReturn(null);
            assertEquals(pastTransactionDtoList, globalService.getPastTransactions(userDto));
            verify(userService, Mockito.times(pastTransactionDtoList.size())).findById(any(Integer.class));
            verify(currencyService, Mockito.times(pastTransactionDtoList.size())).findById(any(Integer.class));
        }

        @Test
        public void getPastTransactionsTestIfReceiverAndInternal() {
            pastTransactionDtoList = new ArrayList<>(List.of(
                    new PastTransactionDto(internalTransactionDto.getId(),
                            userDto.getUsername(),
                            internalTransactionDto.getDescription(),
                            internalTransactionDto.getAmount(),
                            currency.getName())
            ));
            when(transactionService.findBySenderId(any(Integer.class))).thenReturn(null);
            when(transactionService.findByReceiverId(any(Integer.class))).thenReturn(new ArrayList<>(List.of(internalTransactionDto)));
            assertEquals(pastTransactionDtoList, globalService.getPastTransactions(userDto));
            verify(userService, Mockito.times(pastTransactionDtoList.size())).findById(any(Integer.class));
            verify(currencyService, Mockito.times(pastTransactionDtoList.size())).findById(any(Integer.class));
        }

        @Test
        public void getPastTransactionsTestIfExternalAndToIban() {
            externalTransactionDto.setToIban(true);
            pastTransactionDtoList = new ArrayList<>(List.of(
                    new PastTransactionDto(externalTransactionDto.getId(),
                            userDto.getUsername(),
                            externalTransactionDto.getDescription(),
                            -externalTransactionDto.getAmount(),
                            currency.getName())
            ));
            when(transactionService.findBySenderId(any(Integer.class))).thenReturn(new ArrayList<>(List.of(externalTransactionDto)));
            when(transactionService.findByReceiverId(any(Integer.class))).thenReturn(null);
            assertEquals(pastTransactionDtoList, globalService.getPastTransactions(userDto));
            verify(userService, Mockito.times(pastTransactionDtoList.size())).findById(any(Integer.class));
            verify(currencyService, Mockito.times(pastTransactionDtoList.size())).findById(any(Integer.class));
        }

        @Test
        public void getPastTransactionsTestIfExternalAndNotToIban() {
            externalTransactionDto.setToIban(false);
            pastTransactionDtoList = new ArrayList<>(List.of(
                    new PastTransactionDto(externalTransactionDto.getId(),
                            userDto.getUsername(),
                            externalTransactionDto.getDescription(),
                            externalTransactionDto.getAmount(),
                            currency.getName())
            ));
            when(transactionService.findBySenderId(any(Integer.class))).thenReturn(new ArrayList<>(List.of(externalTransactionDto)));
            when(transactionService.findByReceiverId(any(Integer.class))).thenReturn(null);
            assertEquals(pastTransactionDtoList, globalService.getPastTransactions(userDto));
            verify(userService, Mockito.times(pastTransactionDtoList.size())).findById(any(Integer.class));
            verify(currencyService, Mockito.times(pastTransactionDtoList.size())).findById(any(Integer.class));
        }

        @Test
        public void getPastTransactionsTestIfEmpty() {
            assertThrows(IllegalArgumentException.class, () -> globalService.getPastTransactions(new UserDto()));
            verify(userService, Mockito.times(0)).findById(any(Integer.class));
            verify(currencyService, Mockito.times(0)).findById(any(Integer.class));
        }

        @Test
        public void getPastTransactionsTestIfNull() {
            assertThrows(IllegalArgumentException.class, () -> globalService.getPastTransactions(null));
            verify(userService, Mockito.times(0)).findById(any(Integer.class));
            verify(currencyService, Mockito.times(0)).findById(any(Integer.class));
        }

        @Test
        public void getPastTransactionsTestIfNotInDB() {
            when(transactionService.findByReceiverId(any(Integer.class))).thenReturn(null);
            when(transactionService.findBySenderId(any(Integer.class))).thenReturn(null);
            assertEquals(new ArrayList<>(), globalService.getPastTransactions(userDto));
            verify(userService, Mockito.times(0)).findById(any(Integer.class));
            verify(currencyService, Mockito.times(0)).findById(any(Integer.class));
        }
    }
    @Nested
    class calculateCommissionAmountTests {

        @Test
        public void calculateCommissionAmountTest() {
            Double result = globalService.calculateCommissionAmount(externalTransactionDto);
            assertEquals(externalTransactionDto.getAmount() * commission.getRate(), result);
            verify(commissionService, Mockito.times(1)).findById(any(Integer.class));
        }

        @Test
        public void calculateCommissionAmountTestIfInternal() {
            Double result = globalService.calculateCommissionAmount(internalTransactionDto);
            assertEquals(internalTransactionDto.getAmount() * commission.getRate(), result);
            verify(commissionService, Mockito.times(1)).findById(any(Integer.class));
        }

        @Test
        public void calculateCommissionAmountTestIfEmpty() {
            assertThrows(IllegalArgumentException.class, () -> globalService.calculateCommissionAmount(new ExternalTransactionDto()));
            verify(commissionService, Mockito.times(0)).findById(any(Integer.class));
        }

        @Test
        public void calculateCommissionAmountTestIfNull() {
            assertThrows(IllegalArgumentException.class, () -> globalService.calculateCommissionAmount(null));
            verify(commissionService, Mockito.times(0)).findById(any(Integer.class));
        }

        @Test
        public void calculateCommissionAmountTestIfCommissionNotFound() {
            when(commissionService.findById(any(Integer.class))).thenReturn(null);
            assertThrows(IllegalArgumentException.class, () -> globalService.calculateCommissionAmount(externalTransactionDto));
            verify(commissionService, Mockito.times(1)).findById(any(Integer.class));
        }
    }
}
