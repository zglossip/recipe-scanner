package com.zglossip.recipecatalog.scanner.client.parse;

import com.zglossip.recipecatalog.scanner.client.domain.Recipe;
import com.zglossip.recipecatalog.scanner.client.domain.ScannedRecipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;

public class RecipeParser {

  private static final Logger LOGGER = LoggerFactory.getLogger(RecipeParser.class);

  private final OllamaClient ollamaClient;

  public RecipeParser(final OllamaClient ollamaClient) {
    this.ollamaClient = ollamaClient;
  }

  private String normalizeOcrText(String text) {
    return text
        // Unicode fraction characters
        .replace("\u00BC", "1/4")   // ¼
        .replace("\u00BD", "1/2")   // ½
        .replace("\u00BE", "3/4")   // ¾
        .replace("\u2153", "1/3")   // ⅓
        .replace("\u2154", "2/3")   // ⅔
        .replace("\u215B", "1/8")   // ⅛
        .replace("\u215C", "3/8")   // ⅜
        .replace("\u215D", "5/8")   // ⅝
        .replace("\u215E", "7/8")   // ⅞
        // Common Tesseract misreadings of fractions (e.g. ¼ → 4%, ½ → 2%)
        .replaceAll("(?<![\\d])4%", "1/4")
        .replaceAll("(?<![\\d])2%", "1/2")
        .replaceAll("(?<![\\d])3%", "3/4")
        .replaceAll("(?<![\\d])1%", "1/8");
  }

  public List<ScannedRecipe> parse(String text) {
    String normalized = normalizeOcrText(text);
    LOGGER.info("Parsing OCR text ({} chars, {} after normalization)", text.length(), normalized.length());
    ParsedRecipesResult result = ollamaClient.generateRecipes(normalized);
    return result.recipes().stream()
        .map(p -> new ScannedRecipe(
            new Recipe(
                p.name(),
                List.of(),
                List.of(),
                List.of(),
                p.servingAmount(),
                p.servingName(),
                "",
                Instant.now()
            ),
            p.ingredients() != null ? p.ingredients() : List.of(),
            p.instructions() != null ? p.instructions() : List.of()
        ))
        .toList();
  }
}
