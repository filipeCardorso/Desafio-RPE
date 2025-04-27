package com.reqres.automation.fixtures;

import com.reqres.automation.model.User;
import org.springframework.stereotype.Component;

/**
 * Classe responsável por criar dados de teste para usuários
 * Implementa o padrão de projeto Fixture para isolamento dos testes
 */
@Component
public class UserFixture {

    /**
     * Cria um novo usuário válido para testes
     * @return Objeto User configurado
     */
    public User createValidUser() {
        return User.builder()
                .firstName("João")
                .lastName("Silva")
                .email("joao.silva@email.com")
                .job("QA Engineer")
                .build();
    }
    
    /**
     * Cria um usuário para teste de atualização
     * @return Objeto User configurado para atualização
     */
    public User createUserForUpdate() {
        return User.builder()
                .firstName("João Atualizado")
                .lastName("Silva Atualizado")
                .job("Senior QA Engineer")
                .build();
    }
    
    /**
     * Cria um usuário para teste de atualização parcial
     * @return Objeto User configurado para atualização parcial
     */
    public User createUserForPartialUpdate() {
        return User.builder()
                .job("Automation Specialist")
                .build();
    }
} 