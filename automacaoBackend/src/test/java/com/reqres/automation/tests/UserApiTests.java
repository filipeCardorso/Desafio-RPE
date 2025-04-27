package com.reqres.automation.tests;

import com.reqres.automation.assertions.UserAssertions;
import com.reqres.automation.fixtures.UserFixture;
import com.reqres.automation.model.User;
import com.reqres.automation.model.UserListResponse;
import com.reqres.automation.service.IUserService;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testes da API de usuários
 * Implementa todos os princípios SOLID, Clean Code e Clean Architecture
 */
@Epic("API Reqres.in")
@Feature("Operações com Usuários")
@Tag("user-api")
public class UserApiTests extends AbstractApiTest {

    @Autowired
    private IUserService userService;
    
    @Autowired
    private UserFixture userFixture;
    
    @Autowired
    private UserAssertions userAssertions;
    
    private static final int VALID_USER_ID = 2;
    private static final int INVALID_USER_ID = 999;

    @Test
    @Order(1)
    @Story("Listar usuários")
    @DisplayName("Deve retornar lista de usuários com sucesso")
    @Description("Teste que verifica se a API retorna a lista de usuários com sucesso")
    @Severity(SeverityLevel.BLOCKER)
    public void shouldReturnUserList() {
        // Arrange
        final int page = 1;
        
        // Act
        Response response = userService.getUserList(requestSpec, page);
        
        // Assert
        responseValidator.validateResponse(200, r -> {
            UserListResponse userList = r.as(UserListResponse.class);
            userAssertions.assertUserListResponse(userList, page);
            
            // Verificação adicional do primeiro usuário
            if (!userList.getData().isEmpty()) {
                userAssertions.assertUserBasicAttributes(userList.getData().get(0));
            }
        }, response);
    }
    
    @Test
    @Order(2)
    @Story("Buscar usuário específico")
    @DisplayName("Deve retornar usuário específico com sucesso")
    @Description("Teste que verifica se a API retorna os dados de um usuário específico")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldReturnSpecificUser() {
        // Act
        Response response = userService.getUser(requestSpec, VALID_USER_ID);
        
        // Assert
        responseValidator.validateResponse(200, r -> {
            User user = r.jsonPath().getObject("data", User.class);
            userAssertions.assertUserBasicAttributes(user);
            assertEquals(VALID_USER_ID, user.getId(), "ID do usuário incorreto");
        }, response);
    }
    
    @Test
    @Order(3)
    @Story("Criar usuário")
    @DisplayName("Deve criar um novo usuário com sucesso")
    @Description("Teste que verifica se a API cria um novo usuário corretamente")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldCreateUser() {
        // Arrange
        User newUser = userFixture.createValidUser();
        
        // Act
        Response response = userService.createUser(requestSpec, newUser);
        
        // Assert
        responseValidator.validateResponse(201, r -> {
            User createdUser = r.as(User.class);
            userAssertions.assertUserCreated(newUser, createdUser);
        }, response);
    }
    
    @Test
    @Order(4)
    @Story("Atualizar usuário")
    @DisplayName("Deve atualizar um usuário existente com sucesso")
    @Description("Teste que verifica se a API atualiza completamente os dados de um usuário")
    @Severity(SeverityLevel.NORMAL)
    public void shouldUpdateUser() {
        // Arrange
        User updatedUser = userFixture.createUserForUpdate();
        
        // Act
        Response response = userService.updateUser(requestSpec, VALID_USER_ID, updatedUser);
        
        // Assert
        responseValidator.validateResponse(200, r -> {
            User responseUser = r.as(User.class);
            userAssertions.assertUserUpdated(updatedUser, responseUser);
        }, response);
    }
    
    @Test
    @Order(5)
    @Story("Atualizar usuário parcialmente")
    @DisplayName("Deve atualizar parcialmente um usuário com sucesso")
    @Description("Teste que verifica se a API atualiza parcialmente os dados de um usuário")
    @Severity(SeverityLevel.NORMAL)
    public void shouldPartiallyUpdateUser() {
        // Arrange
        User partialUser = userFixture.createUserForPartialUpdate();
        
        // Act
        Response response = userService.patchUser(requestSpec, VALID_USER_ID, partialUser);
        
        // Assert
        responseValidator.validateResponse(200, r -> {
            User responseUser = r.as(User.class);
            userAssertions.assertUserUpdated(partialUser, responseUser);
        }, response);
    }
    
    @Test
    @Order(6)
    @Story("Remover usuário")
    @DisplayName("Deve remover um usuário com sucesso")
    @Description("Teste que verifica se a API remove um usuário corretamente")
    @Severity(SeverityLevel.NORMAL)
    public void shouldDeleteUser() {
        // Act
        Response response = userService.deleteUser(requestSpec, VALID_USER_ID);
        
        // Assert - para deleção, apenas validamos o status code
        responseValidator.validateStatusCode(204, response);
    }
    
    @Test
    @Order(7)
    @Story("Erro - Usuário inexistente")
    @DisplayName("Deve retornar erro ao buscar usuário inexistente")
    @Description("Teste que verifica se a API retorna erro ao buscar usuário que não existe")
    @Severity(SeverityLevel.NORMAL)
    public void shouldReturnErrorWhenUserNotFound() {
        // Act
        Response response = userService.getUser(requestSpec, INVALID_USER_ID);
        
        // Assert - para erro, apenas validamos o status code
        responseValidator.validateStatusCode(404, response);
    }
}   