package com.openclassrooms.paymybuddy.unit;

import com.openclassrooms.paymybuddy.controller.HomeController;
import com.openclassrooms.paymybuddy.dto.*;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = HomeController.class)
class HomeControllerTest {
	
	@Autowired
	HomeController homeController;

	Model model = new ConcurrentModel();

	Principal principal = new Principal() {
		@Override
		public String getName() {
			return userDto.getUsername();
		}
	};

	@MockBean
	private UserService userService;

	@MockBean
	private TransactionService transactionService;

	final UserDto userDto = new UserDto(1, 0.00,1,"email","iban","password",1,"username",new ArrayList<>());
	InternalTransactionDto internalTransactionDto = new InternalTransactionDto(1, 10.0, 0.05*10.0, userDto.getCurrencyId(), "description", userDto.getId(), 2, new Timestamp(0));
	ExternalTransactionDto externalTransactionDto = new ExternalTransactionDto(1, 10.0, 0.05*10.0, userDto.getCurrencyId(), "description", userDto.getIban(), userDto.getId(), new Timestamp(0), false);
	DatabaseTransactionDto databaseTransactionDto = new DatabaseTransactionDto(1, 10.0, 0.05*10.0, userDto.getCurrencyId(), "description", userDto.getIban(), userDto.getId(), 2, new Timestamp(0), false);

	final PastTransactionDto pastTransactionDto = new PastTransactionDto(internalTransactionDto.getId(), userDto.getUsername(), internalTransactionDto.getDescription(), internalTransactionDto.getAmount(), "currency name");
	final List<PastTransactionDto> pastTransactionDtoList = new ArrayList<>(Arrays.asList(new PastTransactionDto(pastTransactionDto.getId(), pastTransactionDto.getUsername(), pastTransactionDto.getDescription(), -pastTransactionDto.getAmount(), pastTransactionDto.getCurrency()), pastTransactionDto));

	@BeforeEach
	public void SetUp() {
		when(userService.findById(any(Integer.class))).thenReturn(userDto);
		when(userService.findByUsername(any(String.class))).thenReturn(userDto);
		when(userService.addUser(any(UserDto.class))).thenReturn(userDto);
		when(userService.addConnectionToUser(any(UserDto.class), any(UserDto.class))).thenReturn(userDto);
		when(transactionService.getPastTransactions(userDto)).thenReturn(pastTransactionDtoList);
		when(transactionService.addInternalTransaction(any(InternalTransactionDto.class))).thenReturn(internalTransactionDto);
		when(transactionService.addExternalTransaction(any(ExternalTransactionDto.class))).thenReturn(externalTransactionDto);
	}

	@Test
	void contextLoads() {
	}
	
	@Nested
	class showRegistrationFormTests {
		@Test
		public void showRegistrationFormTest() {
			assertEquals("signup_form",homeController.showRegistrationForm(model));
		}
		@Test
		public void showRegistrationFormTestIfNull() {
			assertThrows(NullPointerException.class, () -> homeController.showRegistrationForm(null));
		}
	}

	@Nested
	class processRegisterTests {
		@Test
		public void processRegisterTest() {
			assertEquals("register_success",homeController.processRegister(userDto));
			verify(userService, Mockito.times(1)).addUser(any(UserDto.class));
		}
		
		@Test
		public void processRegisterTestIfEmpty() {
			assertThrows(IllegalArgumentException.class, () -> homeController.processRegister(new UserDto()));
			verify(userService, Mockito.times(0)).addUser(any(UserDto.class));
		}
		@Test
		public void processRegisterTestIfNull() {
			assertThrows(IllegalArgumentException.class, () -> homeController.processRegister(null));
			verify(userService, Mockito.times(0)).addUser(any(UserDto.class));
		}

		@Test
		public void processRegisterTestIfErrorOnSave() {
			when(userService.addUser(any(UserDto.class))).thenThrow(new IllegalArgumentException());
			assertThrows(IllegalArgumentException.class, () -> homeController.processRegister(userDto));
			verify(userService, Mockito.times(1)).addUser(any(UserDto.class));
		}
	}

