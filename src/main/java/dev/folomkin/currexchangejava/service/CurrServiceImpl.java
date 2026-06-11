package dev.folomkin.currexchangejava.service;

import dev.folomkin.currexchangejava.domain.dto.CbrResponse;
import dev.folomkin.currexchangejava.domain.dto.Money;
import dev.folomkin.currexchangejava.domain.entity.CharCode;
import dev.folomkin.currexchangejava.domain.entity.CurrEntity;
import dev.folomkin.currexchangejava.repository.CurrRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CurrServiceImpl implements CurrService {

    private final RestTemplate restTemplate;
    private final CurrRepo currRepo;

    public CurrServiceImpl(RestTemplate restTemplate,
                           CurrRepo currRepo) {
        this.restTemplate = restTemplate;
        this.currRepo = currRepo;
    }

    private static final String EXTERNAL_URL =
            "https://www.cbr-xml-daily.ru/daily_json.js";


    //-> Загрузить список валют в БД
    @Transactional
    @Override
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
                    //-> При каждом новом запросе не дописываем валюты,
                    // а обновляем, если такие есть в БД
                    currRepo.findByCharCode(c.name()).ifPresent(
                            existingCurr -> {
                                curr.setId(existingCurr.getId());
                            }
                    );


                    LocalDateTime ldt = LocalDateTime
                            .parse(response.timestamp(),
                                    DateTimeFormatter.ISO_OFFSET_DATE_TIME);

                    curr.setResponseTimestamp(ldt);
                    valuteList.add(curr);
                    currRepo.save(curr);
                }
            }
        }
        return valuteList;
    }


    @Override
    public String convert(Money money) {
        CurrEntity curr = currRepo.findByCharCode(money.charCode().toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Валюта '" + money.charCode() + "' не найдена. Проверьте правильность кода."
                ));

        System.out.println("Last Update: " + curr.getLastUpdated());
        BigDecimal amount = money.amount();
        BigDecimal course = curr.getValue();
        BigDecimal nominal = BigDecimal.valueOf(curr.getNominal());

        //-> Формула: (Количество * Курс) / Номинал
        BigDecimal res = amount.multiply(course)
                .divide(nominal, 2, RoundingMode.HALF_UP);
        return "Курс: " + money.amount() + " " + money.charCode() + "/" + res + " руб.";
    }
}
