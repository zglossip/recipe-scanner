package com.zglossip.recipecatalog.scanner.client.parse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ParsedRecipesResult(List<ParsedRecipe> recipes) {
}
