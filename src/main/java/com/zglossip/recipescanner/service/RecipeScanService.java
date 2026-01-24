package com.zglossip.recipescanner.service;

import com.zglossip.recipescanner.api.RecipeScanResponse;
import com.zglossip.recipescanner.client.FoodHistoryApiClient;
import com.zglossip.recipescanner.domain.Recipe;
import com.zglossip.recipescanner.extract.TextExtractor;
import com.zglossip.recipescanner.parse.RecipeParser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class RecipeScanService {
	private final TextExtractor textExtractor;
	private final RecipeParser recipeParser;
	private final FoodHistoryApiClient foodHistoryApiClient;

	public RecipeScanService(
			TextExtractor textExtractor,
			RecipeParser recipeParser,
			FoodHistoryApiClient foodHistoryApiClient
	) {
		this.textExtractor = textExtractor;
		this.recipeParser = recipeParser;
		this.foodHistoryApiClient = foodHistoryApiClient;
	}

	public RecipeScanResponse scan(MultipartFile file) {
		// TODO: Validate file type/size and route to appropriate extractor.
		String text = textExtractor.extract(file);
		Recipe recipe = recipeParser.parse(text);
		foodHistoryApiClient.send(recipe);
		return new RecipeScanResponse("submitted", "Recipe sent to food-history-api.");
	}
}
