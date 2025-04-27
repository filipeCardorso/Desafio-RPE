package com.reqres.automation.service;

/**
 * Contrato para serviços de autenticação com a API
 * 
 * @implNote Implementa o princípio de Segregação de Interface (ISP) do SOLID,
 * mantendo o contrato focado apenas em operações de autenticação
 * 
 * @implSpec Esta interface define o comportamento esperado para componentes
 * que lidam com autenticação, permitindo diferentes implementações como:
 * <ul>
 *   <li>Autenticação baseada em JWT</li>
 *   <li>Autenticação baseada em OAuth</li>
 *   <li>Autenticação por API Key</li>
 *   <li>Implementações mockadas para testes</li>
 * </ul>
 * 
 * @apiNote Desacopla o mecanismo de autenticação do resto do sistema,
 * facilitando testes e permitindo mudanças na implementação sem
 * afetar os consumidores do serviço
 */
public interface IAuthService {
    
    /**
     * Obtém um token de autenticação válido para uso nas requisições
     * 
     * @return Token de autenticação (JWT/Bearer) ou string vazia em caso de falha
     * @implNote Implementações devem gerenciar cache e renovação automática
     * de tokens expirados, evitando requisições desnecessárias de autenticação
     */
    String getAuthToken();
} 