package com.zglossip.recipescanner.parse;

import com.zglossip.recipescanner.domain.Recipe;

public interface RecipeParser {
	Recipe parse(String text);
}
