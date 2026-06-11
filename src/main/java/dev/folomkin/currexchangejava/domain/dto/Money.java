package dev.folomkin.currexchangejava.domain.dto;

import dev.folomkin.currexchangejava.domain.entity.CharCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;


@Schema(description = "Форма конвертации валюты в рубли")
public record Money(

        @Schema(description = "Код валюты")
        @Size(min = 3, max = 3, message = "Код валюты должен состоять из 3 символов")
        @NotNull(message = "Код валюты обязателен")
        String charCode,

        @Schema(description = "Сумма перевода")
        @NotNull(message = "Сумма обязательна")
        BigDecimal amount) {
}
