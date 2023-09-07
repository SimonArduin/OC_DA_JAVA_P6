package com.openclassrooms.paymybuddy.unit;

import com.openclassrooms.paymybuddy.controller.ApplicationController;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ApplicationController.class)
class ApplicationControllerTest extends TestVariables {
	
	@Autowired
	ApplicationController applicationController;

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

	@BeforeEach
	public void SetUp() {
		initializeVariables();

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
			assertEquals("signup_form", applicationController.showRegistrationForm(model));
		}
		@Test
		public void showRegistrationFormTestIfNull() {
			assertThrows(NullPointerException.class, () -> applicationController.showRegistrationForm(null));
		}
	}

	@Nested
	class processRegisterTests {
		@Test
		public void processRegisterTest() {
			assertEquals("register_success", applicationController.processRegister(userDto));
			verify(userService, Mockito.times(1)).addUser(any(UserDto.class));
		}
		
		@Test
		public void processRegisterTestIfEmpty() {
			assertThrows(IllegalArgumentException.class, () -> applicationController.processRegister(new UserDto()));
			verify(userService, Mockito.times(0)).addUser(any(UserDto.class));
		}
		@Test
		public void processRegisterTestIfNull() {
			assertThrows(IllegalArgumentException.class, () -> applicationController.processRegister(null));
			verify(userService, Mockito.times(0)).addUser(any(UserDto.class));
		}

		@Test
		public void processRegisterTestIfErrorOnSave() {
			when(userService.addUser(any(UserDto.class))).thenThrow(new IllegalArgumentException());
			assertThrows(IllegalArgumentException.class, () -> applicationController.processRegister(userDto));
			verify(userService, Mockito.times(1)).addUser(any(UserDto.class));
		}
	}

	@Nested
	class profileTests {
		@Test
		public void profileTest() {
			assertEquals("profile", applicationController.profile(model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}

		@Test
		public void profileTestIfModelNull() {
			assertThrows(NullPointerException.class, () -> applicationController.profile(null, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}

		@Test
		public void profileTestIfPrincipalNull() {
			assertThrows(NullPointerException.class, () -> applicationController.profile(model, null));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
		}

		@Test
		public void profileTestIfNoUserFound() {
			when(userService.findByUsername(any(String.class))).thenReturn(null);
			assertEquals("error", applicationController.profile(model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}
	}

	@Nested
	class contactTests {
		@Test
		public void contactTest() {
			assertEquals("contact", applicationController.contact(model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}

		@Test
		public void contactTestIfModelNull() {
			assertThrows(NullPointerException.class, () -> applicationController.contact(null, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}

		@Test
		public void contactTestIfPrincipalNull() {
			assertThrows(NullPointerException.class, () -> applicationController.contact(model, null));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
		}

		@Test
		public void contactTestIfNoUserFound() {
			when(userService.findByUsername(any(String.class))).thenReturn(null);
			assertEquals("error", applicationController.contact(model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}
	}

	@Nested
	class showAddConnectionFormTests {
		@Test
		public void showAddConnectionFormTest() {
			assertEquals("add_connection_form", applicationController.showAddConnectionForm(model));
		}
		@Test
		public void showAddConnectionFormTestIfNull() {
			assertThrows(NullPointerException.class, () -> applicationController.showAddConnectionForm(null));
		}
	}

	@Nested
	class processAddConnectionTests {
		@Test
		public void processAddConnectionTest() {
			assertEquals("contact", applicationController.processAddConnection(userDto, model, principal));
			verify(userService, Mockito.times(2+1)).findByUsername(any(String.class));
			verify(userService, Mockito.times(1)).addConnectionToUser(any(UserDto.class), any(UserDto.class));
		}

		@Test
		public void processAddConnectionTestIfConnectionNull() {
			assertThrows(IllegalArgumentException.class, () -> applicationController.processAddConnection(null, model, principal));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
			verify(userService, Mockito.times(0)).addConnectionToUser(any(UserDto.class), any(UserDto.class));
		}

		@Test
		public void processAddConnectionTestIfConnectionEmpty() {
			assertThrows(IllegalArgumentException.class, () -> applicationController.processAddConnection(new UserDto(), model, principal));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
			verify(userService, Mockito.times(0)).addConnectionToUser(any(UserDto.class), any(UserDto.class));
		}

		@Test
		public void processAddConnectionTestIfModelNull() {
			assertEquals("error", applicationController.processAddConnection(userDto, null, principal));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
			verify(userService, Mockito.times(0)).addConnectionToUser(any(UserDto.class), any(UserDto.class));
		}

		@Test
		public void processAddConnectionTestIfPrincipalNull() {
			assertEquals("error", applicationController.processAddConnection(userDto, model, null));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
			verify(userService, Mockito.times(0)).addConnectionToUser(any(UserDto.class), any(UserDto.class));
		}

		@Test
		public void processAddConnectionTestIfNoUserFound() {
			when(userService.findByUsername(any(String.class))).thenReturn(null);
			assertEquals("error", applicationController.processAddConnection(userDto, model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(userService, Mockito.times(0)).addConnectionToUser(any(UserDto.class), any(UserDto.class));
		}

		@Test
		public void processAddConnectionTestIfErrorOnAddAction() {
			when(userService.addConnectionToUser(any(UserDto.class), any(UserDto.class))).thenThrow(new IllegalArgumentException());
			assertThrows(Exception.class, () -> applicationController.processAddConnection(userDto, model, principal));
			verify(userService, Mockito.times(2)).findByUsername(any(String.class));
			verify(userService, Mockito.times(1)).addConnectionToUser(any(UserDto.class), any(UserDto.class));
		}
	}

	@Nested
	class showAddTransactionFormTests {
		@Test
		public void showAddTransactionFormTest() {
			assertEquals("transfer", applicationController.showAddTransactionForm(model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(1)).getPastTransactions(any(UserDto.class));
		}
		@Test
		public void showAddTransactionFormTestIfModelNull() {
			assertThrows(NullPointerException.class, () -> applicationController.showAddTransactionForm(null, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(0)).getPastTransactions(any(UserDto.class));
		}
		@Test
		public void showAddTransactionFormTestIfPrincipalNull() {
			assertThrows(NullPointerException.class, () -> applicationController.showAddTransactionForm(model, null));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(0)).getPastTransactions(any(UserDto.class));
		}

		@Test
		public void showAddTransactionFormTestIfNoUserFound() {
			when(userService.findByUsername(any(String.class))).thenReturn(null);
			assertEquals("error", applicationController.showAddTransactionForm(model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(0)).getPastTransactions(any(UserDto.class));
		}

		@Test
		public void showAddTransactionFormTestIfNoPastTransactionsFound() {
			when(transactionService.getPastTransactions(any(UserDto.class))).thenReturn(null);
			assertEquals("transfer", applicationController.showAddTransactionForm(model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(1)).getPastTransactions(any(UserDto.class));
		}
	}

	@Nested
	class processTransferTests {
		@Test
		public void processTransferTest() {
			assertEquals("transfer", applicationController.processTransfer(internalTransactionDto, model, principal));
			verify(userService, Mockito.times(1+1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(1)).addInternalTransaction(any(InternalTransactionDto.class));
			verify(transactionService, Mockito.times(1+1)).getPastTransactions(any(UserDto.class));
		}

		@Test
		public void processTransferTestIfTransactionEmpty() {
			assertThrows(IllegalArgumentException.class, () -> applicationController.processTransfer(new InternalTransactionDto(), model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(0)).addInternalTransaction(any(InternalTransactionDto.class));
			verify(transactionService, Mockito.times(0)).getPastTransactions(any(UserDto.class));
		}

		@Test
		public void processTransferTestIfTransactionNull() {
			assertThrows(IllegalArgumentException.class, () -> applicationController.processTransfer(null, model, principal));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(0)).addInternalTransaction(any(InternalTransactionDto.class));
			verify(transactionService, Mockito.times(0)).getPastTransactions(any(UserDto.class));
		}

		@Test
		public void processTransferTestIfModelNull() {
			assertEquals("error", applicationController.processTransfer(internalTransactionDto, null, principal));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(0)).addInternalTransaction(any(InternalTransactionDto.class));
			verify(transactionService, Mockito.times(0)).getPastTransactions(any(UserDto.class));
		}

		@Test
		public void processTransferTestIfPrincipalNull() {
			assertEquals("error", applicationController.processTransfer(internalTransactionDto, model, null));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(0)).addInternalTransaction(any(InternalTransactionDto.class));
			verify(transactionService, Mockito.times(0)).getPastTransactions(any(UserDto.class));
		}

		@Test
		public void processTransferTestIfNoUserFound() {
			when(userService.findByUsername(any(String.class))).thenReturn(null);
			assertEquals("error", applicationController.processTransfer(internalTransactionDto, model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(0)).addInternalTransaction(any(InternalTransactionDto.class));
			verify(transactionService, Mockito.times(0)).getPastTransactions(any(UserDto.class));
		}

		@Test
		public void processTransferTestIfNoPastTransactionFound() {
			when(transactionService.getPastTransactions(any(UserDto.class))).thenReturn(null);
			assertEquals("transfer", applicationController.processTransfer(internalTransactionDto, model, principal));
			verify(userService, Mockito.times(1+1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(1)).addInternalTransaction(any(InternalTransactionDto.class));
			verify(transactionService, Mockito.times(1+1)).getPastTransactions(any(UserDto.class));
		}

		@Test
		public void processTransferTestIfErrorOnAddTransaction() {
			when(transactionService.addInternalTransaction(any(InternalTransactionDto.class))).thenThrow(new IllegalArgumentException());
			assertThrows(Exception.class, () -> applicationController.processTransfer(internalTransactionDto, model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(1)).addInternalTransaction(any(InternalTransactionDto.class));
			verify(transactionService, Mockito.times(0)).getPastTransactions(any(UserDto.class));
		}
	}

	@Nested
	class showAddTransactionFromBankAccountFormTests {
		@Test
		public void showAddTransactionFromBankAccountFormTest() {
			assertEquals("add_transaction_from_bank_account", applicationController.showAddTransactionFromBankAccountForm(model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}
		@Test
		public void showAddTransactionFromBankAccountFormTestIfModelNull() {
			assertThrows(NullPointerException.class, () -> applicationController.showAddTransactionFromBankAccountForm(null, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}
		@Test
		public void showAddTransactionFromBankAccountFormTestIfPrincipalNull() {
			assertThrows(NullPointerException.class, () -> applicationController.showAddTransactionFromBankAccountForm(model, null));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
		}

		@Test
		public void showAddTransactionFromBankAccountFormTestIfNoUserFound() {
			when(userService.findByUsername(any(String.class))).thenReturn(null);
			assertEquals("error", applicationController.showAddTransactionFromBankAccountForm(model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}

		@Test
		public void showAddTransactionFromBankAccountFormTestIfNoPastTransactionsFound() {
			when(transactionService.getPastTransactions(any(UserDto.class))).thenReturn(null);
			assertEquals("add_transaction_from_bank_account", applicationController.showAddTransactionFromBankAccountForm(model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}
	}

	@Nested
	class processAddTransactionFromBankAccountFormTests {
		@Test
		public void processAddTransactionFromBankAccountFormTest() {
			assertEquals("profile", applicationController.processAddTransactionFromBankAccountForm(externalTransactionDto, model, principal));
			verify(userService, Mockito.times(1+1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(1)).addExternalTransaction(any(ExternalTransactionDto.class));
		}

		@Test
		public void processAddTransactionFromBankAccountFormTestIfTransactionEmpty() {
			assertThrows(IllegalArgumentException.class, () -> applicationController.processAddTransactionFromBankAccountForm(new ExternalTransactionDto(), model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(0)).addExternalTransaction(any(ExternalTransactionDto.class));
		}

		@Test
		public void processAddTransactionFromBankAccountFormTestIfTransactionNull() {
			assertThrows(IllegalArgumentException.class, () -> applicationController.processAddTransactionFromBankAccountForm(null, model, principal));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(0)).addExternalTransaction(any(ExternalTransactionDto.class));
		}

		@Test
		public void processAddTransactionFromBankAccountFormTestIfModelNull() {
			assertEquals("error", applicationController.processAddTransactionFromBankAccountForm(externalTransactionDto, null, principal));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(0)).addExternalTransaction(any(ExternalTransactionDto.class));
		}

		@Test
		public void processAddTransactionFromBankAccountFormTestIfPrincipalNull() {
			assertEquals("error", applicationController.processAddTransactionFromBankAccountForm(externalTransactionDto, model, null));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(0)).addExternalTransaction(any(ExternalTransactionDto.class));
		}

		@Test
		public void processAddTransactionFromBankAccountFormTestIfNoUserFound() {
			when(userService.findByUsername(any(String.class))).thenReturn(null);
			assertEquals("error", applicationController.processAddTransactionFromBankAccountForm(externalTransactionDto, model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(0)).addExternalTransaction(any(ExternalTransactionDto.class));
		}

		@Test
		public void processAddTransactionFromBankAccountFormTestIfNoPastTransactionFound() {
			when(transactionService.getPastTransactions(any(UserDto.class))).thenReturn(null);
			assertEquals("profile", applicationController.processAddTransactionFromBankAccountForm(externalTransactionDto, model, principal));
			verify(userService, Mockito.times(1+1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(1)).addExternalTransaction(any(ExternalTransactionDto.class));
		}

		@Test
		public void processAddTransactionFromBankAccountFormTestIfErrorOnAddTransaction() {
			when(transactionService.addExternalTransaction(any(ExternalTransactionDto.class))).thenThrow(new IllegalArgumentException());
			assertThrows(Exception.class, () -> applicationController.processAddTransactionFromBankAccountForm(externalTransactionDto, model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(1)).addExternalTransaction(any(ExternalTransactionDto.class));
		}
	}

	@Nested
	class showAddTransactionToBankAccountFormTests {
		@Test
		public void showAddTransactionToBankAccountFormTest() {
			assertEquals("add_transaction_to_bank_account", applicationController.showAddTransactionToBankAccountForm(model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}
		@Test
		public void showAddTransactionToBankAccountFormTestIfModelNull() {
			assertThrows(NullPointerException.class, () -> applicationController.showAddTransactionToBankAccountForm(null, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}
		@Test
		public void showAddTransactionToBankAccountFormTestIfPrincipalNull() {
			assertThrows(NullPointerException.class, () -> applicationController.showAddTransactionToBankAccountForm(model, null));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
		}

		@Test
		public void showAddTransactionToBankAccountFormTestIfNoUserFound() {
			when(userService.findByUsername(any(String.class))).thenReturn(null);
			assertEquals("error", applicationController.showAddTransactionToBankAccountForm(model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}

		@Test
		public void showAddTransactionToBankAccountFormTestIfNoPastTransactionsFound() {
			when(transactionService.getPastTransactions(any(UserDto.class))).thenReturn(null);
			assertEquals("add_transaction_to_bank_account", applicationController.showAddTransactionToBankAccountForm(model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}
	}

	@Nested
	class processAddTransactionToBankAccountFormTests {
		@Test
		public void processAddTransactionToBankAccountFormTest() {
			assertEquals("profile", applicationController.processAddTransactionToBankAccountForm(externalTransactionDto, model, principal));
			verify(userService, Mockito.times(1+1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(1)).addExternalTransaction(any(ExternalTransactionDto.class));
		}

		@Test
		public void processAddTransactionToBankAccountFormTestIfTransactionEmpty() {
			assertThrows(IllegalArgumentException.class, () -> applicationController.processAddTransactionToBankAccountForm(new ExternalTransactionDto(), model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(0)).addExternalTransaction(any(ExternalTransactionDto.class));
		}

		@Test
		public void processAddTransactionToBankAccountFormTestIfTransactionNull() {
			assertThrows(IllegalArgumentException.class, () -> applicationController.processAddTransactionToBankAccountForm(null, model, principal));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(0)).addExternalTransaction(any(ExternalTransactionDto.class));
		}

		@Test
		public void processAddTransactionToBankAccountFormTestIfModelNull() {
			assertEquals("error", applicationController.processAddTransactionToBankAccountForm(externalTransactionDto, null, principal));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(0)).addExternalTransaction(any(ExternalTransactionDto.class));
		}

		@Test
		public void processAddTransactionToBankAccountFormTestIfPrincipalNull() {
			assertEquals("error", applicationController.processAddTransactionToBankAccountForm(externalTransactionDto, model, null));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(0)).addExternalTransaction(any(ExternalTransactionDto.class));
		}

		@Test
		public void processAddTransactionToBankAccountFormTestIfNoUserFound() {
			when(userService.findByUsername(any(String.class))).thenReturn(null);
			assertEquals("error", applicationController.processAddTransactionToBankAccountForm(externalTransactionDto, model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(0)).addExternalTransaction(any(ExternalTransactionDto.class));
		}

		@Test
		public void processAddTransactionToBankAccountFormTestIfNoPastTransactionFound() {
			when(transactionService.getPastTransactions(any(UserDto.class))).thenReturn(null);
			assertEquals("profile", applicationController.processAddTransactionToBankAccountForm(externalTransactionDto, model, principal));
			verify(userService, Mockito.times(1+1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(1)).addExternalTransaction(any(ExternalTransactionDto.class));
		}

		@Test
		public void processAddTransactionToBankAccountFormTestIfErrorOnAddTransaction() {
			when(transactionService.addExternalTransaction(any(ExternalTransactionDto.class))).thenThrow(new IllegalArgumentException());
			assertThrows(Exception.class, () -> applicationController.processAddTransactionToBankAccountForm(externalTransactionDto, model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(transactionService, Mockito.times(1)).addExternalTransaction(any(ExternalTransactionDto.class));
		}
	}

}
