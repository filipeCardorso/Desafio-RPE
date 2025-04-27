package com.reqres.automation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada principal da aplicação de automação de testes
 * 
 * @implNote Implementada como uma aplicação Spring Boot para facilitar a
 * injeção de dependências e configuração de componentes de teste
 * 
 * @implSpec Utiliza o mecanismo de anotação @SpringBootApplication que combina:
 * <ul>
 *   <li>@Configuration: Define a classe como fonte de definições de beans</li>
 *   <li>@EnableAutoConfiguration: Adiciona automaticamente beans baseados no classpath</li>
 *   <li>@ComponentScan: Escaneia pacotes para localizar componentes marcados</li>
 * </ul>
 * 
 * @apiNote Embora seja uma aplicação de automação de testes, a estrutura Spring
 * oferece vantagens como gerenciamento de ciclo de vida, injeção de dependências
 * e manipulação de propriedades, facilitando a organização e manutenção dos testes
 */
@SpringBootApplication
public class Application {
    /**
     * Método principal que inicializa o contexto Spring
     * 
     * @param args Argumentos da linha de comando
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
} 