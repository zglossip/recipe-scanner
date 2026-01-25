package com.zglossip.recipescanner.service;

import com.zglossip.recipescanner.api.RecipeScanResponse;
import com.zglossip.recipescanner.client.FoodHistoryApiClient;
import com.zglossip.recipescanner.domain.Recipe;
import com.zglossip.recipescanner.extract.TextExtractor;
import com.zglossip.recipescanner.parse.RecipeParser;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

@Service
public class RecipeScanService {
	private final List<TextExtractor> textExtractors;
	private final RecipeParser recipeParser;
	private final FoodHistoryApiClient foodHistoryApiClient;

	public RecipeScanService(
			List<TextExtractor> textExtractors,
			RecipeParser recipeParser,
			FoodHistoryApiClient foodHistoryApiClient
	) {
		this.textExtractors = textExtractors;
		this.recipeParser = recipeParser;
		this.foodHistoryApiClient = foodHistoryApiClient;
	}

	public RecipeScanResponse scan(MultipartFile file) {
		TextExtractor extractor = textExtractors.stream()
				.filter(candidate -> candidate.supports(file))
				.findFirst()
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.UNSUPPORTED_MEDIA_TYPE,
						"Unsupported file type."
				));
		String text = extractor.extract(file);
		Recipe recipe = recipeParser.parse(text);
		foodHistoryApiClient.send(recipe);
		return new RecipeScanResponse("submitted", "Recipe sent to food-history-api.");
	}
}
