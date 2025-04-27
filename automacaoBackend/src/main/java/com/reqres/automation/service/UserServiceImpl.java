package com.reqres.automation.service;

import com.reqres.automation.config.ApiConfig;
import com.reqres.automation.model.User;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementação do serviço de usuários
 * Aplica os princípios de Responsabilidade Única (S) e Inversão de Dependência (D) do SOLID
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private ApiConfig apiConfig;
    
    @Autowired
    private IApiService apiService;
    
    @Override
    @Step("Buscando lista de usuários - página {page}")
    public Response getUserList(RequestSpecification requestSpec, int page) {
        String endpoint = apiConfig.getUsersEndpoint() + "?page=" + page;
        return apiService.get(requestSpec, endpoint);
    }
    
    @Override
    @Step("Buscando usuário com ID {userId}")
    public Response getUser(RequestSpecification requestSpec, int userId) {
        String endpoint = apiConfig.getUserEndpoint(userId);
        return apiService.get(requestSpec, endpoint);
    }
    
    @Override
    @Step("Criando novo usuário")
    public Response createUser(RequestSpecification requestSpec, User user) {
        String endpoint = apiConfig.getUsersEndpoint();
        return apiService.post(requestSpec, endpoint, user);
    }
    
    @Override
    @Step("Atualizando usuário ID {userId}")
    public Response updateUser(RequestSpecification requestSpec, int userId, User user) {
        String endpoint = apiConfig.getUserEndpoint(userId);
        return apiService.put(requestSpec, endpoint, user);
    }
    
    @Override
    @Step("Atualizando parcialmente usuário ID {userId}")
    public Response patchUser(RequestSpecification requestSpec, int userId, User user) {
        String endpoint = apiConfig.getUserEndpoint(userId);
        return apiService.patch(requestSpec, endpoint, user);
    }
    
    @Override
    @Step("Removendo usuário ID {userId}")
    public Response deleteUser(RequestSpecification requestSpec, int userId) {
        String endpoint = apiConfig.getUserEndpoint(userId);
        return apiService.delete(requestSpec, endpoint);
    }
} 