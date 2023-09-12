package com.openclassrooms.paymybuddy.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.paymybuddy.TestVariables;
import com.openclassrooms.paymybuddy.dto.UserDto;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ControllerIT extends TestVariables {

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

        @WithMockUser
        @Test
        public void showRegistrationFormTest() throws Exception {
            mockMvc.perform(get("/register").with(csrf()))
                    .andExpect(status().is2xxSuccessful());
        }
        @Test
        public void showRegistrationFormTestIfNotAuthenticated() throws Exception {
            mockMvc.perform(get("/register").with(csrf()))
                    .andExpect(status().is3xxRedirection());
        }
    }

    @Nested
    class processRegisterTests {

        @WithMockUser("user01")
        @Test
        public void processRegisterTest() throws Exception {
            mockMvc.perform(post("/process_register").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(userDto)))
                    .andExpect(status().is2xxSuccessful());
        }

        @Test
        public void processRegisterTestIfNotAuthenticated() throws Exception {
            mockMvc.perform(post("/process_register").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(userDto)))
                    .andExpect(status().is2xxSuccessful());
        }

        @Test
        public void processRegisterTestIfEmpty() throws Exception {
            mockMvc.perform(post("/process_register").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(new UserDto())))
                    .andExpect(status().is4xxClientError());
        }
        @Test
        public void processRegisterTestIfNull() throws Exception {
            mockMvc.perform(post("/process_register").with(csrf()))
                    .andExpect(status().is4xxClientError());
        }
    }

    @Nested
    class profileTests {
        @WithMockUser("user01")
        @Test
        public void profileTest() throws Exception {
            mockMvc.perform(get("/profile").with(csrf()))
                    .andExpect(status().is2xxSuccessful());
        }

        @Test
        public void profileTestIfNotAuthenticated() throws Exception {
            mockMvc.perform(get("/profile").with(csrf()))
                    .andExpect(status().is3xxRedirection());
        }

    }

    @Nested
    class contactTests {

        @WithMockUser("user01")
        @Test
        public void contactTest() throws Exception {
        }
    }

    @Nested
    class showAddConnectionFormTests {

        @WithMockUser("user01")
        @Test
        public void showAddConnectionFormTest() throws Exception {
        }
    }

    @Nested
    class processAddConnectionTests {

        @WithMockUser("user01")
        @Test
        public void processAddConnectionTest() throws Exception {
        }

        @WithMockUser("user01")
        @Test
        public void processAddConnectionTestIfConnectionNull() throws Exception {
        }

        @WithMockUser("user01")
        @Test
        public void processAddConnectionTestIfConnectionEmpty() throws Exception {
            
        }
    }

    @Nested
    class showTransferFormTests {

        @WithMockUser("user01")
        @Test
        public void showTransferFormTest() throws Exception {
        }
    }

    @Nested
    class processTransferTests {

        @WithMockUser("user01")
        @Test
        public void processTransferTest() throws Exception {
        }

        @WithMockUser("user01")
        @Test
        public void processTransferTestIfTransactionNull() throws Exception {
        }
    }

    @Nested
    class showAddTransactionFromBankAccountFormTests {

        @WithMockUser("user01")
        @Test
        public void showAddTransactionFromBankAccountFormTest() throws Exception {
        }
    }

    @Nested
    class processAddTransactionFromBankAccountFormTests {

        @WithMockUser("user01")
        @Test
        public void processAddTransactionFromBankAccountFormTest() throws Exception {
            
        }

        @WithMockUser("user01")
        @Test
        public void processAddTransactionFromBankAccountFormTestIfTransactionNull() throws Exception {
            
        }
    }

    @Nested
    class showAddTransactionToBankAccountFormTests {

        @WithMockUser("user01")
        @Test
        public void showAddTransactionToBankAccountFormTest() throws Exception {
        }
    }

    @Nested
    class processAddTransactionToBankAccountFormTests {

        @WithMockUser("user01")
        @Test
        public void processAddTransactionToBankAccountFormTest() throws Exception {
            
        }

        @WithMockUser("user01")
        @Test
        public void processAddTransactionToBankAccountFormTestIfTransactionNull() throws Exception {
            
        }
    }
}
