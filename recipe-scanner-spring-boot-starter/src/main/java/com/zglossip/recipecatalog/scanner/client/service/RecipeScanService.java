package com.zglossip.recipecatalog.scanner.client.service;

import com.zglossip.recipecatalog.scanner.client.domain.RecipeScanResponse;
import com.zglossip.recipecatalog.scanner.client.extract.ImageOcrTextExtractor;
import com.zglossip.recipecatalog.scanner.client.extract.PdfOcrTextExtractor;
import com.zglossip.recipecatalog.scanner.client.extract.TextExtractor;
import com.zglossip.recipecatalog.scanner.client.parse.OllamaClient;
import com.zglossip.recipecatalog.scanner.client.parse.ParsedRecipesResult;
import com.zglossip.recipecatalog.scanner.client.parse.RecipeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

public class RecipeScanService {
  private static final Logger LOGGER = LoggerFactory.getLogger(RecipeScanService.class);

  private final List<TextExtractor> textExtractors = Arrays.asList(new ImageOcrTextExtractor(), new PdfOcrTextExtractor());

  private final RecipeParser recipeParser;
  private final OllamaClient ollamaClient;

  public RecipeScanService(final String baseUrl, final String model, final int numCtx) {
    this.ollamaClient = new OllamaClient(baseUrl, model, numCtx);
    this.recipeParser = new RecipeParser(ollamaClient);
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

    if (text == null || text.isBlank()) {
      LOGGER.warn("OCR produced no text filename={} contentType={}",
          file.getOriginalFilename(),
          file.getContentType());
      throw new ResponseStatusException(
          HttpStatus.UNPROCESSABLE_CONTENT,
          "No text could be extracted from the file."
      );
    }
    return new RecipeScanResponse(recipeParser.parse(text), text, "Recipe scanned successfully.");
  }

  public String ocr(MultipartFile file) {
    LOGGER.info("OCR-only scan file name={} contentType={} sizeBytes={}",
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

    String text = extractor.extract(file);

    if (text == null || text.isBlank()) {
      LOGGER.warn("OCR produced no text filename={} contentType={}",
          file.getOriginalFilename(),
          file.getContentType());
      throw new ResponseStatusException(
          HttpStatus.UNPROCESSABLE_CONTENT,
          "No text could be extracted from the file."
      );
    }

    return text;
  }

  public ParsedRecipesResult parseText(String text) {
    LOGGER.info("Parsing text directly chars={}", text.length());
    return ollamaClient.generateRecipes(text);
  }
}
