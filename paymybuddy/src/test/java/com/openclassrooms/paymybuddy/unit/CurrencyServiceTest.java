package com.openclassrooms.paymybuddy.unit;

import com.openclassrooms.paymybuddy.entity.Currency;
import com.openclassrooms.paymybuddy.repository.CurrencyRepository;
import com.openclassrooms.paymybuddy.service.CurrencyService;
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

@SpringBootTest(classes = CurrencyService.class)
public class CurrencyServiceTest extends TestVariables {
    @Autowired
    CurrencyService currencyService;

    @MockBean
    CurrencyRepository currencyRepository;

    @BeforeEach
    private void setUp() {
        initializeVariables();
        when(currencyRepository.findById(any(Integer.class))).thenReturn(currency);
    }

    @Test
    void contextLoads() {}

    @Nested
    class findByIdTests {

        @Test
        public void findByIdTest() {
            assertEquals(currency, currencyService.findById(currency.getId()));
            verify(currencyRepository, Mockito.times(1)).findById(any(Integer.class));
        }

        @Test
        public void findByIdTestIfNotInDB() {
            when(currencyRepository.findById(any(Integer.class))).thenReturn(null);
            assertEquals(null, currencyService.findById(currency.getId()));
            verify(currencyRepository, Mockito.times(1)).findById(any(Integer.class));
        }

        @Test
        public void findByIdTestIfNull() {
            assertThrows(IllegalArgumentException.class, () -> currencyService.findById(null));
            verify(currencyRepository, Mockito.times(0)).findById(any(Integer.class));
        }
    }
}
