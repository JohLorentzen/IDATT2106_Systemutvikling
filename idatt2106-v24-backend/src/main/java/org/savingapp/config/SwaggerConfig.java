package org.savingapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Configuration class for Swagger.
 */
@Configuration
public class SwaggerConfig {


    /**
     * This method is used to create a GroupedOpenApi bean that is used for grouping API endpoints in the Swagger UI.
     *
     * @return A GroupedOpenApi object that groups all API endpoints under the name "public-apis".
     */
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-apis")
                .pathsToMatch("/**")
                .build();
    }


    /**
     * This method is used to create an OpenAPI object that is used for configuring the Swagger UI.
     *
     * @return An OpenAPI object that configures the Swagger UI with the APIs information and security requirements.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("API for Sparesti").version("1.0")
                        .description("This is a REST API for Sparesti, a personal savings application."))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
