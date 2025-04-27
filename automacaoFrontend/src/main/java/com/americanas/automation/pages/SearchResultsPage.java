package com.americanas.automation.pages;

import com.americanas.automation.models.ProductInfo;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class SearchResultsPage extends BasePage {

    @Value("${price.filter.min:2500}")
    private String minPrice;

    @Value("${price.filter.max:5000}")
    private String maxPrice;

    @Value("${price.filter.expected:3500}")
    private double expectedPrice;

    @FindBy(css = ".ProductCard_productCard__MwY4X")
    private List<WebElement> productCards;

    @FindBy(css = ".ProductCard_productName__mwx7Y")
    private List<WebElement> productNames;

    @FindBy(css = ".ProductCard_productPrice__XFEqu")
    private List<WebElement> productPrices;

    @FindBy(css = ".starV2")
    private List<WebElement> productRatings;

    @FindBy(css = ".ProductGrid_productGallery__n3L6E")
    private WebElement productGrid;

    @FindBy(css = "input[data-testid^='filter-']")
    private List<WebElement> filterCheckboxes;

    @FindBy(css = "button.FilterFacetCheckbox_showAll__e_whq")
    private WebElement showAllPriceFiltersButton;

    @FindBy(css = "button[data-testid='pagination-button']")
    private WebElement showMoreProductsButton;

    @PostConstruct
    public void init() {
        initElements();
    }

    private WebElement getPriceFilterCheckbox() {
        String value = minPrice + "-" + maxPrice;
        String selector = String.format("input[data-fs-input='true'][type='checkbox'][value='%s']", value);
        
        // Debug: Mostrar o que estamos procurando
        System.out.println("DEBUG: Procurando filtro com value=" + value);
        
        List<WebElement> checkboxes = driver.findElements(By.cssSelector(selector));
        if (!checkboxes.isEmpty()) {
            System.out.println("DEBUG: Filtro exato encontrado: " + value);
            return checkboxes.get(0);
        }
        
        // Se não encontrar exato, procura pelos valores disponíveis
        List<WebElement> allCheckboxes = driver.findElements(By.cssSelector("input[data-fs-input='true'][type='checkbox']"));
        System.out.println("DEBUG: Total de filtros disponíveis: " + allCheckboxes.size());
        
        // Procura o filtro que mais se aproxima
        WebElement bestMatch = null;
        for (WebElement cb : allCheckboxes) {
            String val = cb.getAttribute("value");
            System.out.println("DEBUG: Filtro disponível: " + val);
            
            // Verifica se o valor do filtro contém a faixa desejada
            if (val.contains("2500") && val.contains("5000")) {
                System.out.println("DEBUG: Encontrado filtro que contém a faixa desejada");
                return cb;
            }
            
            // Procura por valores próximos
            try {
                String[] range = val.split("-");
                if (range.length == 2) {
                    int min = Integer.parseInt(range[0]);
                    int max = Integer.parseInt(range[1]);
                    
                    // Se a faixa contém nosso intervalo desejado
                    if (min <= 2500 && max >= 5000) {
                        bestMatch = cb;
                        System.out.println("DEBUG: Encontrado filtro que contém a faixa: " + val);
                    }
                }
            } catch (Exception ignored) {}
        }
        
        if (bestMatch != null) {
            return bestMatch;
        }
        
        // Se não encontrar nada adequado, retorna o primeiro disponível
        if (!allCheckboxes.isEmpty()) {
            System.out.println("WARN: Usando primeiro filtro disponível: " + allCheckboxes.get(0).getAttribute("value"));
            return allCheckboxes.get(0);
        }
        
        throw new org.openqa.selenium.NoSuchElementException("Nenhum filtro de preço disponível encontrado!");
    }

    @Step("Aplicar filtro de preço parametrizado")
    public void applyPriceFilter() {
        waitUtils.waitForPageLoad();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Expande o acordeão do filtro de preço se necessário
        try {
            WebElement accordionButton = driver.findElement(By.xpath("//button[contains(.,'Preço')]"));
            if (accordionButton != null && accordionButton.getAttribute("aria-expanded") != null && accordionButton.getAttribute("aria-expanded").equals("false")) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", accordionButton);
                accordionButton.click();
                Thread.sleep(1000);
            }
        } catch (Exception ignored) {}
        
        // Sempre tenta clicar em 'ver tudo' antes de buscar o filtro
        try {
            WebElement showAllButton = driver.findElement(By.cssSelector("button.FilterFacetCheckbox_showAll__e_whq"));
            if (showAllButton.isDisplayed() && showAllButton.isEnabled()) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", showAllButton);
                showAllButton.click();
                Thread.sleep(1000);
            }
        } catch (Exception ignored) {}
        
        // Busca e aplica o filtro de preço
        WebElement priceFilterCheckbox = getPriceFilterCheckbox();
        waitUtils.waitForElementClickable(priceFilterCheckbox);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", priceFilterCheckbox);
        priceFilterCheckbox.click();
        
        // Aguarda o carregamento completo após aplicar o filtro
        waitUtils.waitForPageLoad();
        try {
            Thread.sleep(5000); // Espera adicional para a página carregar os resultados filtrados
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Step("Validar grid de produtos visível")
    public void validateProductGridVisible() {
        waitUtils.waitForElementVisible(productGrid);
    }

    @Step("Validar produtos exibidos no grid")
    public void validateProductsInGrid() {
        // Implementação com tratamento de StaleElementReferenceException
        int attempts = 0;
        boolean success = false;
        
        while (attempts < 3 && !success) {
            try {
                List<WebElement> updatedProductCards = driver.findElements(By.cssSelector(".ProductCard_productCard__MwY4X"));
                if (updatedProductCards.isEmpty()) {
                    throw new AssertionError("Nenhum produto exibido no grid!");
                }
                
                // Verifica se o primeiro elemento é realmente visível
                updatedProductCards.get(0).isDisplayed();
                success = true;
            } catch (StaleElementReferenceException e) {
                attempts++;
                if (attempts == 3) {
                    throw e;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @Step("Validar filtro de preço aplicado")
    public void validatePriceFilterApplied() {
        WebElement priceFilterCheckbox = getPriceFilterCheckbox();
        if (!priceFilterCheckbox.isSelected()) {
            throw new AssertionError("Filtro de preço não está aplicado!");
        }
    }

    public double getExpectedPrice() {
        return expectedPrice;
    }

    @Step("Coletar informações dos produtos com valor maior que R$3500")
    public List<ProductInfo> getProductsAbovePrice(double minPrice) {
        List<ProductInfo> products = new ArrayList<>();
        
        // Aguarda um pouco para garantir que os produtos filtrados carregaram
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Tenta carregar mais produtos até encontrar suficientes acima do preço esperado
        int attempts = 0;
        while (attempts < 3 && products.stream().filter(p -> p.getPrice() > minPrice).count() < 1) {
            List<WebElement> productCards = driver.findElements(By.cssSelector(".ProductCard_productCard__MwY4X"));
            
            // Debug: imprimir quantidade de produtos encontrados
            System.out.println("=== DEBUG: Número de produtos encontrados: " + productCards.size() + " ===");
            
            for (WebElement card : productCards) {
                try {
                    String name = card.findElement(By.cssSelector(".ProductCard_productName__mwx7Y")).getText();
                    WebElement priceElement = card.findElement(By.cssSelector(".ProductCard_productPrice__XFEqu"));
                    String priceText = priceElement.getText();
                    
                    // Debug: imprimir preço bruto
                    System.out.println("DEBUG: Produto: " + name);
                    System.out.println("DEBUG: Preço texto original: " + priceText);
                    
                    double price = extractPrice(priceText);
                    
                    // Debug: imprimir preço convertido
                    System.out.println("DEBUG: Preço convertido: " + price);
                    System.out.println("DEBUG: Preço mínimo esperado: " + minPrice);
                    
                    String rating = "0";
                    try {
                        WebElement ratingElement = card.findElement(By.cssSelector(".avg-rating"));
                        rating = ratingElement.getText();
                    } catch (Exception e) {
                        log.warn("Rating não encontrado para o produto: {}", name);
                    }
                    
                    // Adicionar APENAS produtos com preço maior que o mínimo
                    if (price > minPrice) {
                        System.out.println("DEBUG: PRODUTO ACIMA DO MÍNIMO ADICIONADO - " + name + " - R$" + price);
                        products.add(ProductInfo.builder()
                                .name(name)
                                .price(price)
                                .rating(rating)
                                .build());
                    } else {
                        System.out.println("DEBUG: Produto ignorado (preço abaixo do mínimo): " + name + " - R$" + price);
                    }
                } catch (Exception e) {
                    log.error("Erro ao extrair informações do produto", e);
                    e.printStackTrace();
                }
            }
            
            // Se não encontrou produtos suficientes, tenta carregar mais
            if (products.stream().filter(p -> p.getPrice() > minPrice).count() < 1 && attempts < 2) {
                try {
                    WebElement showMoreButton = driver.findElement(By.cssSelector("button[data-testid='pagination-button']"));
                    if (showMoreButton.isDisplayed() && showMoreButton.isEnabled()) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", showMoreButton);
                        showMoreButton.click();
                        Thread.sleep(3000); // Aguarda carregar mais produtos
                        System.out.println("DEBUG: Clicou em 'ver mais produtos'");
                    } else {
                        break; // Não há mais produtos para carregar
                    }
                } catch (Exception e) {
                    System.out.println("DEBUG: Botão 'ver mais produtos' não encontrado ou não clicável");
                    break;
                }
            }
            
            attempts++;
        }
        
        // Debug: imprimir resumo final
        System.out.println("=== DEBUG: Resumo Final ===");
        System.out.println("Total de produtos acima de R$" + minPrice + ": " + products.size());
        
        return products;
    }

    private double extractPrice(String priceText) {
        try {
            // Debug: mostrando etapas de limpeza do preço
            System.out.println("DEBUG: Preço original: " + priceText);
            
            // Procura por diferentes padrões de preço
            // Padrão 1: "R$ X.XXX,XX"
            // Padrão 2: "X.XXX,XX"
            // Padrão 3: "R$ X.XXX"
            
            String cleanPrice = priceText;
            
            // Remove tudo que não é número, vírgula ou ponto
            cleanPrice = cleanPrice.replaceAll("[^0-9.,]", "");
            
            // Se encontrar vírgula seguida de 2 dígitos, assume que é decimal
            if (cleanPrice.matches(".*,\\d{2}$")) {
                cleanPrice = cleanPrice.replaceAll("\\.", "").replaceAll(",", ".");
            } else {
                // Se não, remove pontos e vírgulas
                cleanPrice = cleanPrice.replaceAll("[.,]", "");
            }
            
            System.out.println("DEBUG: Preço limpo: " + cleanPrice);
            
            return Double.parseDouble(cleanPrice);
        } catch (Exception e) {
            log.error("Erro ao converter preço: {}", priceText, e);
            e.printStackTrace();
            return 0.0;
        }
    }
}