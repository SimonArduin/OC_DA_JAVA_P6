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
public class UserTest extends TestVariables {
    @BeforeEach
    private void setUp() {
        initializeVariables();
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
        public void addConnectionIfNoExistingConnectionsTest() {
            user.setConnections(null);
            assertTrue(user.addConnection(userOther));
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

    @Nested
    class equalsTests {

        @Nested
        class thisNotEmpty {
            @Test
            public void equalsTest() {
                assertTrue(user.equals(user));
            }

            @Test
            public void equalsTestIfEmpty() {
                assertFalse(user.equals(new User()));
            }

            @Test
            public void equalsTestIfNull() {
                assertFalse(user.equals(null));
            }

            @Test
            public void equalsTestIfNotUser() {
                assertFalse(user.equals("notUser"));
            }

            @Test
            public void equalsTestIfDifferentUser() {
                assertFalse(user.equals(userOther));
            }
            @Test
            public void equalsTestIfNoConnections() {
                user.setConnections(null);
                assertTrue(user.equals(user));
            }
            @Test
            public void equalsTestIfOtherNoConnections() {
                userOther = new User(userDto);
                userOther.setConnections(null);
                assertFalse(user.equals(userOther));
            }
        }

        @Nested
        class thisEmpty {

            @Test
            public void equalsTestIfEmpty() {
                assertTrue(new User().equals(new User()));
            }

            @Test
            public void equalsTestIfNull() {
                assertTrue(new User().equals(null));
            }

            @Test
            public void equalsTestIfNotUser() {
                assertFalse(new User().equals("notUser"));
            }

            @Test
            public void equalsTestIfDifferentUser() {
                assertFalse(new User().equals(userOther));
            }
        }
    }
    @Nested
    class isEmptyTests {
        @Test
        public void isEmptyTest() {
            assertFalse(user.isEmpty());
        }

        @Test
        public void isEmptyTestIfNoAmount() {
            user.setUsername(null);
            assertTrue(user.isEmpty());
        }

        @Test
        public void isEmptyTestIfNoSenderId() {
            user.setPassword(null);
            assertTrue(user.isEmpty());
        }

        @Test
        public void isEmptyTestIfNoReceiverId() {
            user.setEmail(null);
            assertTrue(user.isEmpty());
        }

        @Test
        public void isEmptyTestIfEmpty() {
            assertTrue(new User().isEmpty());
        }
    }
}
