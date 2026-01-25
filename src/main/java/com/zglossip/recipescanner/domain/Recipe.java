package com.zglossip.recipescanner.domain;

import java.time.Instant;
import java.util.List;

public record Recipe(
		Long id,
		String name,
		List<String> courseTypes,
		List<String> cuisineTypes,
		List<String> tags,
		Integer servingAmount,
		String servingName,
		String source,
		Instant uploaded
) {
}
