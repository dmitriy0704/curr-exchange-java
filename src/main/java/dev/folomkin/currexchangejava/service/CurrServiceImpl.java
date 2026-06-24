package dev.folomkin.currexchangejava.service;

import dev.folomkin.currexchangejava.domain.dto.Money;
import dev.folomkin.currexchangejava.domain.entity.CurrEntity;
import dev.folomkin.currexchangejava.integrations.Integration;
import dev.folomkin.currexchangejava.repository.CurrRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

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
    public List<CurrEntity> saveValute() {
        List<CurrEntity> currEntities = integration.externalRequest();
        currRepo.saveAll(currEntities);
        return currEntities;
    }

    @Override
    public BigDecimal convert(Money money) {

        // Валидация входного объекта:
        validateMoney(money);

        // Валюта получается из ЦБ и сохраняется в другом методе,
        // в этом приходит валюта уже из БД.
        CurrEntity curr = getCurrencyByCharCode(money);

        BigDecimal course = curr.getValue();
        BigDecimal nominal = BigDecimal.valueOf(curr.getNominal());

        //-> Формула: (Количество * Курс) / Номинал
        return money.amount().multiply(course)
                .divide(nominal, 2, RoundingMode.HALF_UP);
    }

    private void validateMoney(Money money) {
        if (money == null) {
            throw new IllegalArgumentException("Объект Money не может быть null");
        }
        if (money.charCode() == null || money.charCode().name().isBlank()) {
            throw new IllegalArgumentException("Код валюты не может быть пустым");
        }
        if (money.amount() == null || money.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма перевода должна быть больше нуля");
        }
    }

    private CurrEntity getCurrencyByCharCode(Money money) {
        String moneyCharCode = money.charCode().name().toUpperCase();
        return currRepo
                .findByCharCode(moneyCharCode)
                .orElseGet(() -> {
                    log.info("Валюта есть в enum, но в БД еще не загружена");
                    integration.externalRequest();
                    return currRepo.findByCharCode(moneyCharCode).orElseThrow(() ->
                            new IllegalArgumentException("После загрузки курсов валюта не была найдена")
                    );
                });
    }


}


//TODO: Разделить на части, чтобы можно было написать юнит тесты только на математику
//TODO: Написать интеграционный тест

