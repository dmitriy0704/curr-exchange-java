package dev.folomkin.currexchangejava.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("Обмен валют")
                        .description("Rest API")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Dmitriy Folomkin")
                                .email("dmitriy.folomkin@yandex.ru").url("www.folomkin.dev"))
                        .license(new License().name("License of API")
                                .url("API license URL")));
    }
}
