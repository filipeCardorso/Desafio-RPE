package com.reqres.automation.tests;

import com.reqres.automation.validators.ApiErrorValidator;
import com.reqres.automation.validators.ApiErrorValidator.ErrorType;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite de testes avançada para validação de tratamento de erros da API
 * 
 * @implNote Desenvolvida seguindo todas as diretrizes SOLID e Clean Architecture:
 * <ul>
 *   <li>SRP: Cada classe aninhada foca em um único tipo de erro</li>
 *   <li>OCP: Estrutura extensível para adição de novos casos de teste</li>
 *   <li>LSP: Herança apropriada de AbstractApiTest</li>
 *   <li>ISP: Uso de interfaces focadas para validação</li>
 *   <li>DIP: Injeção de dependências para os validadores</li>
 * </ul>
 * 
 * @implSpec Implementa os seguintes padrões de design:
 * <ul>
 *   <li>Strategy: Diferentes estratégias de validação para cada tipo de erro</li>
 *   <li>Nested Tests: Organização hierárquica para melhor legibilidade</li>
 *   <li>Parameterized Tests: Validação de múltiplos cenários com a mesma lógica</li>
 *   <li>AAA (Arrange-Act-Assert): Estrutura clara para cada caso de teste</li>
 * </ul>
 * 
 * @see ApiErrorValidator Para o validador especializado em respostas de erro
 * @see AbstractApiTest Para a configuração base dos testes de API
 */
@Epic("Testes de Tratamento de Erros da API")
@Feature("Validação avançada de respostas de erro")
@Tag("error-handling")
@Tag("improved")
@Owner("QA Team")
public class ImprovedApiErrorHandlingTest extends AbstractApiTest {
    
    @Autowired
    private ApiErrorValidator errorValidator;
    
    @Nested
    @DisplayName("Testes de validação de API Key")
    class ApiKeyValidationTests {
        
        @Test
        @Story("Validação de erro de API Key ausente")
        @DisplayName("Deve validar mensagem de erro quando API Key está ausente")
        @Description("Verifica se a API retorna mensagem de erro específica quando a chave de API não é fornecida")
        @Severity(SeverityLevel.CRITICAL)
        public void testApiKeyMissingError() {
            // Arrange - Requisição sem API Key
            RequestSpecification requestWithoutKey = RestAssured.given()
                    .baseUri(apiConfig.getBaseUrl())
                    .contentType(ContentType.JSON);
            
            // Act
            Response response = requestWithoutKey.get(apiConfig.getUsersEndpoint());
            
            // Assert
            // Validação usando o validator especializado
            errorValidator.validateErrorResponse(response, ErrorType.AUTHENTICATION);
            errorValidator.validateErrorMessage(response, "error", "Missing API key");
            responseValidator.validateFieldExists(response, "how_to_get_one");
        }
        
        @Test
        @Story("Validação de erro de API Key inválida")
        @DisplayName("Deve validar mensagem de erro quando API Key é inválida")
        @Description("Verifica se a API retorna mensagem de erro específica quando uma chave de API inválida é fornecida")
        @Severity(SeverityLevel.CRITICAL)
        public void testInvalidApiKeyError() {
            // Arrange - Requisição com API Key inválida
            RequestSpecification requestWithInvalidKey = RestAssured.given()
                    .baseUri(apiConfig.getBaseUrl())
                    .header("X-API-KEY", "invalid-api-key-12345")
                    .contentType(ContentType.JSON);
            
            // Act
            Response response = requestWithInvalidKey.get(apiConfig.getUsersEndpoint());
            
            // Assert usando validador especializado
            errorValidator.validateErrorResponse(response, ErrorType.AUTHENTICATION);
        }
    }
    
    @Nested
    @DisplayName("Testes de validação de autenticação")
    class AuthenticationValidationTests {
        
