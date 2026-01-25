package com.zglossip.recipescanner.domain;

import java.util.List;

public record IngredientList(Long recipeId, List<Ingredient> ingredients) {
}
