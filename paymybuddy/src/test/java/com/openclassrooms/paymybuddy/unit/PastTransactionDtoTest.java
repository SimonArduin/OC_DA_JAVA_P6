package com.openclassrooms.paymybuddy.unit;

import com.openclassrooms.paymybuddy.dto.PastTransactionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = PastTransactionDto.class)
public class PastTransactionDtoTest extends TestVariables {
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


    @Nested
    class isEmptyTests {
        @Test
        public void isEmptyTest() {
            assertFalse(pastTransactionDto.isEmpty());
        }

        @Test
        public void isEmptyTestIfNoUsername() {
            pastTransactionDto.setUsername(null);
            assertTrue(pastTransactionDto.isEmpty());
        }

        @Test
        public void isEmptyTestIfNoAmount() {
            pastTransactionDto.setAmount(null);
            assertTrue(pastTransactionDto.isEmpty());
        }

        @Test
        public void isEmptyTestIfNoDescription() {
            pastTransactionDto.setDescription(null);
            assertTrue(pastTransactionDto.isEmpty());
        }

        @Test
        public void isEmptyTestIfNoCurrency() {
            pastTransactionDto.setCurrencySymbol(null);
            assertTrue(pastTransactionDto.isEmpty());
        }

        @Test
        public void isEmptyTestIfEmpty() {
            assertTrue(new PastTransactionDto().isEmpty());
        }
    }
}
