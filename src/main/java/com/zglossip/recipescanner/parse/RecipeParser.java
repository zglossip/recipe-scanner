package com.zglossip.recipescanner.parse;

import com.zglossip.recipescanner.domain.Recipe;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class RecipeParser {
	public Recipe parse(String text) {
		// TODO: Implement parsing rules to map OCR text into Recipe fields.
		// TODO: Extract tags, courseTypes, and cuisineTypes from parsed sections.
		return new Recipe(null, "", List.of(), List.of(), List.of(), null, null, null, Instant.now());
	}
}
