# Automação de Testes Frontend - Americanas

Este projeto realiza automação de testes de interface para o site da Americanas utilizando Java, Selenium WebDriver, Cucumber (BDD), Spring Boot e Allure Reports. O objetivo é validar funcionalidades de busca, filtros e exibição de produtos, garantindo a qualidade da experiência do usuário.

## 🛠 Tecnologias Utilizadas
- **Java 17**
- **Maven**
- **Selenium WebDriver**
- **Cucumber (Gherkin)**
- **Spring Boot**
- **Allure Reports**

## 📁 Estrutura do Projeto
```
pom.xml
README.md
src/
  main/java/com/americanas/automation/...
  main/resources/application.properties
  test/java/com/americanas/automation/...
  test/resources/features/smart_tv_search.feature
```
- **pages/**: Page Objects (HomePage, SearchResultsPage, BasePage)
- **steps/**: Steps do Cucumber (SmartTVSearchSteps)
- **config/**: Configurações do WebDriver e integração Cucumber+Spring
- **utils/**: Utilitários de espera (WaitUtils)
- **models/**: Modelos de dados (ProductInfo)
- **features/**: Cenários de teste em Gherkin

## ⚙️ Pré-requisitos
- Java 17+
- Maven 3.6+
- Google Chrome instalado
- Sistema operacional Linux (ou ajuste para seu SO)

## 🚀 Instalação e Execução
1. **Clone o repositório:**
   ```
   git clone <seu-repo>
   cd automacaoFrontend
   ```
2. **Instale as dependências:**
   ```
   mvn clean install
   ```
3. **Execute os testes:**
   ```
   mvn test
   ```
4. **(Opcional) Gere o relatório Allure:**
   ```
   mvn allure:serve
   ```

## 📝 Configuração
Edite o arquivo `src/main/resources/application.properties` para ajustar:
- URL base do sistema
- Timeout de espera do Selenium
- Parâmetros de filtro de preço
- Configuração de browser, paralelismo, idioma, etc.

Exemplo:
```
base.url=https://www.americanas.com.br/
webdriver.wait.timeout=15
price.filter.min=2500
price.filter.max=5000
price.filter.expected=3500
webdriver.browser=chrome
```

## 🧪 Como criar novos cenários
- Adicione arquivos `.feature` em `src/test/resources/features/` usando Gherkin.
- Implemente os steps correspondentes em `steps/`.
- Utilize os Page Objects para interações reutilizáveis.

## 🐞 Troubleshooting
- **Erro de WebDriver:** Certifique-se de que o Chrome está instalado e compatível com a versão do chromedriver.
- **Timeouts:** Ajuste o `webdriver.wait.timeout` no `application.properties`.
- **Elementos não encontrados:** O frontend pode ter mudado. Ajuste os seletores nos Page Objects.
- **Problemas de contexto Spring:** Verifique se as anotações de configuração estão corretas.

## 🤝 Contribuição
Pull requests são bem-vindos! Para grandes mudanças, abra uma issue primeiro para discutir o que você gostaria de modificar.

## 📄 Licença
Este projeto é open-source e está sob a licença MIT.
