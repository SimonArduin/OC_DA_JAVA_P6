package com.openclassrooms.paymybuddy.unit;

import com.openclassrooms.paymybuddy.entity.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = Transaction.class)
public class TransactionTest extends TestVariables{
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
                assertTrue(internalTransaction.equals(internalTransaction));
            }

            @Test
            public void equalsTestIfEmpty() {
                assertFalse(internalTransaction.equals(new Transaction()));
            }

            @Test
            public void equalsTestIfNull() {
                assertFalse(internalTransaction.equals(null));
            }

            @Test
            public void equalsTestIfNotTransaction() {
                assertFalse(internalTransaction.equals("notTransaction"));
            }

            @Test
            public void equalsTestIfDifferentTransaction() {
                assertFalse(internalTransaction.equals(transactionOther));
            }
        }

        @Nested
        class thisEmpty {

            @Test
            public void equalsTestIfEmpty() {
                assertTrue(new Transaction().equals(new Transaction()));
            }

            @Test
            public void equalsTestIfNull() {
                assertTrue(new Transaction().equals(null));
            }

            @Test
            public void equalsTestIfNotTransaction() {
                assertFalse(new Transaction().equals("notTransaction"));
            }

            @Test
            public void equalsTestIfDifferentTransaction() {
                assertFalse(new Transaction().equals(transactionOther));
            }
        }
    }
    @Nested
    class isEmptyTests {
        @Test
        public void isEmptyTest() {
            assertFalse(internalTransaction.isEmpty());
        }

        @Test
        public void isEmptyTestIfNoAmount() {
            internalTransaction.setAmount(null);
            assertTrue(internalTransaction.isEmpty());
        }

        @Test
        public void isEmptyTestIfNoSenderId() {
            internalTransaction.setSenderId(null);
            assertTrue(internalTransaction.isEmpty());
        }

        @Test
        public void isEmptyTestIfNoCurrencyId() {
            internalTransaction.setCurrencyId(null);
            assertTrue(internalTransaction.isEmpty());
        }

        @Test
        public void isEmptyTestIfNoTimestampId() {
            internalTransaction.setTimestamp(null);
            assertTrue(internalTransaction.isEmpty());
        }

        @Test
        public void isEmptyTestIfEmpty() {
            assertTrue(new Transaction().isEmpty());
        }
    }
    @Nested
    class isExternalTransactionTests {
        @Test
        public void isExternalTransactionTest() {
            assertTrue(externalTransaction.isExternalTransaction());
        }

        @Test
        public void isExternalTransactionTestIfNoIban() {
            externalTransaction.setIban(null);
            assertFalse(externalTransaction.isExternalTransaction());
        }

        @Test
        public void isExternalTransactionTestIfNoToIban() {
            externalTransaction.setToIban(null);
            assertFalse(externalTransaction.isExternalTransaction());
        }

        @Test
        public void isExternalTransactionTestIfEmpty() {
            assertFalse(new Transaction().isExternalTransaction());
        }

        @Test
        public void isExternalTransactionTestIfInternal() {
            assertFalse(internalTransaction.isExternalTransaction());
        }
    }
    @Nested
    class isInternalTransactionTests {
        @Test
        public void isInternalTransactionTest() {
            assertTrue(internalTransaction.isInternalTransaction());
        }

        @Test
        public void isInternalTransactionTestIfNoReceiverId() {
            internalTransaction.setReceiverId(null);
            assertFalse(internalTransaction.isInternalTransaction());
        }

        @Test
        public void isInternalTransactionTestIfEmpty() {
            assertFalse(new Transaction().isInternalTransaction());
        }

        @Test
        public void isInternalTransactionTestIfExternal() {
            assertFalse(externalTransaction.isInternalTransaction());
        }
    }
}
