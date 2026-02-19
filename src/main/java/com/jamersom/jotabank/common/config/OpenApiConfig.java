package com.jamersom.jotabank.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI jotabankOpenAPI() {

        var bearerScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        return new OpenAPI()
                .info(new Info()
                        .title("JotaBank API")
                        .version("v1")
                        .description("Fictional banking API using Jotacoin (JTC). JWT auth, accounts, transfers and statements.")
                )
                .schemaRequirement("bearerAuth", bearerScheme)
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
