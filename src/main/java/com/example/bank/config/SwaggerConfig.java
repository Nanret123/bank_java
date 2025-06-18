package com.example.bank.config;

import java.util.Arrays;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration

public class SwaggerConfig {
  @Bean
  public OpenAPI corebankingOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Core Banking System API 🏦")
            .description(
                "RESTful API for Core Banking Operations including Account Management, Transactions, and User Administration")
            .version("v1.0")
            .contact(new Contact()
                .name("Banking IT Team")
                .email("it-support@bank.com")
                .url("https://bank.com/support")))
        .externalDocs(new ExternalDocumentation()
            .description("Banking System Documentation")
            .url("https://docs.bank.com"))
        .servers(Arrays.asList(
            new Server().url("http://localhost:8080")
                .description("Development Server"),
            new Server().url("https://api-staging.bank.com")
                .description("Staging Server"),
            new Server().url("https://api.bank.com")
                .description("Production Server")))
        .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
        .components(new Components()
            .addSecuritySchemes("Bearer Authentication", createBearerScheme())
            .addSchemas("ApiResponse", createApiResponseSchema())
            .addSchemas("ErrorResponse", createErrorResponseSchema()));
  }

  private SecurityScheme createBearerScheme() {
    return new SecurityScheme()
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT")
        .description("Enter JWT Bearer token in the format: Bearer {token}");
  }

  private Schema<?> createApiResponseSchema() {
    return new Schema<>()
        .type("object")
        .addProperty("success",
            new Schema<>().type("boolean").description("Operation success status"))
        .addProperty("message", new Schema<>().type("string").description("Response message"))
        .addProperty("data", new Schema<>().description("Response data"))
        .addProperty("timestamp", new Schema<>().type("string").format("date-time")
            .description("Response timestamp"));
  }

  private Schema<?> createErrorResponseSchema() {
    return new Schema<>()
        .type("object")
        .addProperty("timestamp", new Schema<>().type("string").format("date-time"))
        .addProperty("status", new Schema<>().type("integer"))
        .addProperty("error", new Schema<>().type("string"))
        .addProperty("message", new Schema<>().type("string"))
        .addProperty("path", new Schema<>().type("string"));
  }

  @Bean
  public GroupedOpenApi authenticationApi() {
    return GroupedOpenApi.builder()
        .group("01-authentication")
        .displayName("Authentication & User Management")
        .pathsToMatch("/api/auth/**", "/api/users/**")
        .build();
  }

  @Bean
  public GroupedOpenApi customerApi() {
    return GroupedOpenApi.builder()
        .group("02-customers")
        .displayName("Customer Management")
        .pathsToMatch("/api/customers/**")
        .build();
  }

  @Bean
  public GroupedOpenApi accountApi() {
    return GroupedOpenApi.builder()
        .group("03-accounts")
        .displayName("Account Management")
        .pathsToMatch("/api/accounts/**")
        .build();
  }

  @Bean
  public GroupedOpenApi transactionApi() {
    return GroupedOpenApi.builder()
        .group("04-transactions")
        .displayName("Transaction Processing")
        .pathsToMatch("/api/transactions/**")
        .build();
  }

  @Bean
  public GroupedOpenApi ledgerApi() {
    return GroupedOpenApi.builder()
        .group("05-ledger")
        .displayName("Ledger & Accounting")
        .pathsToMatch("/api/ledger/**")
        .build();
  }

  @Bean
  public GroupedOpenApi reportingApi() {
    return GroupedOpenApi.builder()
        .group("06-reporting")
        .displayName("Reports & Audit")
        .pathsToMatch("/api/reports/**", "/api/audit/**")
        .build();
  }

  @Bean
  public GroupedOpenApi branchApi() {
    return GroupedOpenApi.builder()
        .group("07-branches")
        .displayName("Branch Management")
        .pathsToMatch("/api/branches/**")
        .build();
  }

  @Bean
  public GroupedOpenApi adminApi() {
    return GroupedOpenApi.builder()
        .group("08-admin")
        .displayName("System Administration")
        .pathsToMatch("/api/admin/**")
        .build();
  }
}
