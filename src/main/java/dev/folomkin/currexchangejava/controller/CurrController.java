package dev.folomkin.currexchangejava.controller;


import dev.folomkin.currexchangejava.domain.dto.Money;
import dev.folomkin.currexchangejava.domain.entity.CurrEntity;
import dev.folomkin.currexchangejava.integrations.Integration;
import dev.folomkin.currexchangejava.service.CurrService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Конвертация валют", description = "")
public class CurrController {

    private final CurrService currService;
    private final Integration integration;

    public CurrController(CurrService currService,
                          Integration integration) {
        this.currService = currService;
        this.integration = integration;
    }


    @Operation(summary = "Получение списка валют от ЦБ")
    @GetMapping("/get-valutes")
    public List<CurrEntity> externalRequest() {
        return integration.externalRequest();
    }


    @Operation(
            summary = "Конвертация валют",
            description = "Необходимо указать код валюты и количество единиц валюты"
    )
    @PostMapping("/convert")
    public BigDecimal convert(@Valid @RequestBody Money money) {
        return currService.convert(money);
    }
}
