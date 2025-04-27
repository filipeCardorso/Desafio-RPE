package com.reqres.automation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Modelo para resposta paginada da listagem de usuários da API ReqRes
 * 
 * @implNote Implementa o padrão de design DTO (Data Transfer Object) com
 * classe interna aninhada para estruturas complexas de resposta
 * 
 * @implSpec Utiliza anotações Jackson para mapeamento flexível entre JSON e Java,
 * suportando mudanças na API com mínimo impacto no código através de
 * {@link JsonIgnoreProperties} para propriedades desconhecidas
 * 
 * @apiNote Esta classe representa a estrutura completa de uma resposta paginada,
 * incluindo metadados de paginação e a lista de usuários no campo "data"
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserListResponse {
    /** Número da página atual na resposta paginada */
    private Integer page;
    
    /** Número de registros por página definido na resposta */
    @JsonProperty("per_page")
    private Integer perPage;
    
    /** Número total de registros disponíveis na API */
    private Integer total;
    
    /** Número total de páginas disponíveis na API */
    @JsonProperty("total_pages")
    private Integer totalPages;
    
    /** Lista de usuários retornados na página atual */
    private List<User> data;
    
    /** Metadados adicionais de suporte fornecidos pela API */
    private Support support;
    
    /**
     * Classe interna representando informações de suporte da API
     * 
     * @implNote Implementa o padrão de design Value Object para
     * encapsular dados relacionados em uma única entidade coesa
     * 
     * @implSpec Classes internas estáticas são preferidas para modelos
     * que só fazem sentido no contexto da classe principal, mantendo
     * o código mais organizado e reduzindo proliferação de classes
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Support {
        /** URL para obter suporte ou mais informações */
        private String url;
        
        /** Mensagem informativa de suporte */
        private String text;
    }
} 