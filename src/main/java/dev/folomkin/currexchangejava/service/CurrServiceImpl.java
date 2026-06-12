package dev.folomkin.currexchangejava.service;

import dev.folomkin.currexchangejava.domain.dto.Money;
import dev.folomkin.currexchangejava.domain.entity.CharCode;
import dev.folomkin.currexchangejava.domain.entity.CurrEntity;
import dev.folomkin.currexchangejava.repository.CurrRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
public class CurrServiceImpl implements CurrService {

    private final CurrRepo currRepo;

    public CurrServiceImpl(CurrRepo currRepo) {
        this.currRepo = currRepo;
    }


    @Override
    public String convert(Money money) {
        var curr = currRepo.findByCharCode(money.charCode().name().toUpperCase());

        BigDecimal amount = money.amount();

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Количество переводимых денег должно быть больше нуля");
        }

        BigDecimal course = curr.get().getValue();
        BigDecimal nominal = BigDecimal.valueOf(curr.get().getNominal());

        //-> Формула: (Количество * Курс) / Номинал
        BigDecimal res = amount.multiply(course)
                .divide(nominal, 2, RoundingMode.HALF_UP);
        return "Курс: " + money.amount() + " " + money.charCode() + "/" + res + " руб.";
    }
}
