package dev.folomkin.currexchangejava.controller;


import dev.folomkin.currexchangejava.domain.dto.Money;
import dev.folomkin.currexchangejava.domain.entity.CurrEntity;
import dev.folomkin.currexchangejava.service.CurrService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CurrController {

    private final CurrService currService;

    public CurrController(CurrService currService) {
        this.currService = currService;
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }


    @GetMapping("/valute-list")
    public List<CurrEntity> externalRequest() {
        return currService.externalRequest();
    }


    @PostMapping("/convert")
    public String convert(@RequestBody Money money) {
        return currService.convert(money);
    }
}
