package com.openclassrooms.paymybuddy.unit;

import com.openclassrooms.paymybuddy.dto.DatabaseTransactionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = DatabaseTransactionDto.class)
public class DatabaseTransactionDtoTest {
    @Autowired
    DatabaseTransactionDto databaseTransactionDto;
    @Autowired
    DatabaseTransactionDto databaseTransactionDtoOther;
    @BeforeEach
    private void setUp() {
        databaseTransactionDto = new DatabaseTransactionDto(1, 10.0, 0.05*10.0, 1, "description", "iban", 1, 2, new Timestamp(0), false);
        databaseTransactionDtoOther = new DatabaseTransactionDto(2, 20.0, 0.05*10.0, 2, "descriptionOther", "ibanOther", 3, 4, new Timestamp(0), true);
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
