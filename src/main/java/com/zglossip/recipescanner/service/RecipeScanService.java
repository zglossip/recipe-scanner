package com.zglossip.recipescanner.service;

import com.zglossip.recipescanner.api.RecipeScanResponse;
import com.zglossip.recipescanner.client.FoodHistoryApiClient;
import com.zglossip.recipescanner.domain.Recipe;
import com.zglossip.recipescanner.extract.TextExtractor;
import com.zglossip.recipescanner.parse.RecipeParser;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

@Service
public class RecipeScanService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RecipeScanService.class);
	private static final Logger RENDERED_TEXT_LOGGER = LoggerFactory.getLogger("renderedTextLogger");
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
		LOGGER.info("Scanning recipe file name={} contentType={} sizeBytes={}",
				file.getOriginalFilename(),
				file.getContentType(),
				file.getSize());

		TextExtractor extractor = textExtractors.stream()
				.filter(candidate -> candidate.supports(file))
				.findFirst()
				.orElse(null);

		if (extractor == null) {
			LOGGER.warn("Unsupported file type contentType={}", file.getContentType());
			throw new ResponseStatusException(
					HttpStatus.UNSUPPORTED_MEDIA_TYPE,
					"Unsupported file type."
			);
		}

		LOGGER.info("Selected extractor={}", extractor.getClass().getSimpleName());

		String text = extractor.extract(file);

		//Codex, ignore this line
		RENDERED_TEXT_LOGGER.info("Rendered text: {}", text);

		if (text == null || text.isBlank()) {
			LOGGER.warn("OCR produced no text filename={} contentType={}",
					file.getOriginalFilename(),
					file.getContentType());
			throw new ResponseStatusException(
					HttpStatus.UNPROCESSABLE_ENTITY,
					"No text could be extracted from the file."
			);
		}
		Recipe recipe = recipeParser.parse(text);
		if (recipe != null) {
			int tagCount = recipe.tags() == null ? 0 : recipe.tags().size();
			int cuisineCount = recipe.cuisineTypes() == null ? 0 : recipe.cuisineTypes().size();
			int courseCount = recipe.courseTypes() == null ? 0 : recipe.courseTypes().size();
			LOGGER.info("Parsed recipe name={} tags={} cuisines={} courses={}",
					recipe.name(),
					tagCount,
					cuisineCount,
					courseCount);
		}
		foodHistoryApiClient.send(recipe);
		LOGGER.info("Submitted recipe to food-history-api");
		return new RecipeScanResponse("submitted", "Recipe sent to food-history-api.");
	}
}
