package com.reqres.automation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo de domínio representando um Usuário na API ReqRes
 * 
 * @implNote Utiliza o padrão de design DTO (Data Transfer Object) para
 * serialização/deserialização de dados entre a API e a aplicação
 * 
 * @implSpec Implementa anotações Jackson para mapeamento JSON flexível e
 * tolerante a novos campos (@JsonIgnoreProperties), bem como mapeamento de
 * propriedades com nomenclatura não-padrão (@JsonProperty)
 * 
 * @apiNote Suporta conversões entre diferentes convenções de nomenclatura
 * (camelCase, snake_case) através das anotações Jackson e métodos auxiliares
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    /** Identificador único do usuário */
    private Integer id;
    
    /** Email do usuário, utilizado como identificador de login */
    private String email;
    
    /** Nome do usuário, mapeado a partir do campo "first_name" no JSON */
    @JsonProperty("first_name")
    private String firstName;
    
    /**
     * Setter alternativo para nome que mapeia o campo "name" para firstName
     * 
     * @param name Nome a ser definido
     * @implNote Implementa estratégia de fallback para diferentes formatos de API,
     * permitindo compatibilidade com endpoints que usam "name" em vez de "first_name"
     */
    @JsonProperty("name")
    public void setNameAsFirstName(String name) {
        if (this.firstName == null) {
            this.firstName = name;
        }
    }
    
    /**
     * Obtém o nome completo do usuário, combinando firstName e lastName
     * 
     * @return Nome completo formatado ou apenas firstName se lastName for nulo
     * @implNote Este método facilita a integração com o framework Allure para
     * exibição consistente do nome de usuário em relatórios de teste
     */
    public String getName() {
        if (firstName == null) {
            return null;
        }
        return lastName != null ? firstName + " " + lastName : firstName;
    }
    
    /** Sobrenome do usuário, mapeado a partir do campo "last_name" no JSON */
    @JsonProperty("last_name")
    private String lastName;
    
    /** URL da imagem de avatar do usuário */
    private String avatar;
    
    /** Cargo ou função do usuário, utilizado em operações de criação e atualização */
    private String job;
    
    /** Timestamp de criação do registro no formato ISO-8601 */
    @JsonProperty("createdAt")
    private String createdAt;
    
    /** Timestamp da última atualização do registro no formato ISO-8601 */
    @JsonProperty("updatedAt")
    private String updatedAt;
} 