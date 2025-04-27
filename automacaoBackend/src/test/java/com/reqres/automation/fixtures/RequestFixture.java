package com.reqres.automation.fixtures;

import com.reqres.automation.config.ApiConfig;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Classe responsável por criar e configurar as especificações de requisição
 * Implementa o padrão de projeto Fixture para isolamento dos testes
 */
@Component
public class RequestFixture {

    @Autowired
    private ApiConfig apiConfig;

    /**
     * Cria uma especificação de requisição base com configurações comuns
     * @return Especificação de requisição configurada
     */
    public RequestSpecification createBaseSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(apiConfig.getBaseUrl())
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
    }

    /**
     * Cria uma especificação de requisição autenticada com API key
     * @return Especificação de requisição com API key
     */
    public RequestSpecification createApiKeySpec() {
        return new RequestSpecBuilder()
                .setBaseUri(apiConfig.getBaseUrl())
                .setContentType(ContentType.JSON)
                .addHeader("X-API-KEY", apiConfig.getApiKey())
                .addFilter(new AllureRestAssured())
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
    }

    /**
     * Cria uma especificação de requisição com token JWT
     * @param token Token JWT de autenticação
     * @return Especificação de requisição com token JWT
     */
    public RequestSpecification createAuthSpec(String token) {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(apiConfig.getBaseUrl())
                .setContentType(ContentType.JSON)
                .addHeader("X-API-KEY", apiConfig.getApiKey())
                .addFilter(new AllureRestAssured())
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter());

        if (token != null && !token.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + token);
        }

        return builder.build();
    }
} 