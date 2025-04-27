package com.reqres.automation.service;

import com.reqres.automation.model.User;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * Contrato para operações de gerenciamento de usuários na API
 * 
 * @implNote Implementa o princípio de Segregação de Interface (ISP) do SOLID,
 * definindo operações específicas para o domínio de usuários
 * 
 * @implSpec Esta interface segue o padrão de design Repository para abstração 
 * de operações CRUD, isolando os detalhes da API dos consumidores do serviço.
 * Define um conjunto completo de operações para o ciclo de vida de usuários:
 * <ul>
 *   <li>Listagem paginada</li>
 *   <li>Recuperação de usuário específico</li>
 *   <li>Criação de novos usuários</li>
 *   <li>Atualização completa ou parcial</li>
 *   <li>Exclusão de usuários</li>
 * </ul>
 * 
 * @apiNote Todas as operações recebem RequestSpecification para flexibilizar
 * as configurações de cada chamada, permitindo customização de headers,
 * autenticação, timeouts, etc.
 */
public interface IUserService {
    
    /**
     * Recupera uma lista paginada de usuários
     * 
     * @param requestSpec Especificação da requisição com autenticação e headers
     * @param page Número da página desejada (base 1)
     * @return Resposta contendo metadados de paginação e lista de usuários
     * @implNote Tipicamente retorna HTTP 200 com array de usuários no campo "data"
     * e informações de paginação nos campos "page", "per_page", "total" e "total_pages"
     */
    Response getUserList(RequestSpecification requestSpec, int page);
    
    /**
     * Recupera informações detalhadas de um usuário específico
     * 
     * @param requestSpec Especificação da requisição com autenticação e headers
     * @param userId Identificador único do usuário
     * @return Resposta contendo os dados do usuário ou erro 404 se não encontrado
     * @implNote Tipicamente retorna HTTP 200 com dados do usuário no campo "data"
     * ou HTTP 404 se o usuário não existir
     */
    Response getUser(RequestSpecification requestSpec, int userId);
    
    /**
     * Cria um novo usuário na API
     * 
     * @param requestSpec Especificação da requisição com autenticação e headers
     * @param user Objeto contendo nome, função e outros dados do usuário
     * @return Resposta contendo o usuário criado, incluindo ID e timestamps
     * @implNote Tipicamente retorna HTTP 201 (Created) com dados do usuário criado
     * incluindo o ID gerado e timestamps "createdAt"
     */
    Response createUser(RequestSpecification requestSpec, User user);
    
    /**
     * Atualiza completamente um usuário existente
     * 
     * @param requestSpec Especificação da requisição com autenticação e headers
     * @param userId Identificador único do usuário a ser atualizado
     * @param user Objeto contendo todos os dados atualizados do usuário
     * @return Resposta contendo o usuário atualizado, incluindo timestamp de atualização
     * @implNote Segue a semântica HTTP PUT onde todos os campos são substituídos.
     * Tipicamente retorna HTTP 200 com dados do usuário atualizado e timestamp "updatedAt"
     */
    Response updateUser(RequestSpecification requestSpec, int userId, User user);
    
    /**
     * Atualiza parcialmente um usuário existente
     * 
     * @param requestSpec Especificação da requisição com autenticação e headers
     * @param userId Identificador único do usuário a ser atualizado
     * @param user Objeto contendo apenas os campos a serem atualizados
     * @return Resposta contendo o usuário atualizado, incluindo timestamp de atualização
     * @implNote Segue a semântica HTTP PATCH onde apenas os campos enviados são atualizados.
     * Tipicamente retorna HTTP 200 com dados do usuário atualizado e timestamp "updatedAt"
     */
    Response patchUser(RequestSpecification requestSpec, int userId, User user);
    
    /**
     * Remove um usuário da API
     * 
     * @param requestSpec Especificação da requisição com autenticação e headers
     * @param userId Identificador único do usuário a ser removido
     * @return Resposta vazia indicando sucesso ou erro apropriado
     * @implNote Tipicamente retorna HTTP 204 (No Content) em caso de sucesso
     * ou HTTP 404 (Not Found) se o usuário não existir
     */
    Response deleteUser(RequestSpecification requestSpec, int userId);
}