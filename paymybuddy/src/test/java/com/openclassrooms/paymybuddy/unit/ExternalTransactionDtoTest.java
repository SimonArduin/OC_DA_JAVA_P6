package com.openclassrooms.paymybuddy.unit;

import com.openclassrooms.paymybuddy.dto.ExternalTransactionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = ExternalTransactionDto.class)
public class ExternalTransactionDtoTest {
    @Autowired
    ExternalTransactionDto externalTransactionDto;
    @Autowired
    ExternalTransactionDto externalTransactionDtoOther;
    @BeforeEach
    private void setUp() {
        externalTransactionDto = new ExternalTransactionDto(1, 10.0, 0.05*10.0, 1, "description", "iban", 1, new Timestamp(0), false);
        externalTransactionDtoOther = new ExternalTransactionDto(2, 20.0, 0.05*10.0, 2, "descriptionOther", "ibanOther", 3, new Timestamp(0), true);
    }
    @Nested
    class equalsTests {

        @Nested
        class thisNotEmpty {
            @Test
            public void equalsTest() {
                assertTrue(externalTransactionDto.equals(externalTransactionDto));
            }

            @Test
            public void equalsTestIfEmpty() {
                assertFalse(externalTransactionDto.equals(new ExternalTransactionDto()));
            }

            @Test
            public void equalsTestIfNull() {
                assertFalse(externalTransactionDto.equals(null));
            }

            @Test
            public void equalsTestIfNotExternalTransactionDto() {
                assertFalse(externalTransactionDto.equals("notExternalTransactionDto"));
            }

            @Test
            public void equalsTestIfDifferentExternalTransactionDto() {
                assertFalse(externalTransactionDto.equals(externalTransactionDtoOther));
            }
        }

        @Nested
        class thisEmpty {

            @Test
            public void equalsTestIfEmpty() {
                assertTrue(new ExternalTransactionDto().equals(new ExternalTransactionDto()));
            }

            @Test
            public void equalsTestIfNull() {
                assertTrue(new ExternalTransactionDto().equals(null));
            }

            @Test
            public void equalsTestIfNotExternalTransactionDto() {
                assertFalse(new ExternalTransactionDto().equals("notExternalTransactionDto"));
            }

            @Test
            public void equalsTestIfDifferentExternalTransactionDto() {
                assertFalse(new ExternalTransactionDto().equals(externalTransactionDtoOther));
            }
        }
    }

    @Nested
    class isEmptyTests {
        @Test
        public void isEmptyTest() {
            assertFalse(externalTransactionDto.isEmpty());
        }

        @Test
        public void isEmptyTestIfNoAmount() {
            externalTransactionDto.setAmount(null);
            assertTrue(externalTransactionDto.isEmpty());
        }

        @Test
        public void isEmptyTestIfNoSenderId() {
            externalTransactionDto.setSenderId(null);
            assertTrue(externalTransactionDto.isEmpty());
        }

        @Test
        public void isEmptyTestIfNoIban() {
            externalTransactionDto.setIban(null);
            assertTrue(externalTransactionDto.isEmpty());
        }

        @Test
        public void isEmptyTestIfEmpty() {
            assertTrue(new ExternalTransactionDto().isEmpty());
        }
    }
}
