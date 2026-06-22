package dev.folomkin.currexchangejava.service;


import dev.folomkin.currexchangejava.domain.dto.Money;
import dev.folomkin.currexchangejava.domain.entity.CharCode;
import dev.folomkin.currexchangejava.domain.entity.CurrEntity;
import dev.folomkin.currexchangejava.repository.CurrRepo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class CurrServiceIT {

    @Autowired
    private CurrService currService;

    @Autowired
    private CurrRepo currRepo;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        currRepo.deleteAll();

        CurrEntity currEntity = new CurrEntity();
        currEntity.setCharCode("USD");
        currEntity.setValue(new BigDecimal("100.00"));
        currEntity.setNominal(1);
        currRepo.save(currEntity);
    }

    //-> Проверяется ситуация, когда оба поля Money валидны
    @Test
    public void convertValuteWithValidFieldsInMoney() {
        Money validMoney = new Money(CharCode.USD, new BigDecimal("200.00"));
        BigDecimal result = currService.convert(validMoney);
        assertNotNull(result);
        assertEquals(new BigDecimal("20000.00"), result);
    }


    //-> Проверяется ситуация, когда сумма конвертируемой валюты равна нулю
    @Test
    public void convertValuteWithInvalidAmountInMoney() {
        Money invalidMoney = new Money(CharCode.USD, BigDecimal.ZERO);
        assertThrows(IllegalArgumentException.class,
                () -> currService.convert(invalidMoney)
        );
    }

    //-> Проверяется ситуация, когда в Money charCode передается некорректный код валюты
    @Test
    public void convertValuteWithInvalidCharCodeInMoney() throws Exception {
        String invalidJson = """
                {
                    "charCode": "INVALID_CHAR_CODE",
                    "amount": "100.00"
                }
                """;
        mockMvc.perform(post("/api/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

    }
}
