package com.zglossip.recipescanner.domain;

import java.util.List;

public record InstructionList(Long recipeId, List<String> instructions) {
}
