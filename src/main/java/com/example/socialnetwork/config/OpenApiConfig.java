package com.example.socialnetwork.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Swagger Social Network",
                version = "1.0.0",
                description = "OpenApi Documentation for social network project"
        ),
        servers = @Server(
//                description = "",
                url = "http://localhost:8080"
        ),
        externalDocs = @ExternalDocumentation(
                description = "Get more information from github",
                url = "https://github.com/Viancode/SocialNetwork.git"
        ),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "bearerAuth",
        description = "Authentication by JWT",
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
