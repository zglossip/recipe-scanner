package com.zglossip.recipescanner.domain;

import java.util.List;

public record Recipe(String title, List<Ingredient> ingredients, List<Instruction> instructions, String notes) {
}
