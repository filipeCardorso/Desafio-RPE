package com.reqres.automation.validators;

import io.restassured.response.Response;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * Implementação da interface ResponseValidator para validações REST
 * Implementa o princípio de Responsabilidade Única (S) do SOLID
 */
@Component
public class RestResponseValidator implements ResponseValidator {
    
    @Override
    public void validateResponse(int expectedCode, Consumer<Response> responseConsumer, Response response) {
        try {
            // Valida o status code manualmente
            validateStatusCode(expectedCode, response);
            
            // Executa as validações adicionais
            if (responseConsumer != null) {
                responseConsumer.accept(response);
            }
        } catch (Exception e) {
            // Log e rethrow para manter o comportamento original
            System.err.println("Erro ao validar resposta: " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public void validateStatusCode(int expectedCode, Response response) {
        if (expectedCode != response.getStatusCode()) {
            throw new AssertionError("O status code da resposta não é o esperado. Esperado: " + 
                expectedCode + ", Atual: " + response.getStatusCode());
        }
    }
    
    @Override
    public void validateFieldExists(Response response, String field) {
        if (response.jsonPath().get(field) == null) {
            throw new AssertionError("O campo '" + field + "' não foi encontrado na resposta");
        }
    }
    
    @Override
    public void validateJsonContent(Response response) {
        try {
            // Tenta parsear o corpo como JSON
            response.then().extract().jsonPath();
        } catch (Exception e) {
            throw new AssertionError("A resposta não contém conteúdo JSON válido: " + e.getMessage());
        }
    }
} 