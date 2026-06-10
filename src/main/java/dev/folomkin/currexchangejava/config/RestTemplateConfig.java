package dev.folomkin.currexchangejava.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder
                .connectTimeout(Duration.ofSeconds(5))
                .readTimeout(Duration.ofSeconds(5))
                .build();

        restTemplate.getMessageConverters().stream()
                .filter(MappingJackson2HttpMessageConverter.class::isInstance)
                .map(MappingJackson2HttpMessageConverter.class::cast)
                .findFirst()
                .ifPresent(converter -> {
                    List<MediaType> supportedMediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());
                    // Добавляем проблемный content-type в список разрешенных для парсинга
                    supportedMediaTypes.add(new MediaType("application", "javascript"));
                    converter.setSupportedMediaTypes(supportedMediaTypes);
                });

        return restTemplate;
    }

}