	@Nested
	class profileTests {
		@Test
		public void profileTest() {
			assertEquals("profile", homeController.profile(model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}

		@Test
		public void profileTestIfModelNull() {
			assertThrows(NullPointerException.class, () -> homeController.profile(null, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}

		@Test
		public void profileTestIfPrincipalNull() {
			assertThrows(NullPointerException.class, () -> homeController.profile(model, null));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
		}

		@Test
		public void profileTestIfNoUserFound() {
			when(userService.findByUsername(any(String.class))).thenReturn(null);
			assertEquals("error", homeController.profile(model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}
	}

	@Nested
	class contactTests {
		@Test
		public void contactTest() {
			assertEquals("contact", homeController.contact(model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}

		@Test
		public void contactTestIfModelNull() {
			assertThrows(NullPointerException.class, () -> homeController.contact(null, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}

		@Test
		public void contactTestIfPrincipalNull() {
			assertThrows(NullPointerException.class, () -> homeController.contact(model, null));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
		}

		@Test
		public void contactTestIfNoUserFound() {
			when(userService.findByUsername(any(String.class))).thenReturn(null);
			assertEquals("error", homeController.contact(model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}
	}

	@Nested
	class showAddConnectionFormTests {
		@Test
		public void showAddConnectionFormTest() {
			assertEquals("add_connection_form",homeController.showAddConnectionForm(model));
		}
		@Test
		public void showAddConnectionFormTestIfNull() {
			assertThrows(NullPointerException.class, () -> homeController.showAddConnectionForm(null));
		}
	}

	@Nested
	class processAddConnectionTests {
		@Test
		public void processAddConnectionTest() {
			assertEquals("transfer", homeController.processAddConnection(userDto, model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}

		@Test
		public void processAddConnectionTestIfConnectionNull() {
			assertThrows(IllegalArgumentException.class, () -> homeController.processAddConnection(null, model, principal));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
		}

		@Test
		public void processAddConnectionTestIfModelNull() {
			assertThrows(NullPointerException.class, () -> homeController.processAddConnection(userDto, null, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}

		@Test
		public void processAddConnectionTestIfPrincipalNull() {
			assertThrows(NullPointerException.class, () -> homeController.processAddConnection(userDto, model, null));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
		}

		@Test
		public void processAddConnectionTestIfNoUserFound() {
			when(userService.findByUsername(any(String.class))).thenReturn(null);
			assertEquals("error", homeController.processAddConnection(userDto, model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}

		@Test
		public void processAddConnectionTestIfErrorOnAddAction() {
			when(userService.addConnectionToUser(any(UserDto.class), any(UserDto.class))).thenThrow(new IllegalArgumentException());
			assertThrows(Exception.class, () -> homeController.processAddConnection(userDto, model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}
	}

	@Nested
	class showAddTransactionFormTests {
		@Test
		public void showAddTransactionFormTest() {
			assertEquals("transfer",homeController.showAddTransactionForm(model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(1)).getPastTransactions(any(UserDto.class));
		}
		@Test
		public void showAddTransactionFormTestIfModelNull() {
			assertThrows(NullPointerException.class, () -> homeController.showAddTransactionForm(null, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(0)).getPastTransactions(any(UserDto.class));
		}
		@Test
		public void showAddTransactionFormTestIfPrincipalNull() {
			assertThrows(NullPointerException.class, () -> homeController.showAddTransactionForm(model, null));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(0)).getPastTransactions(any(UserDto.class));
		}

		@Test
		public void showAddTransactionFormTestIfNoUserFound() {
			when(userService.findByUsername(any(String.class))).thenReturn(null);
			assertEquals("error", homeController.showAddTransactionForm(model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(0)).getPastTransactions(any(UserDto.class));
		}

		@Test
		public void showAddTransactionFormTestIfNoPastTransactionsFound() {
			when(transactionService.getPastTransactions(any(UserDto.class))).thenReturn(null);
			assertEquals("transfer", homeController.showAddTransactionForm(model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(1)).getPastTransactions(any(UserDto.class));
		}
	}

	@Nested
	class processTransferTests {
		@Test
		public void processTransferTest() {
			assertEquals("transfer", homeController.processTransfer(internalTransactionDto, model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(1)).addInternalTransaction(any(InternalTransactionDto.class));
			verify(transactionService, Mockito.times(1)).getPastTransactions(any(UserDto.class));
		}

		@Test
		public void processTransferTestIfTransactionNull() {
			assertThrows(IllegalArgumentException.class, () -> homeController.processTransfer(null, model, principal));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(0)).addInternalTransaction(any(InternalTransactionDto.class));
			verify(transactionService, Mockito.times(0)).getPastTransactions(any(UserDto.class));
		}

		@Test
		public void processTransferTestIfModelNull() {
			assertThrows(NullPointerException.class, () -> homeController.processTransfer(internalTransactionDto, null, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(1)).addInternalTransaction(any(InternalTransactionDto.class));
			verify(transactionService, Mockito.times(0)).getPastTransactions(any(UserDto.class));
		}

		@Test
		public void processTransferTestIfPrincipalNull() {
			assertThrows(NullPointerException.class, () -> homeController.processTransfer(internalTransactionDto, model, null));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(0)).addInternalTransaction(any(InternalTransactionDto.class));
			verify(transactionService, Mockito.times(0)).getPastTransactions(any(UserDto.class));
		}

		@Test
		public void processTransferTestIfNoUserFound() {
			when(userService.findByUsername(any(String.class))).thenReturn(null);
			assertEquals("error", homeController.processTransfer(internalTransactionDto, model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(0)).addInternalTransaction(any(InternalTransactionDto.class));
			verify(transactionService, Mockito.times(0)).getPastTransactions(any(UserDto.class));
		}

		@Test
		public void processTransferTestIfNoPastTransactionFound() {
			when(transactionService.getPastTransactions(any(UserDto.class))).thenReturn(null);
			assertEquals("transfer", homeController.processTransfer(internalTransactionDto, model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(1)).addInternalTransaction(any(InternalTransactionDto.class));
			verify(transactionService, Mockito.times(1)).getPastTransactions(any(UserDto.class));
		}

		@Test
		public void processTransferTestIfErrorOnAddTransaction() {
			when(transactionService.addInternalTransaction(any(InternalTransactionDto.class))).thenThrow(new IllegalArgumentException());
			assertThrows(Exception.class, () -> homeController.processTransfer(internalTransactionDto, model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(1)).addInternalTransaction(any(InternalTransactionDto.class));
			verify(transactionService, Mockito.times(0)).getPastTransactions(any(UserDto.class));
		}
	}

	@Nested
	class showAddTransactionFromBankAccountFormTests {
		@Test
		public void showAddTransactionFromBankAccountFormTest() {
			assertEquals("add_transaction_from_bank_account",homeController.showAddTransactionFromBankAccountForm(model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}
		@Test
		public void showAddTransactionFromBankAccountFormTestIfModelNull() {
			assertThrows(NullPointerException.class, () -> homeController.showAddTransactionFromBankAccountForm(null, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}
		@Test
		public void showAddTransactionFromBankAccountFormTestIfPrincipalNull() {
			assertThrows(NullPointerException.class, () -> homeController.showAddTransactionFromBankAccountForm(model, null));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
		}

		@Test
		public void showAddTransactionFromBankAccountFormTestIfNoUserFound() {
			when(userService.findByUsername(any(String.class))).thenReturn(null);
			assertEquals("error", homeController.showAddTransactionFromBankAccountForm(model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}

		@Test
		public void showAddTransactionFromBankAccountFormTestIfNoPastTransactionsFound() {
			when(transactionService.getPastTransactions(any(UserDto.class))).thenReturn(null);
			assertEquals("add_transaction_from_bank_account", homeController.showAddTransactionFromBankAccountForm(model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}
	}

	@Nested
	class processAddTransactionFromBankAccountFormTests {
		@Test
		public void processAddTransactionFromBankAccountFormTest() {
			assertEquals("profile", homeController.processAddTransactionFromBankAccountForm(externalTransactionDto, model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(1)).addExternalTransaction(any(ExternalTransactionDto.class));
			verify(transactionService, Mockito.times(1)).getPastTransactions(any(UserDto.class));
		}

		@Test
		public void processAddTransactionFromBankAccountFormTestIfTransactionNull() {
			assertThrows(IllegalArgumentException.class, () -> homeController.processAddTransactionFromBankAccountForm(null, model, principal));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(0)).addExternalTransaction(any(ExternalTransactionDto.class));
			verify(transactionService, Mockito.times(0)).getPastTransactions(any(UserDto.class));
		}

		@Test
		public void processAddTransactionFromBankAccountFormTestIfModelNull() {
			assertThrows(NullPointerException.class, () -> homeController.processAddTransactionFromBankAccountForm(externalTransactionDto, null, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(1)).addExternalTransaction(any(ExternalTransactionDto.class));
			verify(transactionService, Mockito.times(0)).getPastTransactions(any(UserDto.class));
		}

		@Test
		public void processAddTransactionFromBankAccountFormTestIfPrincipalNull() {
			assertThrows(NullPointerException.class, () -> homeController.processAddTransactionFromBankAccountForm(externalTransactionDto, model, null));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(0)).addExternalTransaction(any(ExternalTransactionDto.class));
			verify(transactionService, Mockito.times(0)).getPastTransactions(any(UserDto.class));
		}

		@Test
		public void processAddTransactionFromBankAccountFormTestIfNoUserFound() {
			when(userService.findByUsername(any(String.class))).thenReturn(null);
			assertEquals("error", homeController.processAddTransactionFromBankAccountForm(externalTransactionDto, model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(0)).addExternalTransaction(any(ExternalTransactionDto.class));
			verify(transactionService, Mockito.times(0)).getPastTransactions(any(UserDto.class));
		}

		@Test
		public void processAddTransactionFromBankAccountFormTestIfNoPastTransactionFound() {
			when(transactionService.getPastTransactions(any(UserDto.class))).thenReturn(null);
			assertEquals("profile", homeController.processAddTransactionFromBankAccountForm(externalTransactionDto, model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(1)).addExternalTransaction(any(ExternalTransactionDto.class));
			verify(transactionService, Mockito.times(1)).getPastTransactions(any(UserDto.class));
		}

		@Test
		public void processAddTransactionFromBankAccountFormTestIfErrorOnAddTransaction() {
			when(transactionService.addExternalTransaction(any(ExternalTransactionDto.class))).thenThrow(new IllegalArgumentException());
			assertThrows(Exception.class, () -> homeController.processAddTransactionFromBankAccountForm(externalTransactionDto, model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(1)).addExternalTransaction(any(ExternalTransactionDto.class));
			verify(transactionService, Mockito.times(0)).getPastTransactions(any(UserDto.class));
		}
	}

	@Nested
	class showAddTransactionToBankAccountFormTests {
		@Test
		public void showAddTransactionToBankAccountFormTest() {
			assertEquals("add_transaction_to_bank_account",homeController.showAddTransactionToBankAccountForm(model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}
		@Test
		public void showAddTransactionToBankAccountFormTestIfModelNull() {
			assertThrows(NullPointerException.class, () -> homeController.showAddTransactionToBankAccountForm(null, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}
		@Test
		public void showAddTransactionToBankAccountFormTestIfPrincipalNull() {
			assertThrows(NullPointerException.class, () -> homeController.showAddTransactionToBankAccountForm(model, null));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
		}

		@Test
		public void showAddTransactionToBankAccountFormTestIfNoUserFound() {
			when(userService.findByUsername(any(String.class))).thenReturn(null);
			assertEquals("error", homeController.showAddTransactionToBankAccountForm(model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}

		@Test
		public void showAddTransactionToBankAccountFormTestIfNoPastTransactionsFound() {
			when(transactionService.getPastTransactions(any(UserDto.class))).thenReturn(null);
			assertEquals("add_transaction_to_bank_account", homeController.showAddTransactionToBankAccountForm(model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}
	}

	@Nested
	class processAddTransactionToBankAccountFormTests {
		@Test
		public void processAddTransactionToBankAccountFormTest() {
			assertEquals("profile", homeController.processAddTransactionToBankAccountForm(externalTransactionDto, model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(1)).addExternalTransaction(any(ExternalTransactionDto.class));
			verify(transactionService, Mockito.times(1)).getPastTransactions(any(UserDto.class));
		}

		@Test
		public void processAddTransactionToBankAccountFormTestIfTransactionNull() {
			assertThrows(IllegalArgumentException.class, () -> homeController.processAddTransactionToBankAccountForm(null, model, principal));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(0)).addExternalTransaction(any(ExternalTransactionDto.class));
			verify(transactionService, Mockito.times(0)).getPastTransactions(any(UserDto.class));
		}

		@Test
		public void processAddTransactionToBankAccountFormTestIfModelNull() {
			assertThrows(NullPointerException.class, () -> homeController.processAddTransactionToBankAccountForm(externalTransactionDto, null, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(1)).addExternalTransaction(any(ExternalTransactionDto.class));
			verify(transactionService, Mockito.times(0)).getPastTransactions(any(UserDto.class));
		}

		@Test
		public void processAddTransactionToBankAccountFormTestIfPrincipalNull() {
			assertThrows(NullPointerException.class, () -> homeController.processAddTransactionToBankAccountForm(externalTransactionDto, model, null));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(0)).addExternalTransaction(any(ExternalTransactionDto.class));
			verify(transactionService, Mockito.times(0)).getPastTransactions(any(UserDto.class));
		}

		@Test
		public void processAddTransactionToBankAccountFormTestIfNoUserFound() {
			when(userService.findByUsername(any(String.class))).thenReturn(null);
			assertEquals("error", homeController.processAddTransactionToBankAccountForm(externalTransactionDto, model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(0)).addExternalTransaction(any(ExternalTransactionDto.class));
			verify(transactionService, Mockito.times(0)).getPastTransactions(any(UserDto.class));
		}

		@Test
		public void processAddTransactionToBankAccountFormTestIfNoPastTransactionFound() {
			when(transactionService.getPastTransactions(any(UserDto.class))).thenReturn(null);
			assertEquals("profile", homeController.processAddTransactionToBankAccountForm(externalTransactionDto, model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(1)).addExternalTransaction(any(ExternalTransactionDto.class));
			verify(transactionService, Mockito.times(1)).getPastTransactions(any(UserDto.class));
		}

		@Test
		public void processAddTransactionToBankAccountFormTestIfErrorOnAddTransaction() {
			when(transactionService.addExternalTransaction(any(ExternalTransactionDto.class))).thenThrow(new IllegalArgumentException());
			assertThrows(Exception.class, () -> homeController.processAddTransactionToBankAccountForm(externalTransactionDto, model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(1)).addExternalTransaction(any(ExternalTransactionDto.class));
			verify(transactionService, Mockito.times(0)).getPastTransactions(any(UserDto.class));
		}
	}

}
