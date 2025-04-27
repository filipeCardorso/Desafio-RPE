package com.reqres.automation.validators;

import io.restassured.response.Response;
import java.util.function.Consumer;

/**
 * Interface para validações de resposta de API
 * Implementa o princípio de Segregação de Interfaces (I) do SOLID
 */
public interface ResponseValidator {
    
    /**
     * Valida o status code e executa validações adicionais em uma resposta
     * 
     * @param expectedCode código de status HTTP esperado
     * @param responseConsumer função para validações adicionais (pode ser null)
     * @param response objeto de resposta da API
     */
    void validateResponse(int expectedCode, Consumer<Response> responseConsumer, Response response);
    
    /**
     * Valida se uma resposta possui um statusCode específico
     * 
     * @param expectedCode código de status HTTP esperado
     * @param response objeto de resposta da API
     */
    void validateStatusCode(int expectedCode, Response response);
    
    /**
     * Valida se uma resposta possui um campo específico
     * 
     * @param response objeto de resposta da API
     * @param field nome do campo a ser validado
     */
    void validateFieldExists(Response response, String field);
    
    /**
     * Valida se uma resposta possui conteúdo JSON
     * 
     * @param response objeto de resposta da API
     */
    void validateJsonContent(Response response);
} 