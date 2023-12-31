package com.openclassrooms.paymybuddy.unit;

import com.openclassrooms.paymybuddy.TestVariables;
import com.openclassrooms.paymybuddy.dto.InternalTransactionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = InternalTransactionDto.class)
public class InternalTransactionDtoTest extends TestVariables {
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
                assertTrue(internalTransactionDto.equals(internalTransactionDto));
            }

            @Test
            public void equalsTestIfEmpty() {
                assertFalse(internalTransactionDto.equals(new InternalTransactionDto()));
            }

            @Test
            public void equalsTestIfNull() {
                assertFalse(internalTransactionDto.equals(null));
            }

            @Test
            public void equalsTestIfNotInternalTransactionDto() {
                assertFalse(internalTransactionDto.equals("notInternalTransactionDto"));
            }

            @Test
            public void equalsTestIfDifferentInternalTransactionDto() {
                assertFalse(internalTransactionDto.equals(internalTransactionDtoOther));
            }
        }

        @Nested
        class thisEmpty {

            @Test
            public void equalsTestIfEmpty() {
                assertTrue(new InternalTransactionDto().equals(new InternalTransactionDto()));
            }

            @Test
            public void equalsTestIfNull() {
                assertTrue(new InternalTransactionDto().equals(null));
            }

            @Test
            public void equalsTestIfNotInternalTransactionDto() {
                assertFalse(new InternalTransactionDto().equals("notInternalTransactionDto"));
            }

            @Test
            public void equalsTestIfDifferentInternalTransactionDto() {
                assertFalse(new InternalTransactionDto().equals(internalTransactionDtoOther));
            }
        }
    }

    @Nested
    class isEmptyTests {
        @Test
        public void isEmptyTest() {
            assertFalse(internalTransactionDto.isEmpty());
        }

        @Test
        public void isEmptyTestIfNoAmount() {
            internalTransactionDto.setAmount(null);
            assertTrue(internalTransactionDto.isEmpty());
        }

        @Test
        public void isEmptyTestIfNoSenderId() {
            internalTransactionDto.setSenderId(null);
            assertTrue(internalTransactionDto.isEmpty());
        }

        @Test
        public void isEmptyTestIfNoReceiverId() {
            internalTransactionDto.setReceiverId(null);
            assertTrue(internalTransactionDto.isEmpty());
        }

        @Test
        public void isEmptyTestIfEmpty() {
            assertTrue(new InternalTransactionDto().isEmpty());
        }
    }
}
