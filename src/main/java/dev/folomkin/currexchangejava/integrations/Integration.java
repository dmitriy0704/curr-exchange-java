package dev.folomkin.currexchangejava.integrations;

import dev.folomkin.currexchangejava.domain.dto.CbrResponse;
import dev.folomkin.currexchangejava.domain.entity.CharCode;
import dev.folomkin.currexchangejava.domain.entity.CurrEntity;
import dev.folomkin.currexchangejava.repository.CurrRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class Integration {

    private final RestTemplate restTemplate;
    private final CurrRepo currRepo;


    public Integration(RestTemplate restTemplate, CurrRepo currRepo) {
        this.restTemplate = restTemplate;
        this.currRepo = currRepo;
    }


    private static final String EXTERNAL_URL =
            "https://www.cbr-xml-daily.ru/daily_json.js";


    //-> Загрузить список валют в БД
    public List<CurrEntity> externalRequest() {
        CbrResponse response = restTemplate.getForObject(
                EXTERNAL_URL, CbrResponse.class
        );
        List<CurrEntity> valuteList = new ArrayList<>();
        if (response != null && response.valute() != null) {
            //-> Из ответа получаем список валют

            Map<String, CurrEntity> rates = response.valute();
            //-> Получаем только те валюты, CharCode которых указан в enum

            for (CharCode c : CharCode.values()) {
                CurrEntity curr = rates.get(c.toString());
                if (curr != null) {
                    LocalDateTime ldt = LocalDateTime
                            .parse(response.timestamp(),
                                    DateTimeFormatter.ISO_OFFSET_DATE_TIME);

                    curr.setResponseTimestamp(ldt);
                    valuteList.add(curr);
                }
            }
        }
        return valuteList;
    }


    @Transactional
    void save(CurrEntity curr) {
        currRepo.save(curr);
    }
}
