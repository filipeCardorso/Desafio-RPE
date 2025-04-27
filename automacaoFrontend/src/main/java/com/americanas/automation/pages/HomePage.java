package com.americanas.automation.pages;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Slf4j
@Component
public class HomePage extends BasePage {
    
    private static final String URL = "https://www.americanas.com.br/";
    
    @FindBy(css = "input[data-testid='fs-input']")
    private WebElement searchInput;
    
    @FindBy(css = "button[data-testid='fs-search-button']")
    private WebElement searchButton;

    @FindBy(css = "[data-testid='fs-navbar-header']")
    private WebElement headerContainer;

    @FindBy(css = "img[title='Americanas']")
    private WebElement logo;

    @FindBy(css = ".ButtonLogin_Container__sgzuk, a[href='/login']")
    private WebElement loginButton;

    @FindBy(css = "button[data-testid='cart-toggle']")
    private WebElement cartButton;
    
    @PostConstruct
    public void init() {
        initElements();
    }
    
    @Step("Acessar a home page da Americanas")
    public void goToHomePage() {
        navigateTo(URL);
        waitUtils.waitForPageLoad();
    }
    
    @Step("Pesquisar por: {searchTerm}")
    public void searchFor(String searchTerm) {
        waitUtils.waitForElementClickable(searchInput);
        searchInput.clear();
        searchInput.sendKeys(searchTerm);
        searchInput.sendKeys(Keys.ENTER);
        log.info("Pesquisando por: {}", searchTerm);
    }

    @Step("Validar elementos principais do header")
    public void validateHeaderElements() {
        waitUtils.waitForElementVisible(headerContainer);
        waitUtils.waitForElementVisible(logo);
        waitUtils.waitForElementVisible(searchInput);
        waitUtils.waitForElementVisible(searchButton);
        waitUtils.waitForElementVisible(loginButton);
        waitUtils.waitForElementVisible(cartButton);
    }
}