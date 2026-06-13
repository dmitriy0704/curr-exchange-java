package dev.folomkin.currexchangejava.domain.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum CharCode {
    DZD, GBP, USD, EUR;

    //-> Этот код добавляет возможность вводить код валюты в нижнем регистре.
    @JsonCreator
    public static CharCode fromString(String value) {
        return CharCode.valueOf(value.toUpperCase());
    }
}
