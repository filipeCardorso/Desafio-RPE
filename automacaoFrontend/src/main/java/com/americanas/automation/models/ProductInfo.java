package com.americanas.automation.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfo {
    private String name;
    private double price;
    private String rating;
    
    @Override
    public String toString() {
        return String.format("● Informações do produto desejado:\n● Nome: %s Estrelas: %s", name, rating);
    }
}