package com.americanas.automation.steps;

import com.americanas.automation.models.ProductInfo;
import com.americanas.automation.pages.HomePage;
import com.americanas.automation.pages.SearchResultsPage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.junit.jupiter.api.Assertions;

import java.io.ByteArrayInputStream;
import java.util.List;

public class SmartTVSearchSteps {
    
    @Autowired
    private HomePage homePage;
    
    @Autowired
    private SearchResultsPage searchResultsPage;
    
    @Autowired
    private WebDriver driver;
    
    @Value("${price.filter.expected}")
    private double expectedPrice;
    
    @Before
    public void setup() {
        driver.manage().window().maximize();
    }
    
    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    @Dado("que acesso o site da Americanas")
    public void queAcessoOSiteDaAmericanas() {
        homePage.goToHomePage();
        homePage.validateHeaderElements();
        takeScreenshot("Página inicial");
    }
    
    @Quando("digito {string} no campo de busca")
    public void digitoNoCampoDeBusca(String termo) {
        homePage.searchFor(termo);
        takeScreenshot("Busca realizada");
    }
    
    @E("filtro o preço por R$ 2.500 - R$ 5.000")
    public void filtroOPrecoPor() {
        searchResultsPage.applyPriceFilter();
        takeScreenshot("Filtro aplicado");
    }
    
    @Então("imprimo as informações dos produtos com valor maior que R$3500")
    public void imprimoAsInformacoesDosProutos() {
        List<ProductInfo> products = searchResultsPage.getProductsAbovePrice(expectedPrice);
        
        System.out.println("\n=== PRODUTOS COM VALOR ACIMA DE R$" + expectedPrice + " ===\n");
        
        for (ProductInfo product : products) {
            System.out.println(product.toString());
            System.out.println("----------------------------------------");
        }
        
        takeScreenshot("Produtos filtrados");
        
        // Validação: lista não pode estar vazia
        Assertions.assertFalse(products.isEmpty(), "Nenhum produto encontrado acima do preço esperado!");
        
        // Validação: todos os produtos devem ter preço acima do esperado
        boolean allAbove = products.stream().allMatch(p -> p.getPrice() > expectedPrice);
        Assertions.assertTrue(allAbove, "Existe produto com preço menor ou igual ao valor esperado!");
    }
    
    @Então("o grid de produtos deve ser exibido")
    public void oGridDeProdutosDeveSerExibido() {
        searchResultsPage.validateProductGridVisible();
        searchResultsPage.validateProductsInGrid();
        takeScreenshot("Grid de produtos");
    }

    @Então("o filtro de preço deve estar aplicado")
    public void oFiltroDePrecoDeveEstarAplicado() {
        searchResultsPage.validatePriceFilterApplied();
        takeScreenshot("Filtro de preço aplicado");
    }
    
    private void takeScreenshot(String name) {
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        Allure.addAttachment(name, new ByteArrayInputStream(screenshot));
    }
}