package com.reqres.automation.validators;

import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Classe responsável pela validação de esquemas (schemas) de respostas JSON
 * Implementa o princípio de Responsabilidade Única (S) do SOLID
 */
@Component
public class ResponseSchemaValidator {
    
    private static final String SCHEMA_BASE_PATH = "schemas/";
    private static final Map<String, String> ERROR_SCHEMAS = new HashMap<>();
    
    static {
        // Mapeamento de tipos de erro para esquemas
        ERROR_SCHEMAS.put("authentication", "auth-error-schema.json");
        ERROR_SCHEMAS.put("validation", "validation-error-schema.json");
        ERROR_SCHEMAS.put("not-found", "not-found-schema.json");
        ERROR_SCHEMAS.put("method-not-allowed", "method-not-allowed-schema.json");
        ERROR_SCHEMAS.put("server-error", "server-error-schema.json");
    }
    
    /**
     * Valida se uma resposta está conforme um esquema JSON específico
     * @param response resposta a ser validada
     * @param schemaPath caminho para o arquivo de esquema JSON
     */
    public void validateResponseSchema(Response response, String schemaPath) {
        // Verificar se é uma resposta JSON antes de validar o esquema
        assertEquals(ContentType.JSON.toString(), 
                response.getContentType(), 
                "A resposta deve ser do tipo JSON para validação de esquema");
        
        try {
            InputStream schemaStream = getClass().getClassLoader()
                    .getResourceAsStream(SCHEMA_BASE_PATH + schemaPath);
            
            response.then().assertThat()
                    .body(JsonSchemaValidator.matchesJsonSchema(schemaStream));
        } catch (Exception e) {
            throw new AssertionError("Falha na validação do esquema JSON: " + e.getMessage());
        }
    }
    
    /**
     * Valida se uma resposta de erro está conforme um esquema JSON esperado
     * @param response resposta a ser validada
     * @param errorType tipo de erro para determinar o esquema
     */
    public void validateErrorResponseSchema(Response response, String errorType) {
        String schemaPath = ERROR_SCHEMAS.get(errorType.toLowerCase());
        if (schemaPath == null) {
            throw new IllegalArgumentException("Tipo de erro não suportado: " + errorType);
        }
        
        validateResponseSchema(response, schemaPath);
    }
    
    /**
     * Verifica se os campos obrigatórios existem na resposta de erro
     * @param response resposta a ser validada
     * @param requiredFields campos obrigatórios que devem existir
     */
    public void validateErrorResponseFields(Response response, String... requiredFields) {
        // Verificar se a resposta é JSON
        try {
            response.then().extract().jsonPath();
            
            for (String field : requiredFields) {
                Object value = response.jsonPath().get(field);
                assertTrue(value != null, 
                        "O campo obrigatório '" + field + "' não foi encontrado na resposta de erro");
            }
        } catch (Exception e) {
            throw new AssertionError("Falha ao validar campos de erro: " + e.getMessage());
        }
    }
    
    /**
     * Verifica se a estrutura básica de uma resposta de erro está correta
     * @param response resposta a ser validada
     */
    public void validateBasicErrorStructure(Response response) {
        // Verificar se contém pelo menos um campo de erro comum
        try {
            String jsonBody = response.getBody().asString();
            assertTrue(jsonBody.contains("error") || 
                      jsonBody.contains("message") || 
                      jsonBody.contains("code") ||
                      jsonBody.contains("status"),
                    "A resposta deve conter pelo menos um campo de erro padrão");
        } catch (Exception e) {
            throw new AssertionError("Falha ao validar estrutura de erro: " + e.getMessage());
        }
    }
    
    /**
     * Carrega e valida um esquema personalizado para um tipo específico de resposta
     * @param response resposta a ser validada
     * @param customSchemaPath caminho personalizado para o esquema
     */
    public void validateCustomSchema(Response response, String customSchemaPath) {
        File schemaFile = new File(customSchemaPath);
        if (!schemaFile.exists()) {
            throw new IllegalArgumentException("Esquema personalizado não encontrado: " + customSchemaPath);
        }
        
        try {
            response.then().assertThat()
                    .body(JsonSchemaValidator.matchesJsonSchema(schemaFile));
        } catch (Exception e) {
            throw new AssertionError("Falha na validação do esquema personalizado: " + e.getMessage());
        }
    }
} 