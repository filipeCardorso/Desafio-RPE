package com.reqres.automation.assertions;

import io.restassured.response.Response;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe responsável por fornecer asserções específicas para autenticação
 * Implementa o princípio de Responsabilidade Única (S) do SOLID
 */
@Component
public class AuthAssertions {

    /**
     * Valida se a resposta contém um token de autenticação válido
     * @param response resposta da API
     */
    public void assertValidToken(Response response) {

        String token = response.jsonPath().getString("token");
        assertNotNull(token, "Token não pode ser nulo");
        assertFalse(token.isEmpty(), "Token não pode ser vazio");

    }
    
    /**
     * Valida se a resposta contém uma mensagem de erro apropriada
     * @param response resposta da API
     * @param expectedKeyword palavra-chave esperada na mensagem de erro (opcional)
     */
    public void assertErrorResponse(Response response, String expectedKeyword) {
        String errorMessage = response.jsonPath().getString("error");
        assertNotNull(errorMessage, "Mensagem de erro não pode ser nula");
        
        if (expectedKeyword != null && !expectedKeyword.isEmpty()) {
            assertTrue(errorMessage.toLowerCase().contains(expectedKeyword.toLowerCase()), 
                    "Mensagem de erro deve mencionar: " + expectedKeyword);
        }
    }
    
    /**
     * Valida código de status de erro para operações de autenticação
     * @param response resposta da API
     */
    public void assertAuthErrorStatus(Response response) {
        int statusCode = response.getStatusCode();
        assertTrue(statusCode == 400 || statusCode == 401 || statusCode == 403,
                "Código de status deveria ser 400, 401 ou 403, mas foi: " + statusCode);
    }
} 