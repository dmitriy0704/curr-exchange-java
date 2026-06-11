package dev.folomkin.currexchangejava.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;


@Entity
@Table(name = "currency")
public class CurrEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonProperty("ID")
    private String curr_id;

    @JsonProperty("NumCode")
    private String numCode;

    @JsonProperty("CharCode")
    private String charCode;

    @JsonProperty("Nominal")
    private int nominal;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Value")
    private BigDecimal value;

    @JsonProperty("Previous")
    private double previous;


    private OffsetDateTime lastUpdated;

    public OffsetDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(OffsetDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }


    public String getName() {
        return name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public int getNominal() {
        return nominal;
    }

    public String getCharCode() {
        return charCode;
    }

}
