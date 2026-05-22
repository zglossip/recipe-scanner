package com.zglossip.recipecatalog.scanner.client.domain;

import java.time.Instant;
import java.util.List;

public record Recipe(
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
