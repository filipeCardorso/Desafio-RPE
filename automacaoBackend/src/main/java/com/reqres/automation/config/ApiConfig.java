package com.reqres.automation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuração centralizada para endpoints e credenciais da API
 * 
 * @implNote Esta classe implementa o padrão de design Configuration Property com
 * mecanismo de fallback para valores default, garantindo compatibilidade com
 * diferentes ambientes de execução
 * 
 * @implSpec Carrega automaticamente propriedades do arquivo application.properties
 * e fornece métodos de acesso para endpoints e credenciais necessários
 * nos testes automatizados
 * 
 * @apiNote Mantém centralizada a configuração da URL base, endpoints específicos,
 * credenciais e chave de API para evitar duplicação de código nos testes
 */
@Configuration
@PropertySource("classpath:application.properties")
public class ApiConfig {

    @Value("${api.base.url:https://reqres.in/api}")
    private String baseUrl;

    @Value("${api.users.endpoint:/users}")
    private String usersEndpoint;
    
    @Value("${api.auth.email:eve.holt@reqres.in}")
    private String authEmail;
    
    @Value("${api.auth.password:cityslicka}")
    private String authPassword;
    
    @Value("${api.auth.login.endpoint:/login}")
    private String loginEndpoint;
    
    @Value("${api.key:QpwL5tke4Pnpja7X4}")
    private String apiKey;

    /**
     * Obtém a URL base da API
     * 
     * @return URL base completa para construção de requisições
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Obtém o endpoint para operações com usuários
     * 
     * @return Path relativo do endpoint de usuários
     */
    public String getUsersEndpoint() {
        return usersEndpoint;
    }

    /**
     * Constrói o endpoint para operações com um usuário específico
     * 
     * @param userId Identificador único do usuário
     * @return Path relativo do endpoint de usuário específico
     */
    public String getUserEndpoint(Integer userId) {
        return usersEndpoint + "/" + userId;
    }
    
    /**
     * Obtém o email de autenticação configurado
     * 
     * @return Email de autenticação para obtenção de token
     */
    public String getAuthEmail() {
        return authEmail;
    }
    
    /**
     * Obtém a senha configurada para autenticação
     * 
     * @return Senha para autenticação
     */
    public String getAuthPassword() {
        return authPassword;
    }
    
    /**
     * Obtém o endpoint relativo para login/autenticação
     * 
     * @return Path relativo do endpoint de login
     */
    public String getLoginEndpoint() {
        return loginEndpoint;
    }
    
    /**
     * Constrói a URL completa do endpoint de login
     * 
     * @return URL absoluta para o endpoint de login
     */
    public String getFullLoginEndpoint() {
        return baseUrl + loginEndpoint;
    }
    
    /**
     * Obtém a chave de API para autenticação por API Key
     * 
     * @return Chave de API conforme configurada ou valor default
     */
    public String getApiKey() {
        return apiKey;
    }
} 