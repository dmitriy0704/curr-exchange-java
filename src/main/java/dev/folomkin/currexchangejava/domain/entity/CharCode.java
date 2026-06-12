package dev.folomkin.currexchangejava.domain.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum CharCode {
    DZD, GBP, USD, EUR;

    @JsonCreator
    public static CharCode fromString(String value) {
        try {
            return CharCode.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Возвращаем null, чтобы сработал наш обработчик HttpMessageNotReadableException
            return null;
        }
    }
}
