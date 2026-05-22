package com.zglossip.recipecatalog.scanner.client.parse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zglossip.recipecatalog.scanner.client.domain.Ingredient;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
record ParsedRecipe(
    String name,
    Integer servingAmount,
    String servingName,
    List<Ingredient> ingredients,
    List<String> instructions
) {
}
