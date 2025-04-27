package com.reqres.automation.tests;

import com.reqres.automation.config.ApiConfig;
import com.reqres.automation.fixtures.RequestFixture;
import com.reqres.automation.service.IAuthService;
import com.reqres.automation.util.ReportManager;
import com.reqres.automation.validators.ResponseValidator;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Classe abstrata base para todos os testes de API
 * 
 * @implNote Implementa o princípio de Inversão de Dependência (DIP) do SOLID através
 * da injeção de dependências via Spring
 * 
 * @implNote Adota o padrão Template Method ao definir comportamentos comuns a todos
 * os testes, permitindo customização específica nas subclasses
 * 
 * @implSpec Gerencia ciclo de vida dos testes com autenticação automática
 * e configuração padrão para requisições REST
 */
@SpringBootTest
public abstract class AbstractApiTest {
    
    @Autowired
    protected ApiConfig apiConfig;
    
    @Autowired
    protected IAuthService authService;
    
    @Autowired
    protected RequestFixture requestFixture;
    
    @Autowired
    protected ResponseValidator responseValidator;
    
    protected RequestSpecification requestSpec;
    protected String authToken;
    
    /**
     * Configuração global para todos os testes de API
     * Habilita logging automático para facilitar depuração em caso de falha nos testes
     */
    @BeforeAll
    public static void setupAll() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
    
    /**
     * Inicialização para cada caso de teste individual
     * 
     * @param testInfo Metadados do teste atual para registro e relatório
     * @implNote Implementa throttling de requisições para evitar problemas de rate limiting
     */
    @BeforeEach
    public void setupEach(TestInfo testInfo) {
        ReportManager.logTestStart(testInfo.getDisplayName());
        
        // Throttling para evitar rate limiting em APIs externas
        addDelay(1000);
        
        // Autenticação centralizada via token JWT/OAuth
        authToken = authService.getAuthToken();
        
        // Especificação padrão de requisição seguindo o padrão Builder
        requestSpec = requestFixture.createAuthSpec(authToken);
    }
    
    /**
     * Limpeza após cada caso de teste
     * 
     * @implNote Implementa delay consistente após execução para evitar
     * sobrecarga na API durante suítes de teste extensas
     */
    @AfterEach
    public void tearDown() {
        addDelay(1000);
    }
    
    /**
     * Implementa um mecanismo de delay controlado para operações de API
     * 
     * @param milliseconds Tempo de espera em milissegundos
     * @throws RuntimeException em caso de interrupção, preservando sinais de interrupção
     */
    protected void addDelay(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Factory method para especificações de resposta de sucesso
     * 
     * @param expectedStatusCode Código HTTP de status esperado para cenário de sucesso (2XX)
     * @return ResponseSpecification configurada para validar respostas de sucesso
     * @implNote Configura validação automática de Content-Type JSON para respostas bem-sucedidas
     */
    protected ResponseSpecification createSuccessResponseSpec(int expectedStatusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(expectedStatusCode)
                .expectContentType(ContentType.JSON)
                .build();
    }
    
    /**
     * Factory method para especificações de resposta de erro
     * 
     * @param expectedStatusCode Código HTTP de status esperado para cenário de erro (4XX/5XX)
     * @return ResponseSpecification configurada para validar respostas de erro
     * @implNote Não valida Content-Type para permitir flexibilidade em respostas de erro
     * que podem ou não retornar JSON
     */
    protected ResponseSpecification createErrorResponseSpec(int expectedStatusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(expectedStatusCode)
                .build();
    }
} 