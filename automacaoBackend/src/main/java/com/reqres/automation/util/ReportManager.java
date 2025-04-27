package com.reqres.automation.util;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilitário centralizado para gerenciamento de relatórios e logs dos testes
 * 
 * @implNote Implementa o padrão de design Singleton através de construtores
 * privados e métodos estáticos, garantindo uma única interface para relatórios
 * 
 * @implSpec Integra-se com o framework Allure para geração de relatórios
 * detalhados, incluindo anexos de requisições e respostas via anotações
 * {@link Attachment} e {@link Step}
 * 
 * @apiNote Esta classe implementa fallback para logging via System.out
 * quando o SLF4J não estiver configurado corretamente, garantindo
 * visibilidade de logs em qualquer ambiente de execução
 */
public class ReportManager {
    
    /** Logger SLF4J com nome completo da classe para facilitar configuração */
    private static final Logger logger = LoggerFactory.getLogger(ReportManager.class);
    
    /**
     * Construtor privado para impedir instanciação direta
     * 
     * @implNote Segue o padrão de design Utility Class com métodos estáticos
     */
    private ReportManager() {
    }
    
    /**
     * Registra o início de execução de um caso de teste no log e relatório
     * 
     * @param testName Nome completo ou descrição do teste
     * @implNote Utiliza a anotação {@link Step} do Allure para incluir
     * esta informação como um passo visível nos relatórios de teste
     */
    @Step("Iniciando teste: {testName}")
    public static void logTestStart(String testName) {
        logInfo("===============================================================");
        logInfo("Iniciando teste: {}", testName);
        logInfo("===============================================================");
    }
    
    /**
     * Método resiliente para logging de mensagens informativas
     * 
     * @param message Template da mensagem com placeholders {} para argumentos
     * @param args Argumentos variáveis para substituição nos placeholders
     * @implNote Implementa fallback para System.out caso o logger SLF4J 
     * não esteja disponível ou falhe
     */
    private static void logInfo(String message, Object... args) {
        try {
            if (logger != null) {
                logger.info(message, args);
            } else {
                System.out.println(formatMessage(message, args));
            }
        } catch (Exception e) {
            System.out.println("Erro ao registrar log: " + e.getMessage());
            System.out.println(formatMessage(message, args));
        }
    }
    
    /**
     * Método resiliente para logging de mensagens de erro
     * 
     * @param message Template da mensagem com placeholders {} para argumentos
     * @param args Argumentos variáveis para substituição nos placeholders
     * @implNote Implementa fallback para System.err caso o logger SLF4J 
     * não esteja disponível ou falhe
     */
    private static void logError(String message, Object... args) {
        try {
            if (logger != null) {
                logger.error(message, args);
            } else {
                System.err.println(formatMessage(message, args));
            }
        } catch (Exception e) {
            System.err.println("Erro ao registrar log de erro: " + e.getMessage());
            System.err.println(formatMessage(message, args));
        }
    }
    
    /**
     * Implementação manual de formatação de mensagens de log
     * 
     * @param message Template da mensagem com placeholders {} para argumentos
     * @param args Argumentos variáveis para substituição nos placeholders
     * @return Mensagem formatada com valores substituídos
     * @implNote Emula o comportamento do SLF4J de substituição de placeholders {},
     * permitindo o mesmo formato de mensagens entre o logger e o fallback
     */
    private static String formatMessage(String message, Object... args) {
        if (args == null || args.length == 0) {
            return message;
        }
        
        String formattedMessage = message;
        try {
            for (Object arg : args) {
                int index = formattedMessage.indexOf("{}");
                if (index >= 0) {
                    formattedMessage = formattedMessage.substring(0, index) + 
                                      (arg == null ? "null" : arg.toString()) + 
                                      formattedMessage.substring(index + 2);
                }
            }
        } catch (Exception e) {
            return message + " [Erro ao formatar: " + e.getMessage() + "]";
        }
        
        return formattedMessage;
    }
    
    /**
     * Anexa o corpo da requisição JSON ao relatório Allure
     * 
     * @param requestBody Corpo da requisição em formato JSON
     * @return O mesmo corpo da requisição para encadeamento de métodos
     * @implNote Utiliza {@link Attachment} do Allure para incluir o JSON
     * como evidência no relatório, com type definido para formatação adequada
     */
    @Attachment(value = "Request Body", type = "application/json")
    public static String attachRequestBody(String requestBody) {
        String safeRequestBody = requestBody != null ? requestBody : "{}";
        logInfo("Request Body: {}", safeRequestBody);
        return safeRequestBody;
    }
    
    /**
     * Anexa o corpo da resposta HTTP ao relatório Allure
     * 
     * @param response Objeto Response do RestAssured contendo a resposta completa
     * @return Corpo da resposta em formato String para exibição no relatório
     * @implNote Implementa validação de nulidade e tratamento de exceções para
     * garantir que o relatório seja gerado mesmo em caso de respostas problemáticas
     */
    @Attachment(value = "Response Body", type = "application/json")
    public static String attachResponseBody(Response response) {
        if (response == null) {
            logInfo("Response is null");
            return "{}";
        }
        
        try {
            String responseBody = response.getBody() != null ? response.getBody().asString() : "{}";
            logInfo("Response Body: {}", responseBody);
            return responseBody;
        } catch (Exception e) {
            logError("Erro ao processar o corpo da resposta: {}", e.getMessage());
            return "{ \"error\": \"Failed to process response body\" }";
        }
    }
    
    /**
     * Anexa mensagem de erro ao relatório Allure como evidência
     * 
     * @param message Mensagem detalhada do erro ocorrido
     * @return A mesma mensagem de erro para encadeamento de métodos
     * @implNote Registra o erro tanto no log quanto no relatório Allure,
     * proporcionando visibilidade do problema em múltiplos canais
     */
    @Attachment(value = "Error Message", type = "text/plain")
    public static String attachErrorMessage(String message) {
        logError("Error: {}", message);
        return message;
    }
} 