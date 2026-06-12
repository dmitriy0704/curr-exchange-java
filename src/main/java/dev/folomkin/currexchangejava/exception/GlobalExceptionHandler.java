package dev.folomkin.currexchangejava.exception;

import dev.folomkin.currexchangejava.domain.entity.CharCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleInvalidEnum(HttpMessageNotReadableException ex) {

        String message = ex.getMessage();

        //-> Некорректно введен код валюты
        if (ex.getMessage() != null && ex.getMessage().contains("CharCode")) {
            String allowedCurrencies = Arrays.toString(CharCode.values());
            message = "Указан неверный код валюты. Доступные варианты: " + allowedCurrencies;
            //-> Количество
        } else if (ex.getMessage() != null && ex.getMessage().contains("Unrecognized token")) {
            message = "Поле amount должно быть числом.";
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handlePositiveMoney(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }
}