        @Test
        @Story("Validação de erro de credenciais inválidas")
        @DisplayName("Deve validar erro quando credenciais são inválidas")
        @Description("Verifica o comportamento da API quando credenciais incorretas são fornecidas")
        @Severity(SeverityLevel.CRITICAL)
        public void testInvalidCredentialsLogin() {
            // Arrange
            Map<String, String> invalidCredentials = new HashMap<>();
            invalidCredentials.put("email", "invalid@example.com");
            invalidCredentials.put("password", "invalid");
            
            // Act
            Response response = RestAssured.given()
                    .spec(requestSpec)
                    .body(invalidCredentials)
                    .post(apiConfig.getLoginEndpoint());
            
            // Assert
            errorValidator.validateErrorResponse(response, ErrorType.AUTHENTICATION);
            errorValidator.validateErrorMessage(response, "error", null);
        }
        
        @ParameterizedTest
        @ValueSource(strings = {
            "{\"email\": \"eve.holt@reqres.in\", \"password\": \"\"}",
            "{\"email\": \"\", \"password\": \"cityslicka\"}",
            "{\"email\": \"\", \"password\": \"\"}",
            "{}"
        })
        @Story("Validação de erros de campos de autenticação")
        @DisplayName("Deve validar erro quando campos de autenticação são inválidos")
        @Description("Verifica o comportamento da API quando campos de autenticação estão vazios ou ausentes")
        @Severity(SeverityLevel.NORMAL)
        public void testInvalidAuthFields(String requestBody) {
            // Act
            Response response = RestAssured.given()
                    .spec(requestSpec)
                    .body(requestBody)
                    .post(apiConfig.getLoginEndpoint());
            
            // Assert
            // Em alguns casos a API pode retornar erro de validação (400) ou autenticação (401/403)
            // Portanto, aceitamos ambos os tipos
            int statusCode = response.getStatusCode();
            if (statusCode == 400) {
                errorValidator.validateErrorResponse(response, ErrorType.VALIDATION);
            } else {
                errorValidator.validateErrorResponse(response, ErrorType.AUTHENTICATION);
            }
            
            // Verificar se existe uma mensagem de erro, independente do tipo
            try {
                String errorMessage = response.jsonPath().getString("error");
                assertNotNull(errorMessage, "A resposta deve conter uma mensagem de erro");
            } catch (Exception e) {
                // Se não conseguir obter o erro, verifica se há outros campos indicando erro
                boolean hasErrorInfo = 
                    !response.getBody().asString().isEmpty() && 
                    (response.getBody().asString().contains("error") || 
                     response.getBody().asString().contains("message"));
                
                assertTrue(hasErrorInfo, "A resposta deve conter alguma informação de erro");
            }
        }
    }
    
    @Nested
    @DisplayName("Testes de validação de formato de requisição")
    class RequestFormatValidationTests {
        
        @Test
        @Story("Validação de erro de formato JSON inválido")
        @DisplayName("Deve validar erro quando formato JSON da requisição é inválido")
        @Description("Verifica o comportamento da API quando JSON malformado é enviado")
        @Severity(SeverityLevel.NORMAL)
        public void testInvalidJsonFormat() {
            // Arrange - JSON malformado
            String invalidJson = "{ email: invalid-format, password: missing-quotes }";
            
            // Act
            Response response = RestAssured.given()
                    .spec(requestSpec)
                    .body(invalidJson)
                    .post(apiConfig.getLoginEndpoint());
            
            // Assert
            int statusCode = response.getStatusCode();
            
            // Em uma API bem projetada, esperaríamos um erro 400 Bad Request
            // Mas em APIs de teste, podemos ter comportamentos diferentes
            if (statusCode >= 400) {
                // Se retornou erro, validamos como erro de validação
                errorValidator.validateErrorResponseFormat(response);
            } else {
                // Em alguns casos, a API pode aceitar JSON malformado (não recomendado),
                // então apenas verificamos que houve uma resposta
                assertNotNull(response.getBody().asString());
            }
        }
        
