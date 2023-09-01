package com.openclassrooms.paymybuddy.unit;

import com.openclassrooms.paymybuddy.dto.*;
import com.openclassrooms.paymybuddy.entity.Currency;
import com.openclassrooms.paymybuddy.entity.Transaction;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
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
import java.util.Arrays;
import java.util.List;

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
    @MockBean
    CurrencyService currencyService;

    final Currency currency = new Currency(1, "currency");
    final UserDto userDto = new UserDto(1, 100.00, currency.getId(), "email","iban","password",1,"username",new ArrayList<>());

    final Transaction transaction = new Transaction(1, 10.0, 0.05*10.0, currency.getId(), "description", "iban", 1, 2, new Timestamp(0), false);

    private InternalTransactionDto internalTransactionDto = new InternalTransactionDto(transaction);
    private ExternalTransactionDto externalTransactionDto = new ExternalTransactionDto(transaction);
    private DatabaseTransactionDto databaseTransactionDto = new DatabaseTransactionDto(transaction);

    final List<Transaction> transactionList = new ArrayList<>(Arrays.asList(transaction));
    final List<DatabaseTransactionDto> databaseTransactionDtoList = new ArrayList<>(Arrays.asList(databaseTransactionDto));
    final PastTransactionDto pastTransactionDto = new PastTransactionDto(internalTransactionDto.getId(), userDto.getUsername(), internalTransactionDto.getDescription(), internalTransactionDto.getAmount(), currency.getName());
    final List<PastTransactionDto> pastTransactionDtoList = new ArrayList<>(Arrays.asList(new PastTransactionDto(pastTransactionDto.getId(), pastTransactionDto.getUsername(), pastTransactionDto.getDescription(), -pastTransactionDto.getAmount(), pastTransactionDto.getCurrency()), pastTransactionDto));

    InternalTransactionDto internalTransactionDtoTest = new InternalTransactionDto(internalTransactionDto);
    ExternalTransactionDto externalTransactionDtoTest = new ExternalTransactionDto(externalTransactionDto);
    DatabaseTransactionDto databaseTransactionDtoTest = new DatabaseTransactionDto(databaseTransactionDto);
    @BeforeEach
    private void setUp() {
        internalTransactionDtoTest = new InternalTransactionDto(internalTransactionDto);
        externalTransactionDtoTest = new ExternalTransactionDto(externalTransactionDto);
        databaseTransactionDtoTest = new DatabaseTransactionDto(databaseTransactionDto);

        when(transactionRepository.findById(any(Integer.class))).thenReturn(transaction);
        when(transactionRepository.findBySenderId(any(Integer.class))).thenReturn(transactionList);
        when(transactionRepository.findByReceiverId(any(Integer.class))).thenReturn(transactionList);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        when(userService.findById(any(Integer.class))).thenReturn(userDto);

        when(currencyService.findById(any(Integer.class))).thenReturn(currency);
    }

    @Test
    void contextLoads() {}

    @Nested
    class addInternalTransactionTests {

        @Test
        public void addInternalTransactionTest() {
            assertEquals(internalTransactionDtoTest, transactionService.addInternalTransaction(internalTransactionDto));
            verify(transactionRepository, Mockito.times(1)).save(any(Transaction.class));
            verify(userService, Mockito.times(1)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(1)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addInternalTransactionTestIfEmpty() {
            assertThrows(IllegalArgumentException.class, () -> transactionService.addInternalTransaction(new InternalTransactionDto()));
            verify(transactionRepository, Mockito.times(0)).save(any(Transaction.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addInternalTransactionTestIfNull() {
            assertThrows(IllegalArgumentException.class, () -> transactionService.addInternalTransaction(null));
            verify(transactionRepository, Mockito.times(0)).save(any(Transaction.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addInternalTransactionTestIfErrorOnSave() {
            when(transactionRepository.save(any(Transaction.class))).thenThrow(new IllegalArgumentException());
            assertThrows(IllegalArgumentException.class, () -> transactionService.addInternalTransaction(internalTransactionDto));
            verify(transactionRepository, Mockito.times(1)).save(any(Transaction.class));
            verify(userService, Mockito.times(1)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(1)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addInternalTransactionTestIfNoSenderFound() {
            when(userService.findById(internalTransactionDto.getSenderId())).thenReturn(null);
            assertEquals(null, transactionService.addInternalTransaction(internalTransactionDto));
            verify(transactionRepository, Mockito.times(0)).save(any(Transaction.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }
        @Test
        public void addInternalTransactionTestIfNoReceiverFound() {
            when(userService.findById(internalTransactionDto.getReceiverId())).thenReturn(null);
            assertEquals(null, transactionService.addInternalTransaction(internalTransactionDto));
            verify(transactionRepository, Mockito.times(0)).save(any(Transaction.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addInternalTransactionTestIfNotEnoughOnAccount() {
            UserDto userTest = userDto;
            userTest.setAccountBalance(0.00);
            when(userService.findById(any(Integer.class))).thenReturn(userTest);
            assertEquals(null, transactionService.addInternalTransaction(internalTransactionDto));
            verify(transactionRepository, Mockito.times(0)).save(any(Transaction.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }
    }

    @Nested
    class addExternalTransactionTests {
        @Test
        public void addExternalTransactionTestIfToIban() {
            externalTransactionDtoTest.setToIban(true);
            assertEquals(externalTransactionDto, transactionService.addExternalTransaction(externalTransactionDtoTest));
            verify(transactionRepository, Mockito.times(1)).save(any(Transaction.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(1)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addExternalTransactionTestIfNotToIban() {
            assertEquals(externalTransactionDtoTest, transactionService.addExternalTransaction(externalTransactionDto));
            verify(transactionRepository, Mockito.times(1)).save(any(Transaction.class));
            verify(userService, Mockito.times(1)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addExternalTransactionTestIfEmpty() {
            assertThrows(IllegalArgumentException.class, () -> transactionService.addExternalTransaction(new ExternalTransactionDto()));
            verify(transactionRepository, Mockito.times(0)).save(any(Transaction.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addExternalTransactionTestIfNull() {
            assertThrows(IllegalArgumentException.class, () -> transactionService.addExternalTransaction(null));
            verify(transactionRepository, Mockito.times(0)).save(any(Transaction.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addExternalTransactionTestIfErrorOnSave() {
            when(transactionRepository.save(any(Transaction.class))).thenThrow(new IllegalArgumentException());
            assertThrows(IllegalArgumentException.class, () -> transactionService.addExternalTransaction(externalTransactionDto));
            verify(transactionRepository, Mockito.times(1)).save(any(Transaction.class));
            verify(userService, Mockito.times(1)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }
        @Test
        public void addExternalTransactionTestIfNoSenderFound() {
            when(userService.findById(internalTransactionDto.getSenderId())).thenReturn(null);
            assertEquals(null, transactionService.addExternalTransaction(externalTransactionDto));
            verify(transactionRepository, Mockito.times(0)).save(any(Transaction.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }

        @Test
        public void addExternalTransactionTestIfToIbanAndNotEnoughOnAccount() {
            externalTransactionDtoTest.setToIban(true);
            UserDto userTest = userDto;
            userTest.setAccountBalance(0.00);
            when(userService.findById(any(Integer.class))).thenReturn(userTest);
            assertEquals(null, transactionService.addExternalTransaction(externalTransactionDtoTest));
            verify(transactionRepository, Mockito.times(0)).save(any(Transaction.class));
            verify(userService, Mockito.times(0)).addToAccountBalance(any(UserDto.class), any(Double.class));
            verify(userService, Mockito.times(0)).removeFromAccountBalance(any(UserDto.class), any(Double.class));
        }
    }

    @Nested
    class findByIdTests {

        @Test
        public void findByIdTest() {
            assertEquals(databaseTransactionDtoTest, transactionService.findById(transaction.getId()));
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
            assertEquals(databaseTransactionDtoList, transactionService.findBySenderId(transaction.getId()));
            verify(transactionRepository, Mockito.times(1)).findBySenderId(any(Integer.class));
        }

        @Test
        public void findBySenderTestIfNotInDB() {
            when(transactionRepository.findBySenderId(any(Integer.class))).thenReturn(null);
            assertEquals(new ArrayList<>(), transactionService.findBySenderId(transaction.getId()));
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
            assertEquals(databaseTransactionDtoList, transactionService.findByReceiverId(transaction.getId()));
            verify(transactionRepository, Mockito.times(1)).findByReceiverId(any(Integer.class));
        }

        @Test
        public void findByReceiverTestIfNotInDB() {
            when(transactionRepository.findByReceiverId(any(Integer.class))).thenReturn(null);
            assertEquals(new ArrayList<>(), transactionService.findByReceiverId(transaction.getId()));
            verify(transactionRepository, Mockito.times(1)).findByReceiverId(any(Integer.class));
        }

        @Test
        public void findByReceiverTestIfNull() {
            assertThrows(IllegalArgumentException.class, () -> transactionService.findByReceiverId(null));
            verify(transactionRepository, Mockito.times(0)).findByReceiverId(any(Integer.class));
        }
    }

    @Nested
    class getPastTransactionsTests {

        @Test
        public void getPastTransactionsTest() {
            assertEquals(pastTransactionDtoList, transactionService.getPastTransactions(userDto));
            verify(userService, Mockito.times(2)).findById(any(Integer.class));
            verify(currencyService, Mockito.times(2)).findById(any(Integer.class));
        }

        @Test
        public void getPastTransactionsTestIfEmpty() {
            assertThrows(IllegalArgumentException.class, () -> transactionService.getPastTransactions(new UserDto()));
            verify(userService, Mockito.times(0)).findById(any(Integer.class));
            verify(currencyService, Mockito.times(0)).findById(any(Integer.class));
        }

        @Test
        public void getPastTransactionsTestIfNull() {
            assertThrows(IllegalArgumentException.class, () -> transactionService.getPastTransactions(null));
            verify(userService, Mockito.times(0)).findById(any(Integer.class));
            verify(currencyService, Mockito.times(0)).findById(any(Integer.class));
        }

        @Test
        public void getPastTransactionsTestIfNotInDB() {
            when(transactionRepository.findByReceiverId(any(Integer.class))).thenReturn(null);
            when(transactionRepository.findBySenderId(any(Integer.class))).thenReturn(null);
            assertEquals(new ArrayList<>(), transactionService.getPastTransactions(userDto));
            verify(userService, Mockito.times(0)).findById(any(Integer.class));
            verify(currencyService, Mockito.times(0)).findById(any(Integer.class));
        }
    }
}
