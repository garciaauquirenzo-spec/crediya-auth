package co.com.pragma.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI crediyaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CrediYa Auth API")
                        .description("API para gestión de usuarios (registro, autenticación, etc.)")
                        .version("1.0.0"));
    }
}