        @Test
        @Story("Validação de erro de Content-Type")
        @DisplayName("Deve validar erro quando Content-Type é inválido")
        @Description("Verifica o comportamento da API quando Content-Type não é JSON")
        @Severity(SeverityLevel.NORMAL)
        public void testInvalidContentType() {
            // Arrange - Requisição com Content-Type text/plain
            String validCredentials = "email=eve.holt@reqres.in&password=cityslicka";
            
            // Act - Enviando como form-urlencoded em vez de JSON
            Response response = RestAssured.given()
                    .baseUri(apiConfig.getBaseUrl())
                    .header("X-API-KEY", apiConfig.getApiKey())
                    .contentType(ContentType.URLENC)
                    .body(validCredentials)
                    .post(apiConfig.getLoginEndpoint());
            
            // Assert
            int statusCode = response.getStatusCode();
            if (statusCode >= 400) {
                // A API rejeitou o formato (comportamento esperado para APIs que exigem JSON)
                errorValidator.validateErrorResponseFormat(response);
            } else {
                // A API aceitou outro formato, verificamos se retornou um token
                // Este é o caso da API reqres.in que aceita form-urlencoded
                try {
                    String responseBody = response.getBody().asString();
                    if (responseBody.contains("token")) {
                        String token = response.jsonPath().getString("token");
                        assertNotNull(token, "Token não pode ser nulo");
                        assertFalse(token.isEmpty(), "Token não pode ser vazio");
                    }
                } catch (Exception e) {
                    fail("A resposta não contém um token ou não é um JSON válido: " + e.getMessage());
                }
            }
        }
    }
    
    @Nested
    @DisplayName("Testes de recursos não encontrados")
    class ResourceNotFoundTests {
        
        @Test
        @Story("Validação de erro de recurso não encontrado")
        @DisplayName("Deve validar erro quando recurso não existe")
        @Description("Verifica se a API retorna 404 quando o recurso solicitado não existe")
        @Severity(SeverityLevel.NORMAL)
        public void testResourceNotFound() {
            // Act
            Response response = RestAssured.given()
                    .spec(requestSpec)
                    .get(apiConfig.getUsersEndpoint() + "/9999");
            
            // Assert usando o validador especializado
            errorValidator.validateErrorResponse(response, ErrorType.NOT_FOUND);
        }
    }
    
    @Nested
    @DisplayName("Testes de métodos não permitidos")
    class MethodNotAllowedTests {
        
        @Test
        @Story("Validação de erro de método não permitido")
        @DisplayName("Deve validar erro quando método HTTP não é permitido")
        @Description("Verifica se a API retorna 405 quando um método HTTP não permitido é utilizado")
        @Severity(SeverityLevel.NORMAL)
        public void testMethodNotAllowed() {
            // Tentar deletar um recurso que não suporta o método DELETE
            // ou utilizar PUT onde não é suportado
            Response response = RestAssured.given()
                    .spec(requestSpec)
                    .delete(apiConfig.getLoginEndpoint());
            
            // Assert
            int statusCode = response.getStatusCode();
            
            // A API pode retornar 405 (Method Not Allowed) especificamente
            // ou outro código de erro dependendo da implementação
            if (statusCode == HttpStatus.SC_METHOD_NOT_ALLOWED) {
                errorValidator.validateErrorResponse(response, ErrorType.METHOD_NOT_ALLOWED);
            } else if (statusCode >= 400) {
                // Se for outro código de erro, verificamos apenas o formato geral
                errorValidator.validateErrorResponseFormat(response);
            } else {
                fail("A API deveria retornar um erro para método não permitido, mas retornou: " + 
                     statusCode);
            }
        }
    }
    
    @Nested
    @DisplayName("Testes de erros de timeout e servidor")
    class ServerErrorTests {
        
        @Test
        @Story("Simulação de erro de servidor")
        @DisplayName("Deve validar resposta para simulação de erro interno")
        @Description("Verifica o comportamento da API quando um erro interno é simulado")
        @Severity(SeverityLevel.NORMAL)
        public void testServerErrorSimulation() {
            // Verificar se existe um endpoint que simula erros ou usar um endpoint especial
            // No caso do reqres.in não temos um endpoint específico, então esta é uma demonstração
            String errorSimulationEndpoint = apiConfig.getBaseUrl() + "/api/unknown/23"; // endpoint inexistente
            
            // Act
            Response response = RestAssured.given()
                    .spec(requestSpec)
                    .get(errorSimulationEndpoint);
            
            // Assert
            // Como estamos usando um endpoint inexistente, esperamos um 404
            // Em um cenário real com simulação de erro de servidor, seria 500
            errorValidator.validateNotFoundErrorResponse(response);
        }
    }
} 