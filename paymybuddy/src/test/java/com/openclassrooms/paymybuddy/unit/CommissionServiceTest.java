package com.openclassrooms.paymybuddy.unit;

import com.openclassrooms.paymybuddy.TestVariables;
import com.openclassrooms.paymybuddy.exception.CommissionNotFound;
import com.openclassrooms.paymybuddy.repository.CommissionRepository;
import com.openclassrooms.paymybuddy.service.CommissionService;
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

@SpringBootTest(classes = CommissionService.class)
public class CommissionServiceTest extends TestVariables {
    @Autowired
    CommissionService commissionService;

    @MockBean
    CommissionRepository commissionRepository;

    @BeforeEach
    private void setUp() {
        initializeVariables();

        when(commissionRepository.findById(any(Integer.class))).thenReturn(commission);
    }

    @Test
    void contextLoads() {}

    @Nested
    class findByIdTests {

        @Test
        public void findByIdTest() {
            assertEquals(commission, commissionService.findById(commission.getId()));
            verify(commissionRepository, Mockito.times(1)).findById(any(Integer.class));
        }

        @Test
        public void findByIdTestIfNotInDB() {
            when(commissionRepository.findById(any(Integer.class))).thenReturn(null);
            assertThrows(CommissionNotFound.class, () -> commissionService.findById(commission.getId()));
            verify(commissionRepository, Mockito.times(1)).findById(any(Integer.class));
        }

        @Test
        public void findByIdTestIfNull() {
            assertThrows(IllegalArgumentException.class, () -> commissionService.findById(null));
            verify(commissionRepository, Mockito.times(0)).findById(any(Integer.class));
        }
    }
}
