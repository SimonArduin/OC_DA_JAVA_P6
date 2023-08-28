package com.openclassrooms.paymybuddy.unit;

import com.openclassrooms.paymybuddy.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = UserDto.class)
public class UserDtoTest {
    @Autowired
    UserDto userDto;
    @Autowired
    UserDto userDtoOther;
    @BeforeEach
    private void setUp() {
        userDto = new UserDto(1, 0.00,1,"email","iban","password",1,"username",new ArrayList<>());
        userDtoOther = new UserDto(2, 0.00,1,"emailOtherOther","ibanOther","passwordOther",1,"usernameOther",new ArrayList<>());
    }
    
    @Nested
    class addConnectionTests {
        @Test
        public void addConnectionTest() {
            assertTrue(userDto.addConnection(userDtoOther));
        }
        @Test
        public void addConnectionTestIfEmpty() {
            assertFalse(userDto.addConnection(new UserDto()));
        }
        @Test
        public void addConnectionTestIfNull() {
            assertFalse(userDto.addConnection(null));
        }
        @Test
        public void addConnectionTestIfAlreadyInConnections() {
            userDto.setConnections(new ArrayList<UserDto>(Arrays.asList(userDtoOther)));
            assertFalse(userDto.addConnection(userDtoOther));
        }
        @Test
        public void addConnectionTestIfSame() {
            assertFalse(userDto.addConnection(userDto));
        }
    }

    @Nested
    class equalsTests {

        @Nested
        class thisNotEmpty {
            @Test
            public void equalsTest() {
                assertTrue(userDto.equals(userDto));
            }

            @Test
            public void equalsTestIfEmpty() {
                assertFalse(userDto.equals(new UserDto()));
            }

            @Test
            public void equalsTestIfNull() {
                assertFalse(userDto.equals(null));
            }

            @Test
            public void equalsTestIfNotUserDto() {
                assertFalse(userDto.equals("notUserDto"));
            }

            @Test
            public void equalsTestIfDifferentUserDto() {
                assertFalse(userDto.equals(userDtoOther));
            }
        }

        @Nested
        class thisEmpty {

            @Test
            public void equalsTestIfEmpty() {
                assertTrue(new UserDto().equals(new UserDto()));
            }

            @Test
            public void equalsTestIfNull() {
                assertTrue(new UserDto().equals(null));
            }

            @Test
            public void equalsTestIfNotUserDto() {
                assertFalse(new UserDto().equals("notUserDto"));
            }

            @Test
            public void equalsTestIfDifferentUserDto() {
                assertFalse(new UserDto().equals(userDtoOther));
            }
        }
    }
}
