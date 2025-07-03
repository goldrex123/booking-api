package sky.gurich.booking.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Booking App Api Document")
                .version("v0.1")
                .description("예약 앱 API 명세서");

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
