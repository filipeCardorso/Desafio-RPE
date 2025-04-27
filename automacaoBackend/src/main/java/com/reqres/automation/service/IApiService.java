package com.reqres.automation.service;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * Contrato principal para interações HTTP com a API
 * 
 * @implNote Implementa o princípio de Segregação de Interfaces (ISP) do SOLID,
 * fornecendo uma interface coesa exclusivamente para operações HTTP básicas
 * 
 * @implSpec Abstrai os detalhes de implementação das chamadas REST,
 * permitindo múltiplas implementações e facilitando mock em testes:
 * <ul>
 *   <li>Implementação real baseada em RestAssured</li>
 *   <li>Implementação com cache para otimização</li>
 *   <li>Implementação mockada para testes unitários</li>
 *   <li>Implementação com circuit breaker para resiliência</li>
 * </ul>
 * 
 * @apiNote Todas as operações aceitam RequestSpecification para permitir
 * personalização de cada requisição, seguindo o princípio de inversão
 * de controle e permitindo injeção das configurações
 */
public interface IApiService {
    
    /**
     * Executa uma requisição HTTP GET
     * 
     * @param spec Especificação completa da requisição com headers, auth, etc.
     * @param endpoint Path relativo do endpoint a ser acessado
     * @return Objeto Response encapsulando a resposta HTTP completa
     * @implNote Deve seguir o padrão RESTful onde GET é idempotente e
     * utilizado apenas para recuperação de dados, sem efeitos colaterais
     */
    Response get(RequestSpecification spec, String endpoint);
    
    /**
     * Executa uma requisição HTTP POST
     * 
     * @param spec Especificação completa da requisição com headers, auth, etc.
     * @param endpoint Path relativo do endpoint a ser acessado
     * @param payload Objeto a ser serializado como corpo da requisição (JSON)
     * @return Objeto Response encapsulando a resposta HTTP completa
     * @implNote No padrão RESTful, POST é utilizado para criação de
     * recursos ou execução de operações não-idempotentes
     */
    Response post(RequestSpecification spec, String endpoint, Object payload);
    
    /**
     * Executa uma requisição HTTP PUT
     * 
     * @param spec Especificação completa da requisição com headers, auth, etc.
     * @param endpoint Path relativo do endpoint a ser acessado
     * @param payload Objeto a ser serializado como corpo da requisição (JSON)
     * @return Objeto Response encapsulando a resposta HTTP completa
     * @implNote No padrão RESTful, PUT é idempotente e utilizado para
     * substituição completa de recursos existentes
     */
    Response put(RequestSpecification spec, String endpoint, Object payload);
    
    /**
     * Executa uma requisição HTTP PATCH
     * 
     * @param spec Especificação completa da requisição com headers, auth, etc.
     * @param endpoint Path relativo do endpoint a ser acessado
     * @param payload Objeto a ser serializado como corpo da requisição (JSON)
     * @return Objeto Response encapsulando a resposta HTTP completa
     * @implNote No padrão RESTful, PATCH é utilizado para atualização
     * parcial de recursos existentes, modificando apenas os campos enviados
     */
    Response patch(RequestSpecification spec, String endpoint, Object payload);
    
    /**
     * Executa uma requisição HTTP DELETE
     * 
     * @param spec Especificação completa da requisição com headers, auth, etc.
     * @param endpoint Path relativo do endpoint a ser acessado
     * @return Objeto Response encapsulando a resposta HTTP completa
     * @implNote No padrão RESTful, DELETE é idempotente e utilizado para
     * remoção de recursos, devendo retornar tipicamente 204 (sem conteúdo)
     */
    Response delete(RequestSpecification spec, String endpoint);
} 