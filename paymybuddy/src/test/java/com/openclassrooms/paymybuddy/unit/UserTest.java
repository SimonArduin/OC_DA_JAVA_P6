package com.openclassrooms.paymybuddy.unit;

import com.openclassrooms.paymybuddy.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = User.class)
public class UserTest {
    @Autowired
    User user;
    @Autowired
    User userOther;
    @BeforeEach
    private void setUp() {
        user = new User(1, 0.00,1,"email","iban","password",1,"username",new ArrayList<>());
        userOther = new User(2, 0.00,1,"emailOtherOther","ibanOther","passwordOther",1,"usernameOther",new ArrayList<>());
    }
    
    @Nested
    class addConnectionTests {
        @Test
        public void addConnectionTest() {
            assertTrue(user.addConnection(userOther));
        }
        @Test
        public void addConnectionTestIfEmpty() {
            assertFalse(user.addConnection(new User()));
        }
        @Test
        public void addConnectionTestIfNull() {
            assertFalse(user.addConnection(null));
        }
        @Test
        public void addConnectionTestIfAlreadyInConnections() {
            user.setConnections(new ArrayList<User>(Arrays.asList(userOther)));
            assertFalse(user.addConnection(userOther));
        }
        @Test
        public void addConnectionTestIfSame() {
            assertFalse(user.addConnection(user));
        }
    }
}
