package com.openclassrooms.paymybuddy.unit;

import com.openclassrooms.paymybuddy.entity.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = Transaction.class)
public class TransactionTest {
    @Autowired
    Transaction transaction;
    @Autowired
    Transaction transactionOther;
    @BeforeEach
    private void setUp() {
        transaction = new Transaction(1, 10.0, 0.05*10.0, 1, "description", "iban", 1, 2, new Timestamp(0), false);
        transactionOther = new Transaction(2, 20.0, 0.05*10.0, 2, "descriptionOther", "ibanOther", 3, 4, new Timestamp(0), true);
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
}
