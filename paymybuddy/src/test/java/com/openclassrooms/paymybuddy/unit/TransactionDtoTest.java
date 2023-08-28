package com.openclassrooms.paymybuddy.unit;

import com.openclassrooms.paymybuddy.dto.TransactionDto;
import com.openclassrooms.paymybuddy.entity.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = TransactionDto.class)
public class TransactionDtoTest {
    @Autowired
    TransactionDto transactionDto;
    @Autowired
    TransactionDto transactionDtoOther;
    @BeforeEach
    private void setUp() {
        transactionDto = new TransactionDto(1, 10.0, 0.05*10.0, 1, "description", "iban", 1, 2, new Timestamp(0), false);
        transactionDtoOther = new TransactionDto(2, 20.0, 0.05*10.0, 2, "descriptionOther", "ibanOther", 3, 4, new Timestamp(0), true);
    }
    @Nested
    class equalsTests {

        @Nested
        class thisNotEmpty {
            @Test
            public void equalsTest() {
                assertTrue(transactionDto.equals(transactionDto));
            }

            @Test
            public void equalsTestIfEmpty() {
                assertFalse(transactionDto.equals(new TransactionDto()));
            }

            @Test
            public void equalsTestIfNull() {
                assertFalse(transactionDto.equals(null));
            }

            @Test
            public void equalsTestIfNotTransactionDto() {
                assertFalse(transactionDto.equals("notTransactionDto"));
            }

            @Test
            public void equalsTestIfDifferentTransactionDto() {
                assertFalse(transactionDto.equals(transactionDtoOther));
            }
        }

        @Nested
        class thisEmpty {

            @Test
            public void equalsTestIfEmpty() {
                assertTrue(new TransactionDto().equals(new TransactionDto()));
            }

            @Test
            public void equalsTestIfNull() {
                assertTrue(new TransactionDto().equals(null));
            }

            @Test
            public void equalsTestIfNotTransactionDto() {
                assertFalse(new TransactionDto().equals("notTransactionDto"));
            }

            @Test
            public void equalsTestIfDifferentTransactionDto() {
                assertFalse(new TransactionDto().equals(transactionDtoOther));
            }
        }
    }
}
