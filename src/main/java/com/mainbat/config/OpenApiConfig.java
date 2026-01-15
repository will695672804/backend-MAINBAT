package com.mainbat.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "MAINBAT API",
        version = "1.0.0",
        description = "API REST pour la gestion de maintenance des bâtiments (GMAO)",
        contact = @Contact(
            name = "MAINBAT Support",
            email = "support@mainbat.com"
        ),
        license = @License(
            name = "Propriétaire",
            url = "https://mainbat.com"
        )
    ),
    servers = {
        @Server(url = "/api", description = "Serveur local")
    }
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "Enter JWT token"
)
public class OpenApiConfig {
}
