package com.openclassrooms.paymybuddy.unit;

import com.openclassrooms.paymybuddy.TestVariables;
import com.openclassrooms.paymybuddy.dto.ExternalTransactionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = ExternalTransactionDto.class)
public class ExternalTransactionDtoTest extends TestVariables {
    @BeforeEach
    private void setUp() {
        initializeVariables();
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
