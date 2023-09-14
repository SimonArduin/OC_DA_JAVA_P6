package com.openclassrooms.paymybuddy.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.paymybuddy.Application;
import com.openclassrooms.paymybuddy.TestVariables;
import com.openclassrooms.paymybuddy.dto.UserDto;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ControllerIT extends TestVariables {;

    @Inject
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        initializeVariables();
    }

    @Test
    void contextLoads() {
    }
    @Nested
    class viewHomePageTests {

        @WithMockUser("user01")
        @Test
        public void viewHomePageTest() throws Exception {
            mockMvc.perform(get("/").with(csrf()))
                    .andExpect(status().is2xxSuccessful());
        }
        @Test
        public void viewHomePageTestIfNotAuthenticated() throws Exception {
            mockMvc.perform(get("/").with(csrf()))
                    .andExpect(status().is2xxSuccessful());
        }
    }


    @Nested
    class showRegistrationFormTests {

        String getMapping = "/register";
        String viewName = "signup_form";

        @WithMockUser
        @Test
        public void showRegistrationFormTest() throws Exception {
            mockMvc.perform(get(getMapping).with(csrf()))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(view().name(viewName));
        }
        @Test
        public void showRegistrationFormTestIfNotAuthenticated() throws Exception {
            mockMvc.perform(get(getMapping).with(csrf()))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(view().name(viewName));
        }
    }

    @Nested
    class processRegisterTests {

        String getMapping = "/process_register";
        String viewName = "register_success";
        /*
        String form = "username="+userDto.getUsername()
                +"&email="+userDto.getEmail()
                +"&password="+userDto.getPassword()
                +"&iban="+userDto.getIban();
        */
        String form = "username=username&email=email&password=password&iban=iban";

        @WithMockUser("user01")
        @Test
        public void processRegisterTest() throws Exception {
            mockMvc.perform(post(getMapping).with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .content(form))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(view().name(viewName));
        }

        @Test
        public void processRegisterTestIfNotAuthenticated() throws Exception {
            mockMvc.perform(post(getMapping).with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .content(form))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(view().name(viewName));
        }

        @Test
        public void processRegisterTestIfEmpty() throws Exception {
            mockMvc.perform(post(getMapping)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .content("username=&email=&password=&iban="))
                    .andExpect(status().is4xxClientError());
        }
        @Test
        public void processRegisterTestIfNull() throws Exception {
            mockMvc.perform(post(getMapping).with(csrf()))
                    .andExpect(status().is4xxClientError());
        }
    }

    @Nested
    class profileTests {

        String getMapping = "/profile";
        String viewName = "profile";
        
        @WithMockUser("user01")
        @Test
        public void profileTest() throws Exception {
            mockMvc.perform(get(getMapping).with(csrf()))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(view().name(viewName));
        }

        @Test
        public void profileTestIfNotAuthenticated() throws Exception {
            mockMvc.perform(get(getMapping).with(csrf()))
                    .andExpect(status().is3xxRedirection());
        }

    }

    @Nested
    class contactTests {

        String getMapping = "/contact";
        String viewName = "contact";

        @WithMockUser("user01")
        @Test
        public void contactTest() throws Exception {
            mockMvc.perform(get(getMapping).with(csrf()))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(view().name(viewName));
        }

        @Test
        public void contactTestIfNotAuthenticated() throws Exception {
            mockMvc.perform(get(getMapping).with(csrf()))
                    .andExpect(status().is3xxRedirection());
        }
    }

    @Nested
    class showAddConnectionFormTests {

        String getMapping = "/add_connection";
        String viewName = "add_connection_form";

        @WithMockUser("user01")
        @Test
        public void showAddConnectionFormTest() throws Exception {
            mockMvc.perform(get(getMapping).with(csrf()))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(view().name(viewName));
        }

        @Test
        public void showAddConnectionFormTestIfNotAuthenticated() throws Exception {
            mockMvc.perform(get(getMapping).with(csrf()))
                    .andExpect(status().is3xxRedirection());
        }
    }

    @Nested
    class processAddConnectionTests {

        String getMapping = "/process_add_connection";
        String viewName = "contact";

        String form = "username=user02";

        @WithMockUser("user01")
        @Test
        public void processAddConnectionTest() throws Exception {
            mockMvc.perform(post(getMapping).with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .content(form))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(view().name(viewName));
        }

        @Test
        public void processAddConnectionTestIfNotAuthenticated() throws Exception {
            mockMvc.perform(post(getMapping).with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .content(form))
                    .andExpect(status().is3xxRedirection());
        }

        @WithMockUser("user01")
        @Test
        public void processAddConnectionTestIfEmpty() throws Exception {
            mockMvc.perform(post(getMapping)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .content("username="))
                    .andExpect(status().is4xxClientError());
        }

        @WithMockUser("user01")
        @Test
        public void processAddConnectionTestIfNull() throws Exception {
            mockMvc.perform(post(getMapping).with(csrf()))
                    .andExpect(status().is4xxClientError());
        }
    }

    @Nested
    class showTransferFormTests {

        String getMapping = "/transfer";
        String viewName = "transfer";

        @WithMockUser("user01")
        @Test
        public void showTransferFormTest() throws Exception {
            mockMvc.perform(get(getMapping).with(csrf()))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(view().name(viewName));
        }

        @Test
        public void showTransferFormTestIfNotAuthenticated() throws Exception {
            mockMvc.perform(get(getMapping).with(csrf()))
                    .andExpect(status().is3xxRedirection());
        }
    }

    @Nested
    class processTransferTests {

        String getMapping = "/process_transfer";
        String viewName = "transfer";

        String form = "amount=10&receiverId=16&description=description";

        @WithMockUser("user01")
        @Test
        public void processTransferTest() throws Exception {
            mockMvc.perform(post(getMapping).with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .content(form))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(view().name(viewName));
        }

        @Test
        public void processTransferTestIfNotAuthenticated() throws Exception {
            mockMvc.perform(post(getMapping).with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .content(form))
                    .andExpect(status().is3xxRedirection());
        }

        @WithMockUser("user01")
        @Test
        public void processTransferTestIfEmpty() throws Exception {
            mockMvc.perform(post(getMapping)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .content("amount=&receiverId=&description="))
                    .andExpect(status().is4xxClientError());
        }

        @WithMockUser("user01")
        @Test
        public void processTransferTestIfNull() throws Exception {
            mockMvc.perform(post(getMapping).with(csrf()))
                    .andExpect(status().is4xxClientError());
        }
    }

    @Nested
    class showAddTransactionFromBankAccountFormTests {

        String getMapping = "/add_transaction_from_bank_account";
        String viewName = "add_transaction_from_bank_account";

        @WithMockUser("user01")
        @Test
        public void showAddTransactionFromBankAccountFormTest() throws Exception {
            mockMvc.perform(get(getMapping).with(csrf()))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(view().name(viewName));
        }

        @Test
        public void showAddTransactionFromBankAccountFormTestIfNotAuthenticated() throws Exception {
            mockMvc.perform(get(getMapping).with(csrf()))
                    .andExpect(status().is3xxRedirection());
        }
    }

    @Nested
    class processAddTransactionFromBankAccountFormTests {

        String getMapping = "/process_add_transaction_from_bank_account";
        String viewName = "profile";

        String form = "amount=10";

        @WithMockUser("user01")
        @Test
        public void processAddTransactionFromBankAccountFormTest() throws Exception {
            mockMvc.perform(post(getMapping).with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .content(form))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(view().name(viewName));
        }

        @Test
        public void processAddTransactionFromBankAccountFormTestIfNotAuthenticated() throws Exception {
            mockMvc.perform(post(getMapping).with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .content(form))
                    .andExpect(status().is3xxRedirection());
        }

        @WithMockUser("user01")
        @Test
        public void processAddTransactionFromBankAccountFormTestIfEmpty() throws Exception {
            mockMvc.perform(post(getMapping)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .content("amount="))
                    .andExpect(status().is4xxClientError());
        }

        @WithMockUser("user01")
        @Test
        public void processAddTransactionFromBankAccountFormTestIfNull() throws Exception {
            mockMvc.perform(post(getMapping).with(csrf()))
                    .andExpect(status().is4xxClientError());
        }
    }

    @Nested
    class showAddTransactionToBankAccountFormTests {

        String getMapping = "/add_transaction_to_bank_account";
        String viewName = "add_transaction_to_bank_account";

        @WithMockUser("user01")
        @Test
        public void showAddTransactionToBankAccountFormTest() throws Exception {
            mockMvc.perform(get(getMapping).with(csrf()))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(view().name(viewName));
        }

        @Test
        public void showAddTransactionToBankAccountFormTestIfNotAuthenticated() throws Exception {
            mockMvc.perform(get(getMapping).with(csrf()))
                    .andExpect(status().is3xxRedirection());
        }
    }

    @Nested
    class processAddTransactionToBankAccountFormTests {

        String getMapping = "/process_add_transaction_to_bank_account";
        String viewName = "profile";

        String form = "amount=10";

        @WithMockUser("user01")
        @Test
        public void processAddTransactionToBankAccountFormTest() throws Exception {
            mockMvc.perform(post(getMapping).with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .content(form))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(view().name(viewName));
        }

        @Test
        public void processAddTransactionToBankAccountFormTestIfNotAuthenticated() throws Exception {
            mockMvc.perform(post(getMapping).with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .content(form))
                    .andExpect(status().is3xxRedirection());
        }

        @WithMockUser("user01")
        @Test
        public void processAddTransactionToBankAccountFormTestIfEmpty() throws Exception {
            mockMvc.perform(post(getMapping)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .content("amount="))
                    .andExpect(status().is4xxClientError());
        }

        @WithMockUser("user01")
        @Test
        public void processAddTransactionToBankAccountFormTestIfNull() throws Exception {
            mockMvc.perform(post(getMapping).with(csrf()))
                    .andExpect(status().is4xxClientError());
        }
    }
}
