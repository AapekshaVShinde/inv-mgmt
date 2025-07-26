package com.inventory.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Rate Limiting & Authentication Service")
						.description("API documentation with JWT and API Key security")
				)
				.components(new Components()
						// 1️⃣ JWT Bearer Authentication
						.addSecuritySchemes("bearerAuth",
								new SecurityScheme()
										.type(SecurityScheme.Type.HTTP)
										.scheme("bearer")
										.bearerFormat("JWT")
										.description("JWT Bearer Token Authentication")))
						// 2️⃣ API Key Authentication (For Rate Limiting)
				.addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
	}
}
