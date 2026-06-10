package dev.folomkin.currexchangejava.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class CbrResponse {


    @JsonProperty("Date")
    private String date;

    @JsonProperty("PreviousDate")
    private String previousDate;

    @JsonProperty("PreviousURL")
    private String previousUrl;

    @JsonProperty("Timestamp")
    private String timestamp;

    @JsonProperty("Valute")
    private Map<String, ValuteDetails> valute;


    public Map<String, ValuteDetails> getValute() {
        return valute;
    }

    public void setValute(Map<String, ValuteDetails> valute) {
        this.valute = valute;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
