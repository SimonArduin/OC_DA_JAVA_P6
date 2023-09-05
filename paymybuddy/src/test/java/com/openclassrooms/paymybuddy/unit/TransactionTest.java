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
                assertTrue(transaction.equals(transaction));
            }

            @Test
            public void equalsTestIfEmpty() {
                assertFalse(transaction.equals(new Transaction()));
            }

            @Test
            public void equalsTestIfNull() {
                assertFalse(transaction.equals(null));
            }

            @Test
            public void equalsTestIfNotTransaction() {
                assertFalse(transaction.equals("notTransaction"));
            }

            @Test
            public void equalsTestIfDifferentTransaction() {
                assertFalse(transaction.equals(transactionOther));
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
            assertFalse(transaction.isEmpty());
        }

        @Test
        public void isEmptyTestIfNoAmount() {
            transaction.setAmount(null);
            assertTrue(transaction.isEmpty());
        }

        @Test
        public void isEmptyTestIfNoSenderId() {
            transaction.setSenderId(null);
            assertTrue(transaction.isEmpty());
        }

        @Test
        public void isEmptyTestIfNoCurrencyId() {
            transaction.setCurrencyId(null);
            assertTrue(transaction.isEmpty());
        }

        @Test
        public void isEmptyTestIfNoTimestampId() {
            transaction.setTimestamp(null);
            assertTrue(transaction.isEmpty());
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
            assertTrue(transaction.isExternalTransaction());
        }

        @Test
        public void isExternalTransactionTestIfNoIban() {
            transaction.setIban(null);
            assertFalse(transaction.isExternalTransaction());
        }

        @Test
        public void isExternalTransactionTestIfNoToIban() {
            transaction.setToIban(null);
            assertFalse(transaction.isExternalTransaction());
        }

        @Test
        public void isExternalTransactionTestIfEmpty() {
            assertFalse(new Transaction().isExternalTransaction());
        }
    }
    @Nested
    class isInternalTransactionTests {
        @Test
        public void isInternalTransactionTest() {
            assertTrue(transaction.isInternalTransaction());
        }

        @Test
        public void isInternalTransactionTestIfNoReceiverId() {
            transaction.setReceiverId(null);
            assertFalse(transaction.isInternalTransaction());
        }

        @Test
        public void isInternalTransactionTestIfEmpty() {
            assertFalse(new Transaction().isInternalTransaction());
        }
    }
}
