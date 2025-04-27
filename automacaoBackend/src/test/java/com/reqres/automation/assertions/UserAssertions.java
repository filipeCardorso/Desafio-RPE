package com.reqres.automation.assertions;

import com.reqres.automation.model.User;
import com.reqres.automation.model.UserListResponse;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe responsável por fornecer asserções específicas para usuários
 * Implementa o princípio de Responsabilidade Única (S) do SOLID
 */
@Component
public class UserAssertions {

    /**
     * Valida atributos básicos de um usuário
     * @param user usuário a ser validado
     */
    public void assertUserBasicAttributes(User user) {
        assertNotNull(user, "Usuário não pode ser nulo");
        assertNotNull(user.getId(), "ID do usuário não pode ser nulo");
        assertNotNull(user.getEmail(), "Email do usuário não pode ser nulo");
        assertNotNull(user.getFirstName(), "Nome do usuário não pode ser nulo");
        assertNotNull(user.getLastName(), "Sobrenome do usuário não pode ser nulo");
    }
    
    /**
     * Valida a resposta da lista de usuários
     * @param userList resposta da lista de usuários
     * @param expectedPage página esperada
     */
    public void assertUserListResponse(UserListResponse userList, int expectedPage) {
        assertNotNull(userList, "A resposta não pode ser nula");
        assertEquals(expectedPage, userList.getPage(), "Página incorreta");
        assertNotNull(userList.getData(), "Lista de dados não pode ser nula");
        assertFalse(userList.getData().isEmpty(), "Lista de dados não pode ser vazia");
    }
    
    /**
     * Valida se um usuário foi criado corretamente
     * @param expected usuário esperado
     * @param actual usuário retornado pela API
     */
    public void assertUserCreated(User expected, User actual) {
        assertNotNull(actual, "Usuário criado não pode ser nulo");
        assertNotNull(actual.getId(), "ID do usuário criado não pode ser nulo");
        assertEquals(expected.getFirstName(), actual.getFirstName(), "Nome do usuário incorreto");
        assertEquals(expected.getLastName(), actual.getLastName(), "Sobrenome do usuário incorreto");
        assertEquals(expected.getEmail(), actual.getEmail(), "Email do usuário incorreto");
        assertEquals(expected.getJob(), actual.getJob(), "Cargo do usuário incorreto");
        assertNotNull(actual.getCreatedAt(), "Data de criação não pode ser nula");
    }
    
    /**
     * Valida se um usuário foi atualizado corretamente
     * @param expected usuário esperado
     * @param actual usuário retornado pela API
     */
    public void assertUserUpdated(User expected, User actual) {
        assertNotNull(actual, "Usuário atualizado não pode ser nulo");
        
        if (expected.getFirstName() != null) {
            assertEquals(expected.getFirstName(), actual.getFirstName(), "Nome do usuário incorreto");
        }
        
        if (expected.getLastName() != null) {
            assertEquals(expected.getLastName(), actual.getLastName(), "Sobrenome do usuário incorreto");
        }
        
        if (expected.getJob() != null) {
            assertEquals(expected.getJob(), actual.getJob(), "Cargo do usuário incorreto");
        }
        
        assertNotNull(actual.getUpdatedAt(), "Data de atualização não pode ser nula");
    }
} 