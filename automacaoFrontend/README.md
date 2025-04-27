# Automação de Testes Frontend - Americanas

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-green)
![Selenium](https://img.shields.io/badge/Selenium-4.8-blue)
![Cucumber](https://img.shields.io/badge/Cucumber-BDD-yellow)
![Allure](https://img.shields.io/badge/Allure-Report-red)

Este projeto realiza automação de testes de interface para o site da Americanas utilizando Java, Selenium WebDriver, Cucumber (BDD), Spring Boot e Allure Reports. O objetivo é validar funcionalidades de busca, filtros e exibição de produtos, garantindo a qualidade da experiência do usuário.

## Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Princípios de Design](#princípios-de-design)
- [Arquitetura](#arquitetura)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Pré-requisitos](#pré-requisitos)
- [Configuração](#configuração)
- [Execução dos Testes](#execução-dos-testes)
- [Relatórios](#relatórios)
- [Casos de Teste](#casos-de-teste)
- [Page Objects](#page-objects)
- [Criação de Novos Cenários](#criação-de-novos-cenários)
- [Troubleshooting](#troubleshooting)
- [Boas Práticas](#boas-práticas)
- [Contribuição](#contribuição)
- [Licença](#licença)

## Sobre o Projeto

Este framework de automação de testes frontend foi desenvolvido para garantir a qualidade e confiabilidade do site da Americanas. O projeto implementa testes automatizados para validar fluxos críticos de usuário, como busca de produtos, aplicação de filtros e validação de resultados.

### Tecnologias Utilizadas

- **Java 17**: Linguagem de programação principal
- **Maven**: Gerenciamento de dependências e build
- **Selenium WebDriver**: Automação de browser
- **Cucumber (Gherkin)**: Framework BDD para escrita de cenários de teste
- **Spring Boot**: Framework para configuração e injeção de dependências
- **Allure Reports**: Geração de relatórios de teste

## Princípios de Design

O projeto segue princípios modernos de desenvolvimento:

### Page Object Model (POM)
- Abstração das páginas web em classes Java
- Encapsulamento dos elementos e ações da interface
- Reutilização de código e manutenibilidade

### Behavior Driven Development (BDD)
- Cenários escritos em linguagem natural (Gherkin)
- Colaboração entre desenvolvedores, QA e stakeholders
- Documentação viva dos testes

### Clean Code
- Código legível e auto-explicativo
- Métodos pequenos com responsabilidade única
- Nomenclatura clara e consistente

## Arquitetura

```
                                       ┌──────────────────┐
                                       │    Features      │
                                       │   (Gherkin)      │
                                       └────────┬─────────┘
                                                │
                                                ▼
                         ┌───────────────────────────────────────────┐
                         │                 Step Definitions          │
                         │           (SmartTVSearchSteps)            │
                         └───────────────────────┬───────────────────┘
                                                 │
                     ┌───────────────┬──────────┴───────────┬───────────────┐
                     │               │                      │               │
                     ▼               ▼                      ▼               ▼
            ┌────────────┐   ┌────────────────┐   ┌─────────────────┐   ┌───────────┐
            │  HomePage  │   │ SearchResults  │   │    WaitUtils    │   │  Models   │
            │            │   │     Page       │   │                 │   │           │
            └────────────┘   └────────────────┘   └─────────────────┘   └───────────┘
                     │               │                      │               │
                     └───────────────┼──────────────────────┼───────────────┘
                                     │                      │
                                     ▼                      ▼
                            ┌─────────────────┐   ┌─────────────────┐
                            │   WebDriver     │   │  Spring Boot    │
                            │   Config        │   │   Context       │
                            └─────────────────┘   └─────────────────┘
```

## Estrutura do Projeto

```
src
├── main
│   ├── java
│   │   └── com
│   │       └── americanas
│   │           └── automation
│   │               ├── config
│   │               │   ├── CucumberSpringConfiguration.java
│   │               │   └── WebDriverConfig.java
│   │               ├── models
│   │               │   └── ProductInfo.java
│   │               ├── pages
│   │               │   ├── BasePage.java
│   │               │   ├── HomePage.java
│   │               │   └── SearchResultsPage.java
│   │               ├── utils
│   │               │   └── WaitUtils.java
│   │               └── Application.java
│   └── resources
│       └── application.properties
└── test
    ├── java
    │   └── com
    │       └── americanas
    │           └── automation
    │               ├── runners
    │               │   └── TestRunner.java
    │               └── steps
    │                   └── SmartTVSearchSteps.java
    └── resources
        └── features
            └── smart_tv_search.feature
```

### Descrição das Camadas

- **config**: Configurações do WebDriver e integração Spring-Cucumber
- **models**: Modelos de dados utilizados nos testes
- **pages**: Implementação do Page Object Pattern
- **utils**: Utilitários e helpers, como funções de espera
- **steps**: Definições dos passos Cucumber
- **features**: Cenários de teste em formato Gherkin

## Pré-requisitos

- Java 17 ou superior
- Maven 3.6 ou superior
- Google Chrome instalado (versão compatível com chromedriver)
- Sistema operacional Linux (ou ajuste os caminhos no código para seu SO)
- Conexão com internet para acessar o site da Americanas

## Configuração

### Configuração Básica

O arquivo `application.properties` contém as configurações principais:

```properties
# Configurações de URL
base.url=https://www.americanas.com.br/

# Configurações de tempo de espera
webdriver.wait.timeout=15

# Configurações de filtro de preço
price.filter.min=2500
price.filter.max=5000
price.filter.expected=3500

# Configurações do WebDriver
webdriver.browser=chrome
webdriver.driver.path=/path/to/chromedriver
webdriver.implicitly.wait=10
webdriver.page.load.timeout=30

# Configurações de execução
parallel.execution=false
webdriver.headless=false
webdriver.window.size=1920x1080
webdriver.language=pt-BR
```

### Configuração Personalizada

Você pode sobrescrever as configurações padrão criando um arquivo `application-local.properties` com suas configurações específicas.

## Execução dos Testes

### Executar Todos os Testes

```bash
mvn clean test
```

### Executar Cenários Específicos

Usando tags Cucumber:

```bash
mvn test -Dcucumber.filter.tags="@smart-tv"
```

### Executar em Modo Headless

```bash
mvn test -Dwebdriver.headless=true
```

### Executar com Paralelismo

```bash
mvn test -Dparallel.execution=true
```

## Relatórios

### Allure Reports

Após a execução dos testes, gere o relatório com:

```bash
mvn allure:serve
```

O relatório será aberto automaticamente no navegador padrão.

### Recursos do Relatório

- **Overview**: Visão geral da execução
- **Suites**: Agrupamento por features
- **Graphs**: Visualizações gráficas
- **Timeline**: Linha do tempo de execução
- **Behaviors**: Organização por funcionalidades
- **Categories**: Categorização de falhas

## Casos de Teste

### Funcionalidade: Busca de Smart TV

A feature `smart_tv_search.feature` implementa os seguintes cenários:

1. **Busca com sucesso**
   - Navega para homepage
   - Realiza busca por "smart tv"
   - Valida resultados exibidos

2. **Aplicação de filtro de preço**
   - Aplica filtro de preço (R$ 2500 - R$ 5000)
   - Valida produtos dentro da faixa

3. **Ordenação por maior preço**
   - Ordena resultados por maior preço
   - Valida ordenação descendente

## Page Objects

### BasePage
- Classe base para todos os Page Objects
- Implementa métodos comuns
- Gerencia WebDriver e esperas

### HomePage
- Representa a página inicial
- Métodos para busca de produtos
- Navegação para outras páginas

### SearchResultsPage
- Representa página de resultados
- Métodos para filtros e ordenação
- Validação de produtos exibidos

## Criação de Novos Cenários

### 1. Criar Feature

Crie um arquivo `.feature` em `src/test/resources/features/`:

```gherkin
@new-feature
Feature: Nova Funcionalidade

  Scenario: Novo cenário
    Given que estou na página inicial
    When realizo uma ação específica
    Then valido o resultado esperado
```

### 2. Implementar Steps

Crie uma classe de steps em `src/test/java/.../steps/`:

```java
public class NovoSteps {
    @Given("que estou na página inicial")
    public void navegarParaPaginaInicial() {
        // Implementação
    }
}
```

### 3. Criar Page Objects (se necessário)

Implemente novos Page Objects seguindo o padrão existente.

## Troubleshooting

### Problemas Comuns

1. **Erro de WebDriver**
   - Verifique se o Chrome está instalado
   - Certifique-se que o chromedriver é compatível
   - Ajuste o caminho do driver no properties

2. **Timeouts**
   - Aumente o `webdriver.wait.timeout`
   - Verifique a conexão com internet
   - Use esperas explícitas adequadas

3. **Elementos não encontrados**
   - Verifique se os seletores estão corretos
   - O site pode ter mudado a estrutura
   - Use esperas adequadas

4. **Problemas de contexto Spring**
   - Verifique anotações de configuração
   - Certifique-se que as dependências estão corretas

## Boas Práticas

### Escrita de Testes
- Use cenários independentes
- Evite dados hardcoded
- Mantenha steps reutilizáveis
- Documente comportamentos complexos

### Page Objects
- Um método por ação
- Retorne novos Page Objects
- Use esperas explícitas
- Mantenha seletores atualizados

### Manutenção
- Revise seletores regularmente
- Mantenha features atualizadas
- Refatore código duplicado
- Documente mudanças importantes

## Contribuição

1. Faça um fork do repositório
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

### Diretrizes de Contribuição

- Siga os padrões de código existentes
- Adicione testes para novas funcionalidades
- Atualize a documentação quando necessário
- Use commits semânticos

## Licença

Este projeto é open-source e está sob a licença MIT.