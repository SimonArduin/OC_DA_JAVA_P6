package com.openclassrooms.paymybuddy.unit;

import com.openclassrooms.paymybuddy.dto.ExternalTransactionDto;
import com.openclassrooms.paymybuddy.dto.InternalTransactionDto;
import com.openclassrooms.paymybuddy.dto.PastTransactionDto;
import com.openclassrooms.paymybuddy.dto.UserDto;
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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
    private void setUp() {
        initializeVariables();
        internalTransaction = new Transaction(user.getId(), 10.0, commission.getRate()*10.0, commission.getId(), currency.getId(), "description", null, userDto.getId(), 2, new Timestamp(0), null);
        transactionOther = new Transaction(userOther.getId(), 20.0, commission.getRate()*20.0, commission.getId(), 2, "descriptionOther", null, userDtoOther.getId(), 4, new Timestamp(0), null);

        when(transactionRepository.findById(any(Integer.class))).thenReturn(new Transaction(internalTransactionDto));
        when(transactionRepository.findBySenderId(any(Integer.class))).thenReturn(transactionList);
        when(transactionRepository.findByReceiverId(any(Integer.class))).thenReturn(transactionList);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(internalTransaction);

        when(transactionService.findBySenderId(any(Integer.class))).thenReturn(transactionDtoList);
        when(transactionService.findByReceiverId(any(Integer.class))).thenReturn(transactionDtoList);
        
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
            assertEquals(new InternalTransactionDto(internalTransaction), globalService.addInternalTransaction(internalTransactionDto));
            verify(transactionRepository, Mockito.times(1)).save(any(Transaction.class));
            verify(userService, Mockito.times(2)).findById(any(Integer.class));
            verify(userService, Mockito.times(1)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(1)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addInternalTransactionTestIfEmpty() {
            assertThrows(IllegalArgumentException.class, () -> globalService.addInternalTransaction(new InternalTransactionDto()));
            verify(transactionRepository, Mockito.times(0)).save(any(Transaction.class));
            verify(userService, Mockito.times(0)).findById(any(Integer.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addInternalTransactionTestIfNull() {
            assertThrows(IllegalArgumentException.class, () -> globalService.addInternalTransaction(null));
            verify(transactionRepository, Mockito.times(0)).save(any(Transaction.class));
            verify(userService, Mockito.times(0)).findById(any(Integer.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addInternalTransactionTestIfErrorOnSave() {
            when(transactionRepository.save(any(Transaction.class))).thenThrow(new IllegalArgumentException());
            assertThrows(IllegalArgumentException.class, () -> globalService.addInternalTransaction(internalTransactionDto));
            verify(transactionRepository, Mockito.times(1)).save(any(Transaction.class));
            verify(userService, Mockito.times(2)).findById(any(Integer.class));
            verify(userService, Mockito.times(1)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(1)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addInternalTransactionTestIfNoSenderFound() {
            when(userService.findById(internalTransactionDto.getSenderId())).thenReturn(null);
            assertThrows(UserNotFoundException.class, () -> globalService.addInternalTransaction(internalTransactionDto));
            verify(transactionRepository, Mockito.times(0)).save(any(Transaction.class));
            verify(userService, Mockito.times(1)).findById(any(Integer.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }
        @Test
        public void addInternalTransactionTestIfNoReceiverFound() {
            when(userService.findById(internalTransactionDto.getReceiverId())).thenReturn(null);
            assertThrows(UserNotFoundException.class, () -> globalService.addInternalTransaction(internalTransactionDto));
            verify(transactionRepository, Mockito.times(0)).save(any(Transaction.class));
            verify(userService, Mockito.times(2)).findById(any(Integer.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addInternalTransactionTestIfNotEnoughOnAccount() {
            userDto.setAccountBalance(0.00);
            when(userService.findById(any(Integer.class))).thenReturn(userDto);
            assertThrows(IllegalArgumentException.class, () -> globalService.addInternalTransaction(internalTransactionDto));
            verify(transactionRepository, Mockito.times(0)).save(any(Transaction.class));
            verify(userService, Mockito.times(2)).findById(any(Integer.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }
    }

    @Nested
    class addExternalTransactionTests {
        @BeforeEach
        private void setUp() {
            when(transactionRepository.save(any(Transaction.class))).thenReturn(externalTransaction);
        }

        @Test
        public void addExternalTransactionTestIfToIban() {
            externalTransaction.setToIban(true);
            externalTransactionDto.setToIban(true);
            when(transactionRepository.save(any(Transaction.class))).thenReturn(externalTransaction);
            assertEquals(new ExternalTransactionDto(externalTransaction), globalService.addExternalTransaction(externalTransactionDto));
            verify(transactionRepository, Mockito.times(1)).save(any(Transaction.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(1)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addExternalTransactionTestIfNotToIban() {
            assertEquals(new ExternalTransactionDto(externalTransaction), globalService.addExternalTransaction(externalTransactionDto));
            verify(transactionRepository, Mockito.times(1)).save(any(Transaction.class));
            verify(userService, Mockito.times(1)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addExternalTransactionTestIfEmpty() {
            assertThrows(IllegalArgumentException.class, () -> globalService.addExternalTransaction(new ExternalTransactionDto()));
            verify(transactionRepository, Mockito.times(0)).save(any(Transaction.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addExternalTransactionTestIfNull() {
            assertThrows(IllegalArgumentException.class, () -> globalService.addExternalTransaction(null));
            verify(transactionRepository, Mockito.times(0)).save(any(Transaction.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addExternalTransactionTestIfErrorOnSave() {
            when(transactionRepository.save(any(Transaction.class))).thenThrow(new IllegalArgumentException());
            assertThrows(IllegalArgumentException.class, () -> globalService.addExternalTransaction(externalTransactionDto));
            verify(transactionRepository, Mockito.times(1)).save(any(Transaction.class));
            verify(userService, Mockito.times(1)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }
        @Test
        public void addExternalTransactionTestIfNoSenderFound() {
            when(userService.findById(externalTransactionDto.getSenderId())).thenReturn(null);
            assertThrows(UserNotFoundException.class, () -> globalService.addExternalTransaction(externalTransactionDto));
            verify(transactionRepository, Mockito.times(0)).save(any(Transaction.class));
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
            pastTransactionDtoList = new ArrayList<>(Arrays.asList(
                    new PastTransactionDto(internalTransactionDto.getId(),
                            userDto.getUsername(),
                            internalTransactionDto.getDescription(),
                            -internalTransactionDto.getAmount(),
                            currency.getName())
            ));
            when(transactionService.findBySenderId(any(Integer.class))).thenReturn(new ArrayList<>(Arrays.asList(internalTransactionDto)));
            when(transactionService.findByReceiverId(any(Integer.class))).thenReturn(null);
            assertEquals(pastTransactionDtoList, globalService.getPastTransactions(userDto));
            verify(userService, Mockito.times(pastTransactionDtoList.size())).findById(any(Integer.class));
            verify(currencyService, Mockito.times(pastTransactionDtoList.size())).findById(any(Integer.class));
        }

        @Test
        public void getPastTransactionsTestIfReceiverAndInternal() {
            pastTransactionDtoList = new ArrayList<>(Arrays.asList(
                    new PastTransactionDto(internalTransactionDto.getId(),
                            userDto.getUsername(),
                            internalTransactionDto.getDescription(),
                            internalTransactionDto.getAmount(),
                            currency.getName())
            ));
            when(transactionService.findBySenderId(any(Integer.class))).thenReturn(null);
            when(transactionService.findByReceiverId(any(Integer.class))).thenReturn(new ArrayList<>(Arrays.asList(internalTransactionDto)));
            assertEquals(pastTransactionDtoList, globalService.getPastTransactions(userDto));
            verify(userService, Mockito.times(pastTransactionDtoList.size())).findById(any(Integer.class));
            verify(currencyService, Mockito.times(pastTransactionDtoList.size())).findById(any(Integer.class));
        }

        @Test
        public void getPastTransactionsTestIfExternalAndToIban() {
            externalTransactionDto.setToIban(true);
            pastTransactionDtoList = new ArrayList<>(Arrays.asList(
                    new PastTransactionDto(externalTransactionDto.getId(),
                            userDto.getUsername(),
                            externalTransactionDto.getDescription(),
                            -externalTransactionDto.getAmount(),
                            currency.getName())
            ));
            when(transactionService.findBySenderId(any(Integer.class))).thenReturn(new ArrayList<>(Arrays.asList(externalTransactionDto)));
            when(transactionService.findByReceiverId(any(Integer.class))).thenReturn(null);
            assertEquals(pastTransactionDtoList, globalService.getPastTransactions(userDto));
            verify(userService, Mockito.times(pastTransactionDtoList.size())).findById(any(Integer.class));
            verify(currencyService, Mockito.times(pastTransactionDtoList.size())).findById(any(Integer.class));
        }

        @Test
        public void getPastTransactionsTestIfExternalAndNotToIban() {
            externalTransactionDto.setToIban(false);
            pastTransactionDtoList = new ArrayList<>(Arrays.asList(
                    new PastTransactionDto(externalTransactionDto.getId(),
                            userDto.getUsername(),
                            externalTransactionDto.getDescription(),
                            externalTransactionDto.getAmount(),
                            currency.getName())
            ));
            when(transactionService.findBySenderId(any(Integer.class))).thenReturn(new ArrayList<>(Arrays.asList(externalTransactionDto)));
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
