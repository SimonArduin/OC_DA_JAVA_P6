package com.openclassrooms.paymybuddy.unit;

import com.openclassrooms.paymybuddy.TestVariables;
import com.openclassrooms.paymybuddy.controller.ApplicationController;
import com.openclassrooms.paymybuddy.dto.*;
import com.openclassrooms.paymybuddy.service.GlobalService;
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
	private GlobalService globalService;

	@BeforeEach
	public void SetUp() {
		initializeVariables();

		when(userService.findById(any(Integer.class))).thenReturn(userDto);
		when(userService.findByUsername(any(String.class))).thenReturn(userDto);
		when(userService.addUser(any(UserDto.class))).thenReturn(userDto);
		when(userService.addConnectionToUser(any(UserDto.class), any(UserDto.class))).thenReturn(userDto);
		when(globalService.getPastTransactions(userDto)).thenReturn(pastTransactionDtoList);
		when(globalService.addInternalTransaction(any(InternalTransactionDto.class))).thenReturn(internalTransactionDto);
		when(globalService.addExternalTransaction(any(ExternalTransactionDto.class))).thenReturn(externalTransactionDto);
	}

	@Test
	void contextLoads() {
	}
	@Nested
	class viewHomePageTests {
		@Test
		public void viewHomePageTest() {
			assertEquals("index", applicationController.viewHomePage());
		}
	}


	@Nested
	class showRegistrationFormTests {
		@Test
		public void showRegistrationFormTest() {
			assertEquals("signup_form", applicationController.showRegistrationForm(model));
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
	}

	@Nested
	class profileTests {
		@Test
		public void profileTest() {
			assertEquals("profile", applicationController.profile(model, principal));
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
	}

	@Nested
	class showAddConnectionFormTests {
		@Test
		public void showAddConnectionFormTest() {
			assertEquals("add_connection_form", applicationController.showAddConnectionForm(model));
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
	}

	@Nested
	class showTransferFormTests {
		@Test
		public void showTransferFormTest() {
			assertEquals("transfer", applicationController.showTransferForm(model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
			verify(globalService, Mockito.times(1)).getPastTransactions(any(UserDto.class));
		}
	}

	@Nested
	class processTransferTests {
		@Test
		public void processTransferTest() {
			assertEquals("transfer", applicationController.processTransfer(internalTransactionDto, model, principal));
			verify(userService, Mockito.times(1+1)).findByUsername(any(String.class));
			verify(globalService, Mockito.times(1)).addInternalTransaction(any(InternalTransactionDto.class));
			verify(globalService, Mockito.times(1+1)).getPastTransactions(any(UserDto.class));
		}

		@Test
		public void processTransferTestIfTransactionNull() {
			assertThrows(IllegalArgumentException.class, () -> applicationController.processTransfer(null, model, principal));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
			verify(globalService, Mockito.times(0)).addInternalTransaction(any(InternalTransactionDto.class));
			verify(globalService, Mockito.times(0)).getPastTransactions(any(UserDto.class));
		}
	}

	@Nested
	class showAddTransactionFromBankAccountFormTests {
		@Test
		public void showAddTransactionFromBankAccountFormTest() {
			assertEquals("add_transaction_from_bank_account", applicationController.showAddTransactionFromBankAccountForm(model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}
	}

	@Nested
	class processAddTransactionFromBankAccountFormTests {
		@Test
		public void processAddTransactionFromBankAccountFormTest() {
			assertEquals("profile", applicationController.processAddTransactionFromBankAccount(externalTransactionDto, model, principal));
			verify(userService, Mockito.times(1+1)).findByUsername(any(String.class));
			verify(globalService, Mockito.times(1)).addExternalTransaction(any(ExternalTransactionDto.class));
		}

		@Test
		public void processAddTransactionFromBankAccountFormTestIfTransactionNull() {
			assertThrows(IllegalArgumentException.class, () -> applicationController.processAddTransactionFromBankAccount(null, model, principal));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
			verify(globalService, Mockito.times(0)).addExternalTransaction(any(ExternalTransactionDto.class));
		}
	}

	@Nested
	class showAddTransactionToBankAccountFormTests {
		@Test
		public void showAddTransactionToBankAccountFormTest() {
			assertEquals("add_transaction_to_bank_account", applicationController.showAddTransactionToBankAccountForm(model, principal));
			verify(userService, Mockito.times(1)).findByUsername(any(String.class));
		}
	}

	@Nested
	class processAddTransactionToBankAccountFormTests {
		@Test
		public void processAddTransactionToBankAccountFormTest() {
			assertEquals("profile", applicationController.processAddTransactionToBankAccount(externalTransactionDto, model, principal));
			verify(userService, Mockito.times(1 + 1)).findByUsername(any(String.class));
			verify(globalService, Mockito.times(1)).addExternalTransaction(any(ExternalTransactionDto.class));
		}

		@Test
		public void processAddTransactionToBankAccountFormTestIfTransactionNull() {
			assertThrows(IllegalArgumentException.class, () -> applicationController.processAddTransactionToBankAccount(null, model, principal));
			verify(userService, Mockito.times(0)).findByUsername(any(String.class));
			verify(globalService, Mockito.times(0)).addExternalTransaction(any(ExternalTransactionDto.class));
		}
	}
}
