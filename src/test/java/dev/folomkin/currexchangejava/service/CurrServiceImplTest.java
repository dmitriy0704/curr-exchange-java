package dev.folomkin.currexchangejava.service;

import dev.folomkin.currexchangejava.domain.dto.Money;
import dev.folomkin.currexchangejava.domain.entity.CharCode;
import dev.folomkin.currexchangejava.domain.entity.CurrEntity;
import dev.folomkin.currexchangejava.repository.CurrRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CurrServiceImplTest {

    @Mock
    CurrRepo currRepo;

    @InjectMocks
    CurrServiceImpl currServiceImpl;

    //-> Тестируем конвертацию с валидными полями
    @Test
    void shouldReturnCorrectAmount() {
        Money validMoney = new Money(CharCode.EUR, BigDecimal.valueOf(2));
        CurrEntity currEntity = new CurrEntity();
        currEntity.setValue(BigDecimal.valueOf(82.97));
        currEntity.setNominal(1);
        when(currRepo.findByCharCode("EUR")).thenReturn(Optional.of(currEntity));
        BigDecimal res = currServiceImpl.convert(validMoney);
        assertThat(res).isEqualTo(BigDecimal.valueOf(165.94));
    }


    //-> null вместо объекта Money
    @Test
    void convert_WhenMoneyIsNull_ShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> currServiceImpl.convert(null) // Передаем null
        );
        assertEquals("Объект Money не может быть null", exception.getMessage());
        //-> до базы данных код не дошел
        verifyNoInteractions(currRepo);
    }


    //-> код валюты равен null
    @Test
    void convert_WhenCharCodeIsNull_ShouldThrowException() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> currServiceImpl.convert(new Money(null, BigDecimal.valueOf(100))) // Передаем null
        );

        assertEquals("Код валюты не может быть пустым", exception.getMessage());
        verifyNoInteractions(currRepo);
    }


    //-> сумма перевода меньше либо равна 0
    @Test
    void shouldReturnThrowException_WhenAmountIsZero() {
        Money invalidMoney = new Money(CharCode.EUR, BigDecimal.ZERO);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> currServiceImpl.convert(invalidMoney)
        );

        assertEquals("Сумма должна быть больше или равна нулю", exception.getMessage());
        verifyNoInteractions(currRepo);
    }


    //-> номинал валюты больше 1
    @Test
    void shouldCalculateCorrectly_WhenNominalIsGreaterThanOne() {
        //-> 100 Алжирских динаров = 53.93 р.
        //-> Считаем за 10 единиц
        //-> Формула: (Количество * Курс) / Номинал
        Money money = new Money(CharCode.DZD, BigDecimal.valueOf(10));

        CurrEntity currEntity = new CurrEntity();
        currEntity.setCharCode("DZD");
        currEntity.setValue(BigDecimal.valueOf(53.93));
        currEntity.setNominal(100);

        when(currRepo.findByCharCode("DZD")).thenReturn(Optional.of(currEntity));

        BigDecimal amount = currServiceImpl.convert(money);
        assertThat(amount).isEqualTo(BigDecimal.valueOf(5.39));
    }
}