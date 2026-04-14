package example.quiz_app_Monolith.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server()
                        .url("http://localhost:8080")
                        .description("Local Development Server"))
                .addServersItem(new Server()
                        .url("https://quizapp.example.com")
                        .description("Production Server"))
                .info(new Info()
                        .title("Quiz Application API")
                        .description("API documentation for Quiz Application")
                        .version("1.0.0"));
    }
}

