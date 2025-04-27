# Automação de Testes de API - ReqRes.in

![Java](https://img.shields.io/badge/Java-11-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7-green)
![Rest Assured](https://img.shields.io/badge/Rest%20Assured-5.1.1-blue)
![JUnit 5](https://img.shields.io/badge/JUnit-5.8.2-red)
![Allure](https://img.shields.io/badge/Allure-2.17.3-yellow)

Este projeto contém testes automatizados para a API RESTful do [ReqRes.in](https://reqres.in/), implementando princípios avançados de design de software e padrões de testabilidade.

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
- [Testes de Tratamento de Erros](#testes-de-tratamento-de-erros)
- [Validação e Asserções](#validação-e-asserções)
- [Troubleshooting](#troubleshooting)
- [Atualizações e Melhorias](#atualizações-e-melhorias)
- [Contribuição](#contribuição)
- [Boas Práticas](#boas-práticas)

## Sobre o Projeto

Este framework de automação de testes para APIs foi desenvolvido seguindo práticas modernas de desenvolvimento de software e testabilidade. O objetivo é demonstrar como aplicar conceitos avançados de programação orientada a objetos, SOLID, Clean Code e Clean Architecture em projetos de automação de testes.

### Tecnologias Utilizadas

- **Java 11**: Linguagem de programação robusta e amplamente adotada
- **Maven**: Gerenciamento de dependências e ciclo de vida do build
- **Spring Boot**: Framework para configuração, injeção de dependências e gerenciamento de contexto
- **Rest Assured**: Framework especializado para testes de API REST
- **JUnit 5**: Framework de testes com recursos avançados como testes parametrizados e extensões
- **Allure Report**: Framework para geração de relatórios detalhados e visualmente atrativos
- **Lombok**: Biblioteca para redução de código boilerplate (getters, setters, construtores)
- **AssertJ**: Biblioteca para asserções fluentes e legíveis
- **JSON Schema Validator**: Validação de contratos de API usando JSON Schema

## Princípios de Design

O projeto implementa os seguintes princípios de design e boas práticas:

### SOLID

- **S (Responsabilidade Única)**: Cada classe tem uma única responsabilidade (ex: `ApiErrorValidator` é responsável apenas pela validação de erros)
- **O (Aberto/Fechado)**: As classes são abertas para extensão, mas fechadas para modificação (ex: `ResponseValidator` pode ser estendida)
- **L (Substituição de Liskov)**: As implementações podem substituir suas interfaces base (ex: `RestResponseValidator` implementa `ResponseValidator`)
- **I (Segregação de Interfaces)**: Interfaces específicas são preferidas a uma interface geral (ex: separação entre `IAuthService` e `IUserService`)
- **D (Inversão de Dependência)**: Dependência de abstrações, não de concretizações (ex: injeção de `IApiService` em vez de `BaseApiService`)

### Clean Code

- Nomenclatura significativa e consistente
- Métodos pequenos com responsabilidade única
- Comentários úteis e documentação JavaDoc
- Testes como documentação viva
- Código formatado e consistente

### Clean Architecture

- Separação clara entre camadas (modelo, serviço, teste)
- Independência de frameworks
- Testabilidade como princípio fundamental
- Regras de negócio isoladas

## Arquitetura

```
┌───────────────────┐      ┌───────────────────┐      ┌───────────────────┐
│                   │      │                   │      │                   │
│   Test Classes    │─────▶│   API Services    │─────▶│   API Endpoints   │
│                   │      │                   │      │                   │
└───────────────────┘      └───────────────────┘      └───────────────────┘
         │                          │                           ▲
         │                          │                           │
         ▼                          ▼                           │
┌───────────────────┐      ┌───────────────────┐      ┌───────────────────┐
│                   │      │                   │      │                   │
│     Fixtures      │      │      Config       │─────▶│   Request Specs   │
│                   │      │                   │      │                   │
└───────────────────┘      └───────────────────┘      └───────────────────┘
         │                          ▲
         │                          │
         ▼                          │
┌───────────────────┐      ┌───────────────────┐      ┌───────────────────┐
│                   │      │                   │      │                   │
│    Validators     │◀─────│    Assertions     │◀─────│     Responses     │
│                   │      │                   │      │                   │
└───────────────────┘      └───────────────────┘      └───────────────────┘
```

## Estrutura do Projeto

O projeto segue uma arquitetura de automação de testes orientada a objetos, com as seguintes camadas:

- **Model**: Classes POJO que representam os objetos de negócio e respostas da API
  - Implementam domínio da aplicação
  - Utilizam Lombok para redução de boilerplate
  - Incluem anotações para serialização/deserialização JSON

- **Config**: Classes de configuração da aplicação e da API
  - Centralizam configurações e parâmetros
  - Utilizam Spring para gerenciamento de propriedades
  - Implementam padrão de injeção de dependências

- **Service**: Implementações de serviços para acesso à API
  - Abstraem a comunicação com endpoints específicos
  - Seguem o padrão de interface/implementação
  - Implementam logging e tratamento de erros

- **Tests**: Classes de teste para cada recurso da API
  - Organizadas por funcionalidade
  - Utilizam anotações Allure para documentação
  - Implementam padrão AAA (Arrange-Act-Assert)

- **Validators**: Classes para validação de respostas da API
  - Especializam-se em validações específicas
  - Implementam validações de esquema JSON
  - Fornecem mensagens de erro detalhadas

- **Fixtures**: Classes para criação de dados de teste
  - Encapsulam a criação de objetos de teste
  - Implementam padrão de fábrica para dados de teste
  - Permitem personalização dos dados

- **Assertions**: Classes para validação de dados de negócio
  - Fornecem validações específicas de domínio
  - Melhoram a legibilidade das validações
  - Encapsulam regras complexas de validação

- **Util**: Classes utilitárias para auxiliar nos testes
  - Implementam funcionalidades transversais
  - Incluem utilitários para logging e relatórios
  - Fornecem funções auxiliares comuns

## Pré-requisitos

Para executar este projeto, você precisará de:

- Java 11 ou superior
- Maven 3.6 ou superior
- Conexão com internet (para acessar a API ReqRes.in)
- IDE recomendada: IntelliJ IDEA ou Eclipse

## Configuração

### Autenticação

Este projeto foi configurado para usar autenticação via token JWT, que é obtido automaticamente por meio do endpoint de login da API ReqRes.in. As credenciais de login padrão estão definidas no arquivo `application.properties`:

```properties
api.base.url=https://reqres.in/api
api.users.endpoint=/users
api.auth.email=eve.holt@reqres.in
api.auth.password=cityslicka
api.auth.login.endpoint=/login
api.key=reqres-free-v1
```

### Configuração Personalizada

Você pode sobrescrever as configurações padrão criando um arquivo `application-local.properties` com suas próprias configurações. Este arquivo não é versionado, permitindo configurações personalizadas sem afetar o repositório.

## Execução dos Testes

### Executar Todos os Testes

Para executar todos os testes do projeto, utilize o comando:

```bash
mvn clean test
```

### Executar Categorias Específicas

Você pode executar categorias específicas de testes utilizando as tags JUnit:

```bash
mvn test -Dgroups="user-api"
mvn test -Dgroups="auth-api"
mvn test -Dgroups="error-handling"
```

### Executar Testes Específicos

Para executar classes de teste específicas:

```bash
mvn test -Dtest=UserApiTests
mvn test -Dtest=AuthApiTests
mvn test -Dtest=ImprovedApiErrorHandlingTest
```

### Modo de Execução Paralela

Os testes são configurados para execução paralela através das configurações em `junit-platform.properties`:

```properties
junit.jupiter.execution.parallel.enabled=true
junit.jupiter.execution.parallel.mode.default=concurrent
junit.jupiter.execution.parallel.mode.classes.default=concurrent
junit.jupiter.execution.parallel.config.strategy=dynamic
junit.jupiter.execution.parallel.config.dynamic.factor=0.5
```

## Relatórios

### Allure Reports

O projeto utiliza o Allure para geração de relatórios detalhados. Após a execução dos testes, gere o relatório com o comando:

```bash
mvn allure:report
```

O relatório será gerado no diretório `target/site/allure-maven-plugin/` e pode ser aberto em um navegador.

Para iniciar um servidor local e visualizar os relatórios interativamente:

```bash
mvn allure:serve
```

### Recursos do Relatório Allure

- **Dashboard**: Visão geral da execução dos testes
- **Suites**: Agrupamento hierárquico dos testes
- **Graphs**: Visualizações gráficas dos resultados
- **Timeline**: Execução dos testes ao longo do tempo
- **Behaviors**: Organização por histórias e recursos (épicos, features, stories)
- **Categories**: Categorização de falhas para análise

## Casos de Teste

O projeto implementa testes para os seguintes recursos da API ReqRes.in:

### Usuários (UserApiTests)
- GET /users - Listar todos os usuários
  - Valida paginação, dados dos usuários, estrutura da resposta
- GET /users/{id} - Obter um usuário específico
  - Valida dados do usuário, estrutura da resposta
- POST /users - Criar um novo usuário
  - Valida criação, ID gerado, timestamps
- PUT /users/{id} - Atualizar um usuário (completo)
  - Valida atualização completa, timestamp de atualização
- PATCH /users/{id} - Atualizar um usuário (parcial)
  - Valida atualização parcial, timestamp de atualização
- DELETE /users/{id} - Remover um usuário
  - Valida remoção, código de status

### Autenticação (AuthApiTests)
- POST /register - Registrar um novo usuário
  - Valida registro bem-sucedido, token gerado
  - Valida cenários de erro (email ausente, senha ausente)
- POST /login - Login de usuário
  - Valida login bem-sucedido, token gerado
  - Valida cenários de erro (credenciais inválidas)

## Testes de Tratamento de Erros

A classe `ImprovedApiErrorHandlingTest` implementa testes abrangentes para validação de erros, organizados nas seguintes categorias:

### Validação de API Key
- Teste de API Key ausente
- Teste de API Key inválida

### Validação de Autenticação
- Teste de credenciais inválidas
- Testes parametrizados para campos de autenticação inválidos

### Validação de Formato de Requisição
- Teste de formato JSON inválido
- Teste de Content-Type inválido

### Validação de Recursos
- Teste de recurso não encontrado (404)

### Validação de Métodos HTTP
- Teste de método não permitido (405)

### Validação de Erros de Servidor
- Teste de simulação de erro interno

## Validação e Asserções

O projeto implementa várias estratégias de validação:

### Validadores de Resposta
- `ResponseValidator`: Interface para validação de respostas
- `RestResponseValidator`: Implementação para validação REST
- `ApiErrorValidator`: Especializado em validação de erros
- `ResponseSchemaValidator`: Validação de esquemas JSON

### Asserções de Domínio
- `AuthAssertions`: Asserções específicas para autenticação
- `UserAssertions`: Asserções específicas para usuários

### Validação de Esquema
Validação de contratos da API usando JSON Schema:
- `/schemas/auth-error-schema.json`: Esquema para erros de autenticação

## Troubleshooting

### Erro de autenticação
Se os testes falharem com erro de autenticação, verifique:

1. As credenciais no arquivo `application.properties` estão corretas
2. O endpoint de login está respondendo corretamente
3. O formato do token está sendo incluído corretamente nos cabeçalhos
4. A chave de API está configurada corretamente

### Erro de compilação
Para resolver erros de compilação:

1. Verifique se o JDK 11 está configurado corretamente
2. Execute `mvn clean compile` para verificar erros específicos
3. Verifique se as anotações Lombok estão sendo processadas corretamente

### Erro na execução dos testes
Se ocorrer erros durante a execução dos testes:

1. Limpar as dependências do Maven:
   ```bash
   mvn dependency:purge-local-repository
   ```

2. Reconstruir o projeto:
   ```bash
   mvn clean install -DskipTests
   ```

3. Executar os testes com log detalhado:
   ```bash
   mvn test -Dorg.slf4j.simpleLogger.defaultLogLevel=DEBUG
   ```

### Erro ao gerar relatórios Allure
Se ocorrer problemas ao gerar os relatórios Allure:

1. Verifique se os testes foram executados e geraram resultados
2. Certifique-se que o diretório `target/allure-results` existe
3. Se necessário, instale o Allure CLI localmente:
   ```bash
   npm install -g allure-commandline
   ```

## Atualizações e Melhorias

### Análise e Atualização (2025-04-25)

#### Problemas Identificados

Durante a análise e execução do projeto, foram identificados:

1. **Mudanças na API ReqRes.in**: A API agora exige uma chave de API para acesso, retornando erro 401 quando não fornecida.
2. **Dependências desatualizadas**: Algumas dependências do projeto estavam desatualizadas, causando incompatibilidades.
3. **Tratamento de erros limitado**: Os testes de erro não cobriam todos os cenários possíveis.

#### Solução Implementada

1. **Configuração da Chave de API**:
   - Adicionada propriedade `api.key=reqres-free-v1` no arquivo `application.properties`
   - Atualizado `ApiConfig.java` para incluir o getter da chave de API
   - Modificada estrutura de requisição para incluir o header `X-API-KEY` em todas as requisições

2. **Otimização do Código**:
   - Removidos arquivos de teste redundantes
   - Eliminadas dependências desnecessárias do pom.xml
   - Refatoração para seguir princípios SOLID

3. **Aprimoramento dos Testes de Validação de Erro**:
   - Substituída classe `ApiErrorHandlingTest.java` pela versão melhorada `ImprovedApiErrorHandlingTest.java`
   - Implementação de validações específicas para diversos tipos de erros

### Melhorias nos Testes de Tratamento de Erro (2025-04-26)

A versão melhorada dos testes de tratamento de erro inclui:

1. **Organização estruturada dos testes**:
   - Uso de classes aninhadas (`@Nested`) para agrupar testes por categoria
   - Cada categoria de teste focada em um tipo específico de erro

2. **Testes parametrizados**:
   - Implementação de testes parametrizados para validar múltiplos cenários
   - Redução de duplicação de código e aumento da cobertura
   - Maior manutenibilidade através da centralização de casos de teste

3. **Validações especializadas**:
   - Criação do `ApiErrorValidator` dedicado à validação de respostas de erro
   - Implementação de validações específicas para cada tipo de erro
   - Implementação do `ResponseSchemaValidator` para validação de esquemas JSON

4. **Documentação avançada**:
   - Anotações Allure para relatórios detalhados
   - Documentação JavaDoc completa
   - Comentários explicativos em seções complexas

5. **Flexibilidade e robustez**:
   - Testes tolerantes a variações na implementação da API
   - Validações adaptativas para diferentes comportamentos da API
   - Melhor isolamento dos testes para evitar interdependências

## Contribuição

Para contribuir com este projeto:

1. Faça um fork do repositório
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Implemente suas alterações
4. Adicione testes para sua implementação
5. Verifique se todos os testes estão passando (`mvn test`)
6. Faça commit das alterações (`git commit -m 'Adiciona nova feature'`)
7. Envie para o branch (`git push origin feature/nova-feature`)
8. Abra um Pull Request

### Diretrizes de Contribuição

- Mantenha o código limpo e bem documentado
- Siga os padrões de codificação existentes
- Adicione testes para todas as novas funcionalidades
- Atualize a documentação quando necessário
- Use commits semânticos

## Boas Práticas

### Padrões de Design Implementados

O projeto utiliza diversos padrões de design:

- **Factory Method**: Criação de objetos em fixtures
- **Builder**: Construção fluente de especificações de requisição
- **Strategy**: Diferentes estratégias de validação
- **Template Method**: Framework base para testes
- **Dependency Injection**: Injeção via Spring
- **Facade**: Simplificação do acesso à API

### Práticas de Teste

- **AAA (Arrange-Act-Assert)**: Estrutura clara dos testes
- **Given-When-Then**: Comentários seguindo abordagem BDD
- **Test Data Builders**: Construção fluente de dados de teste
- **Test Doubles**: Uso de mocks quando apropriado
- **Testes Isolados**: Independência entre testes
- **Testes Parametrizados**: Validação de múltiplos cenários

### Convenções de Código

- **Nomenclatura**: CamelCase para classes, métodos e variáveis
- **Organização de Pacotes**: Por funcionalidade e camada
- **Comentários**: JavaDoc para documentação pública
- **Constantes**: Uso de constantes para valores fixos
- **Logging**: Uso de framework de logging apropriado

---

## Detalhamento da Estrutura do Código

```
src
├── main
│   └── java
│       └── com
│           └── reqres
│               └── automation
│                   ├── Application.java             # Classe principal Spring Boot
│                   ├── config
│                   │   └── ApiConfig.java           # Configurações da API
│                   ├── model
│                   │   ├── User.java                # Modelo de Usuário
│                   │   └── UserListResponse.java    # Resposta de lista de usuários
│                   ├── service
│                   │   ├── IApiService.java         # Interface de serviço de API base
│                   │   ├── BaseApiService.java      # Implementação base de serviço de API
│                   │   ├── IAuthService.java        # Interface de serviço de autenticação
│                   │   ├── AuthServiceImpl.java     # Implementação do serviço de autenticação
│                   │   ├── IUserService.java        # Interface de serviço de usuários
│                   │   └── UserServiceImpl.java     # Implementação do serviço de usuários
│                   └── util
│                       └── ReportManager.java       # Utilitário para geração de relatórios
└── test
    ├── java
    │   └── com
    │       └── reqres
    │           └── automation
    │               ├── assertions
    │               │   ├── AuthAssertions.java      # Asserções específicas para autenticação
    │               │   └── UserAssertions.java      # Asserções específicas para usuários
    │               ├── fixtures
    │               │   ├── AuthFixture.java         # Criação de dados para testes de autenticação
    │               │   └── UserFixture.java         # Criação de dados para testes de usuários
    │               ├── tests
    │               │   ├── AbstractApiTest.java     # Classe base para todos os testes
    │               │   ├── AuthApiTests.java        # Testes de API de autenticação
    │               │   ├── UserApiTests.java        # Testes de API de usuários
    │               │   └── ImprovedApiErrorHandlingTest.java # Testes avançados de tratamento de erros
    │               └── validators
    │                   ├── ResponseValidator.java    # Interface para validação de respostas
    │                   ├── RestResponseValidator.java # Implementação de validador de respostas
    │                   ├── ApiErrorValidator.java    # Validador especializado em erros
    │                   └── ResponseSchemaValidator.java # Validador de esquemas JSON
    └── resources
        ├── application.properties     # Propriedades de configuração
        ├── junit-platform.properties  # Configurações do JUnit
        └── schemas
            └── auth-error-schema.json # Esquema para validação de erros
``` 