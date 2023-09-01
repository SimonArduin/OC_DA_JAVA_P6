package com.openclassrooms.paymybuddy.unit;

import com.openclassrooms.paymybuddy.dto.DatabaseTransactionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = DatabaseTransactionDto.class)
public class DatabaseTransactionDtoTest extends TestVariables {
    @BeforeEach
    private void setUp() {
        initializeVariables();

        databaseTransactionDto = new DatabaseTransactionDto(transaction);
        databaseTransactionDtoOther = new DatabaseTransactionDto(transactionOther);
    }
    @Nested
    class equalsTests {

        @Nested
        class thisNotEmpty {
            @Test
            public void equalsTest() {
                assertTrue(databaseTransactionDto.equals(databaseTransactionDto));
            }

            @Test
            public void equalsTestIfEmpty() {
                assertFalse(databaseTransactionDto.equals(new DatabaseTransactionDto()));
            }

            @Test
            public void equalsTestIfNull() {
                assertFalse(databaseTransactionDto.equals(null));
            }

            @Test
            public void equalsTestIfNotDatabaseTransactionDto() {
                assertFalse(databaseTransactionDto.equals("notDatabaseTransactionDto"));
            }

            @Test
            public void equalsTestIfDifferentDatabaseTransactionDto() {
                assertFalse(databaseTransactionDto.equals(databaseTransactionDtoOther));
            }
        }

        @Nested
        class thisEmpty {

            @Test
            public void equalsTestIfEmpty() {
                assertTrue(new DatabaseTransactionDto().equals(new DatabaseTransactionDto()));
            }

            @Test
            public void equalsTestIfNull() {
                assertTrue(new DatabaseTransactionDto().equals(null));
            }

            @Test
            public void equalsTestIfNotDatabaseTransactionDto() {
                assertFalse(new DatabaseTransactionDto().equals("notDatabaseTransactionDto"));
            }

            @Test
            public void equalsTestIfDifferentDatabaseTransactionDto() {
                assertFalse(new DatabaseTransactionDto().equals(databaseTransactionDtoOther));
            }
        }
    }

    @Nested
    class isEmptyTests {
        @Test
        public void isEmptyTest() {
            assertFalse(databaseTransactionDto.isEmpty());
        }

        @Test
        public void isEmptyTestIfNoAmount() {
            databaseTransactionDto.setAmount(null);
            assertTrue(databaseTransactionDto.isEmpty());
        }

        @Test
        public void isEmptyTestIfNoSenderId() {
            databaseTransactionDto.setSenderId(null);
            assertTrue(databaseTransactionDto.isEmpty());
        }

        @Test
        public void isEmptyTestIfNoReceiverId() {
            databaseTransactionDto.setReceiverId(null);
            assertTrue(databaseTransactionDto.isEmpty());
        }

        @Test
        public void isEmptyTestIfEmpty() {
            assertTrue(new DatabaseTransactionDto().isEmpty());
        }
    }
}
