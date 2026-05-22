package com.zglossip.recipecatalog.scanner.client.domain;

import java.util.List;

public record InstructionList(Long recipeId, List<String> instructions) {
}
