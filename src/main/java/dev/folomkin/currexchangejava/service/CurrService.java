package dev.folomkin.currexchangejava.service;

import dev.folomkin.currexchangejava.domain.dto.Money;
import dev.folomkin.currexchangejava.domain.entity.CurrEntity;

import java.math.BigDecimal;
import java.util.List;

public interface CurrService {

    List<CurrEntity> externalRequest();

    String convert(Money money);
}
