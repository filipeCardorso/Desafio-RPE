package com.reqres.automation.service;

import com.reqres.automation.config.ApiConfig;
import com.reqres.automation.util.ReportManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementação do serviço de autenticação
 * Aplica os princípios de Responsabilidade Única (S) e Inversão de Dependência (D) do SOLID
 */
@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private ApiConfig apiConfig;
    
    @Autowired
    private IApiService apiService;

    @Override
    public String getAuthToken() {
        try {
            
            Map<String, String> credentials = createCredentials();
            ReportManager.attachRequestBody(credentials.toString());
            
    
            RequestSpecBuilder requestBuilder = new RequestSpecBuilder()
                    .setBaseUri(apiConfig.getBaseUrl())
                    .setContentType(ContentType.JSON)
                    .addHeader("X-API-KEY", apiConfig.getApiKey());
                
            RequestSpecification authSpec = requestBuilder.build();

            Response response = apiService.post(authSpec, apiConfig.getLoginEndpoint(), credentials);
            
            if (response.getStatusCode() != 200) {
                String errorMessage = "Erro na autenticação: código " + response.getStatusCode();
                ReportManager.attachErrorMessage(errorMessage);
                return "";
            }
            
            return response.jsonPath().getString("token");
        } catch (Exception e) {
            String errorMessage = "Erro ao obter token de autenticação: " + e.getMessage();
            ReportManager.attachErrorMessage(errorMessage);
            return "";
        }
    }
    
    /**
     * Cria o mapa de credenciais para autenticação
     * @return Mapa com as credenciais
     */
    private Map<String, String> createCredentials() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", apiConfig.getAuthEmail());
        credentials.put("password", apiConfig.getAuthPassword());
        return credentials;
    }
} 