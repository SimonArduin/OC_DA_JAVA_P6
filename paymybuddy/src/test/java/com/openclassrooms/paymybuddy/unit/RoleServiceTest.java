package com.openclassrooms.paymybuddy.unit;

import com.openclassrooms.paymybuddy.entity.Role;
import com.openclassrooms.paymybuddy.repository.RoleRepository;
import com.openclassrooms.paymybuddy.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = RoleService.class)
public class RoleServiceTest {
    @Autowired
    RoleService roleService;

    @MockBean
    RoleRepository roleRepository;

    final Role role = new Role(1, "role");

    @BeforeEach
    private void setUp() {
        when(roleRepository.findById(any(Integer.class))).thenReturn(role);
    }

    @Test
    void contextLoads() {}

    @Nested
    class findByIdTests {

        @Test
        public void findByIdTest() {
            assertEquals(role, roleService.findById(role.getId()));
            verify(roleRepository, Mockito.times(1)).findById(any(Integer.class));
        }

        @Test
        public void findByIdTestIfNotInDB() {
            when(roleRepository.findById(any(Integer.class))).thenReturn(null);
            assertEquals(null, roleService.findById(role.getId()));
            verify(roleRepository, Mockito.times(1)).findById(any(Integer.class));
        }

        @Test
        public void findByIdTestIfNull() {
            assertThrows(IllegalArgumentException.class, () -> roleService.findById(null));
            verify(roleRepository, Mockito.times(0)).findById(any(Integer.class));
        }
    }
}
