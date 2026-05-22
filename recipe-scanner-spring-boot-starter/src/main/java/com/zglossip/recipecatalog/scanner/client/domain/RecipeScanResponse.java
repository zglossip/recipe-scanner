package com.zglossip.recipecatalog.scanner.client.domain;

import java.util.List;

public record RecipeScanResponse(List<ScannedRecipe> recipes, String scanned, String message) {
}
