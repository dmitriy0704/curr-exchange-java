package dev.folomkin.currexchangejava.controller;


import dev.folomkin.currexchangejava.service.CurrService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CurrController {

    private final CurrService currService;

    public CurrController(CurrService currService) {
        this.currService = currService;
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/get-external")
    public void externalRequest() {
        currService.externalRequest();
    }
}
