package com.zglossip.recipecatalog.scanner.client.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Ingredient(String name, Double quantity, String uom, String notes) {
}
