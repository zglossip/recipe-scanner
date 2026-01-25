package com.zglossip.recipescanner.parse;

import com.zglossip.recipescanner.domain.Recipe;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class RecipeParser {
	public Recipe parse(String text) {
		// TODO: Implement parsing rules to map OCR text into Recipe fields.
		// TODO: Extract ingredients and instructions lists from parsed sections.
		return new Recipe("", List.of(), List.of(), "");
	}
}
