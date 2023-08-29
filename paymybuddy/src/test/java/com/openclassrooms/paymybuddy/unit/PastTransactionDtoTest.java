package com.openclassrooms.paymybuddy.unit;

import com.openclassrooms.paymybuddy.dto.PastTransactionDto;
import com.openclassrooms.paymybuddy.dto.TransactionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = PastTransactionDto.class)
public class PastTransactionDtoTest {
    @Autowired
    PastTransactionDto pastTransactionDto;
    @Autowired
    PastTransactionDto pastTransactionDtoOther;
    @BeforeEach
    private void setUp() {
        pastTransactionDto = new PastTransactionDto(1, "username", "description", 10.0, "currency");
        pastTransactionDtoOther = new PastTransactionDto(2, "usernameOther", "descriptionOther", 20.0, "currencyOther");
    }
    @Nested
    class equalsTests {

        @Nested
        class thisNotEmpty {
            @Test
            public void equalsTest() {
                assertTrue(pastTransactionDto.equals(pastTransactionDto));
            }

            @Test
            public void equalsTestIfEmpty() {
                assertFalse(pastTransactionDto.equals(new PastTransactionDto()));
            }

            @Test
            public void equalsTestIfNull() {
                assertFalse(pastTransactionDto.equals(null));
            }

            @Test
            public void equalsTestIfNotPastTransactionDto() {
                assertFalse(pastTransactionDto.equals("notPastTransactionDto"));
            }

            @Test
            public void equalsTestIfDifferentPastTransactionDto() {
                assertFalse(pastTransactionDto.equals(pastTransactionDtoOther));
            }
        }

        @Nested
        class thisEmpty {

            @Test
            public void equalsTestIfEmpty() {
                assertTrue(new PastTransactionDto().equals(new PastTransactionDto()));
            }

            @Test
            public void equalsTestIfNull() {
                assertTrue(new PastTransactionDto().equals(null));
            }

            @Test
            public void equalsTestIfNotPastTransactionDto() {
                assertFalse(new PastTransactionDto().equals("notPastTransactionDto"));
            }

            @Test
            public void equalsTestIfDifferentPastTransactionDto() {
                assertFalse(new PastTransactionDto().equals(pastTransactionDtoOther));
            }
        }
    }
}
