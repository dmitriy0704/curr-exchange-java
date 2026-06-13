package dev.folomkin.currexchangejava.service;

import dev.folomkin.currexchangejava.domain.dto.Money;
import dev.folomkin.currexchangejava.domain.entity.CurrEntity;
import dev.folomkin.currexchangejava.integrations.Integration;
import dev.folomkin.currexchangejava.repository.CurrRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
public class CurrServiceImpl implements CurrService {

    private final CurrRepo currRepo;
    private final Integration integration;

    public CurrServiceImpl(CurrRepo currRepo, Integration integration) {
        this.currRepo = currRepo;
        this.integration = integration;
    }


    @Override
    public BigDecimal convert(Money money) {

        if (money == null || money.charCode() == null) {
            throw new IllegalArgumentException("Код валюты не может быть пустым");
        }

        BigDecimal amount = money.amount();

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма перевода должна быть больше нуля");
        }

        String moneyCharCode = money.charCode().name().toUpperCase();
        CurrEntity curr = currRepo
                .findByCharCode(moneyCharCode)
                .orElseGet(() -> {
                    log.info("Валюта есть в enum, но в БД еще не загружена");
                    integration.externalRequest();
                    return currRepo.findByCharCode(moneyCharCode).orElseThrow(() ->
                            new IllegalArgumentException("После загрузки курсов валюта не была найдена")
                    );
                });


        BigDecimal course = curr.getValue();
        BigDecimal nominal = BigDecimal.valueOf(curr.getNominal());

        //-> Формула: (Количество * Курс) / Номинал
        return amount.multiply(course)
                .divide(nominal, 2, RoundingMode.HALF_UP);
    }
}
