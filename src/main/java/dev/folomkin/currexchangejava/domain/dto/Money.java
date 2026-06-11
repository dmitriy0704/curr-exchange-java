package dev.folomkin.currexchangejava.domain.dto;

import dev.folomkin.currexchangejava.domain.entity.CharCode;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record Money(
        @NotNull(message = "Код валюты обязателен")
        String charCode,

        @NotNull(message = "Сумма обязательна")
        BigDecimal amount) {
}
