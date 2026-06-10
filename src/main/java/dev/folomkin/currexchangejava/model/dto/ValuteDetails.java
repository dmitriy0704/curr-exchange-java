package dev.folomkin.currexchangejava.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ValuteDetails {

    @JsonProperty("ID")
    private String id;

    @JsonProperty("NumCode")
    private String numCode;

    @JsonProperty("CharCode")
    private String charCode;

    @JsonProperty("Nominal")
    private int nominal;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Value")
    private double value;

    @JsonProperty("Previous")
    private double previous;

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public int getNominal() {
        return nominal;
    }

    public String getCharCode() {
        return charCode;
    }

}
