package dev.folomkin.currexchangejava.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.folomkin.currexchangejava.domain.entity.CurrEntity;

import java.util.Map;

public record CbrResponse(
        @JsonProperty("Date")
        String date,

        @JsonProperty("PreviousDate")
        String previousDate,

        @JsonProperty("PreviousURL")
        String previousUrl,

        @JsonProperty("Timestamp")
        String timestamp,

        @JsonProperty("Valute")
        Map<String, CurrEntity> valute
) { }
