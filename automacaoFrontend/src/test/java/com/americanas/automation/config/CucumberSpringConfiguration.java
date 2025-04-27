package com.americanas.automation.config;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = com.americanas.automation.Application.class)
public class CucumberSpringConfiguration {
}