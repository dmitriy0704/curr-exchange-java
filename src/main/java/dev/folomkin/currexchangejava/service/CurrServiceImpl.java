package dev.folomkin.currexchangejava.service;

import dev.folomkin.currexchangejava.model.dto.CbrResponse;
import dev.folomkin.currexchangejava.model.dto.ValuteDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CurrServiceImpl implements CurrService {

    private final RestTemplate restTemplate;

    public CurrServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final String EXTERNAL_URL =
            "https://www.cbr-xml-daily.ru/daily_json.js";

    public void externalRequest() {
        CbrResponse response = restTemplate.getForObject(EXTERNAL_URL, CbrResponse.class);

        if(response != null && response.getValute() != null) {
            Map<String, ValuteDetails> rates = response.getValute();

            ValuteDetails aud = rates.get("AUD");
            if (aud != null) {
                System.out.println("Курс " + aud.getName() + ": " + aud.getValue() + " руб.");
            }

        }
    }
}
