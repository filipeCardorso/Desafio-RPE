package com.reqres.automation.validators;

import io.restassured.response.Response;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Validador especializado para respostas de erro da API
 * 
 * @implNote Implementa o princípio de Responsabilidade Única (SRP) do SOLID
 * @implNote Segue o padrão de design Strategy para diferentes estratégias de validação de erro
 * @implSpec Provê métodos específicos para cada tipo de erro HTTP comum, facilitando a 
 * validação detalhada de payloads de erro em testes automatizados
 * @see ErrorType para os tipos de erro suportados
 */
@Component
public class ApiErrorValidator {
    
    /**
     * Valida o formato e estrutura básica de uma resposta de erro HTTP
     * 
     * @param response Objeto Response do RestAssured contendo a resposta HTTP
     * @throws AssertionError se a resposta não atender aos critérios mínimos de uma resposta de erro
     * @implSpec Verifica: código de status ≥ 400, corpo não-nulo, JSON válido
     */
    public void validateErrorResponseFormat(Response response) {
        assertNotNull(response, "A resposta não pode ser nula");
        assertTrue(response.getStatusCode() >= 400, 
                "O código de status deve ser um erro (>=400), mas foi: " + response.getStatusCode());
        
        String responseBody = response.getBody().asString();
        assertNotNull(responseBody, "O corpo da resposta não pode ser nulo");
        assertFalse(responseBody.isEmpty(), "O corpo da resposta não pode ser vazio");
        
        try {
            response.then().extract().jsonPath();
        } catch (Exception e) {
            fail("A resposta não é um JSON válido: " + e.getMessage());
        }
    }
    
    /**
     * Valida respostas de erro de autenticação/autorização (401/403)
     * 
     * @param response Objeto Response do RestAssured
     * @throws AssertionError se a resposta não atender aos critérios de uma resposta de erro de autenticação
     * @implSpec Verifica códigos HTTP adequados (401/403) e indicadores semânticos nos payloads
     */
    public void validateAuthenticationErrorResponse(Response response) {
        int statusCode = response.getStatusCode();
        assertTrue(statusCode == 401 || statusCode == 403 || statusCode == 400,
                "O código de status deve ser 401, 403 ou 400, mas foi: " + statusCode);
        
        String responseBody = response.getBody().asString().toLowerCase();
        boolean hasErrorIndicator = responseBody.contains("error") ||
                                    responseBody.contains("unauthorized") ||
                                    responseBody.contains("forbidden") ||
                                    responseBody.contains("invalid") ||
                                    responseBody.contains("authentication");
        
        assertTrue(hasErrorIndicator, "A resposta deve conter uma indicação de erro de autenticação");
    }
    
    /**
     * Valida respostas de erro de validação de dados de entrada (400 Bad Request)
     * 
     * @param response Objeto Response do RestAssured
     * @throws AssertionError se a resposta não atender aos critérios de uma resposta de erro de validação
     * @implSpec Verifica código HTTP 400 e indicadores semânticos de erro de validação no payload
     */
    public void validateDataValidationErrorResponse(Response response) {
        assertEquals(400, response.getStatusCode(),
                "O código de status deve ser 400 (Bad Request), mas foi: " + response.getStatusCode());
        
        String responseBody = response.getBody().asString().toLowerCase();
        boolean hasValidationIndicator = responseBody.contains("error") ||
                                        responseBody.contains("invalid") ||
                                        responseBody.contains("validation") ||
                                        responseBody.contains("required");
        
        assertTrue(hasValidationIndicator, "A resposta deve conter uma indicação de erro de validação");
    }
    
    /**
     * Valida respostas de recurso não encontrado (404 Not Found)
     * 
     * @param response Objeto Response do RestAssured
     * @throws AssertionError se o código HTTP não for 404
     * @implSpec Verifica exclusivamente o código de status HTTP 404
     */
    public void validateNotFoundErrorResponse(Response response) {
        assertEquals(404, response.getStatusCode(),
                "O código de status deve ser 404 (Not Found), mas foi: " + response.getStatusCode());
    }
    
    /**
     * Valida respostas de método HTTP não permitido (405 Method Not Allowed)
     * 
     * @param response Objeto Response do RestAssured
     * @throws AssertionError se o código HTTP não for 405
     * @implSpec Verifica exclusivamente o código de status HTTP 405
     */
    public void validateMethodNotAllowedErrorResponse(Response response) {
        assertEquals(405, response.getStatusCode(),
                "O código de status deve ser 405 (Method Not Allowed), mas foi: " + response.getStatusCode());
    }
    
    /**
     * Método facade que direciona para o validador específico baseado no tipo de erro
     * 
     * @param response Objeto Response do RestAssured
     * @param expectedErrorType Enumeração indicando o tipo de erro esperado
     * @throws AssertionError se a resposta não corresponder ao tipo de erro esperado
     * @implNote Implementa o padrão de design Facade para simplificar a validação de erros
     */
    public void validateErrorResponse(Response response, ErrorType expectedErrorType) {
        switch (expectedErrorType) {
            case AUTHENTICATION:
                validateAuthenticationErrorResponse(response);
                break;
            case VALIDATION:
                validateDataValidationErrorResponse(response);
                break;
            case NOT_FOUND:
                validateNotFoundErrorResponse(response);
                break;
            case METHOD_NOT_ALLOWED:
                validateMethodNotAllowedErrorResponse(response);
                break;
            default:
                validateErrorResponseFormat(response);
        }
    }
    
    /**
     * Valida o conteúdo específico de uma mensagem de erro no payload
     * 
     * @param response Objeto Response do RestAssured
     * @param errorField Campo/path JSON que contém a mensagem de erro
     * @param expectedErrorContent Conteúdo esperado na mensagem (parcial ou completo)
     * @throws AssertionError se a mensagem de erro não for encontrada ou não contiver o texto esperado
     * @implSpec Suporta verificação parcial de conteúdo através de contains()
     */
    public void validateErrorMessage(Response response, String errorField, String expectedErrorContent) {
        try {
            String errorMessage = response.jsonPath().getString(errorField);
            assertNotNull(errorMessage, "A mensagem de erro não pode ser nula");
            assertFalse(errorMessage.isEmpty(), "A mensagem de erro não pode ser vazia");
            
            if (expectedErrorContent != null && !expectedErrorContent.isEmpty()) {
                assertTrue(errorMessage.toLowerCase().contains(expectedErrorContent.toLowerCase()),
                        "A mensagem de erro deve conter: " + expectedErrorContent);
            }
        } catch (Exception e) {
            fail("Erro ao validar mensagem de erro: " + e.getMessage());
        }
    }
    
    /**
     * Enumeração de tipos de erro HTTP comuns para categorização em testes
     * 
     * @implNote Segue a semântica dos códigos de status HTTP padrão RFC 7231
     */
    public enum ErrorType {
        /** Erros 401 Unauthorized, 403 Forbidden */
        AUTHENTICATION,
        /** Erros 400 Bad Request */
        VALIDATION,
        /** Erros 404 Not Found */
        NOT_FOUND,
        /** Erros 405 Method Not Allowed */
        METHOD_NOT_ALLOWED,
        /** Erros 500 Internal Server Error */
        SERVER_ERROR,
        /** Qualquer tipo de erro não categorizado */
        GENERAL
    }
} 