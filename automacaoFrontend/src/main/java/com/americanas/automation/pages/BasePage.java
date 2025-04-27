package com.americanas.automation.pages;

import com.americanas.automation.utils.WaitUtils;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class BasePage {
    
    @Autowired
    protected WebDriver driver;
    
    @Autowired
    protected WaitUtils waitUtils;
    
    public BasePage() {
        // PageFactory será inicializado no construtor de cada página
    }
    
    protected void initElements() {
        PageFactory.initElements(driver, this);
    }
    
    public void navigateTo(String url) {
        driver.get(url);
        log.info("Navegando para: {}", url);
    }
    
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    public String getTitle() {
        return driver.getTitle();
    }
}