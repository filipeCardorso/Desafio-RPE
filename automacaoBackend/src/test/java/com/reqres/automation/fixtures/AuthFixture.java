package com.reqres.automation.fixtures;

import com.reqres.automation.config.ApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe responsável por criar dados de teste para autenticação
 * Implementa o padrão de projeto Fixture para isolamento dos testes
 */
@Component
public class AuthFixture {

    @Autowired
    private ApiConfig apiConfig;
    
    /**
     * Cria um mapa com credenciais válidas para autenticação
     * @return Mapa com email e senha válidos
     */
    public Map<String, String> createValidCredentials() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", apiConfig.getAuthEmail());
        credentials.put("password", apiConfig.getAuthPassword());
        return credentials;
    }
    
    /**
     * Cria um mapa com credenciais inválidas para autenticação
     * @return Mapa com email válido e senha inválida
     */
    public Map<String, String> createInvalidPasswordCredentials() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", apiConfig.getAuthEmail());
        credentials.put("password", "senhaInvalida");
        return credentials;
    }
    
    /**
     * Cria um mapa apenas com email (sem senha)
     * @return Mapa apenas com email
     */
    public Map<String, String> createCredentialsWithoutPassword() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", apiConfig.getAuthEmail());
        return credentials;
    }
    
    /**
     * Cria um mapa apenas com senha (sem email)
     * @return Mapa apenas com senha
     */
    public Map<String, String> createCredentialsWithoutEmail() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("password", apiConfig.getAuthPassword());
        return credentials;
    }
} 