package com.zglossip.recipecatalog.scanner.service;

import com.zglossip.recipecatalog.client.models.FullRecipeRequest;
import com.zglossip.recipecatalog.client.models.Ingredient;
import com.zglossip.recipecatalog.client.services.RecipeCatalogService;
import com.zglossip.recipecatalog.scanner.client.domain.ScannedRecipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubmissionService {

  private final RecipeCatalogService recipeCatalogService;

  @Autowired
  public SubmissionService(RecipeCatalogService recipeCatalogService) {
    this.recipeCatalogService = recipeCatalogService;
  }

  public void submit(List<ScannedRecipe> recipes) {
    recipes.stream()
        .map(sr -> new FullRecipeRequest(
            sr.recipe().name(),
            sr.recipe().courseTypes(),
            sr.recipe().cuisineTypes(),
            sr.recipe().tags(),
            sr.recipe().servingAmount(),
            sr.recipe().servingName(),
            null,
            LocalDateTime.now(),
            sr.ingredients().stream().map(i -> new Ingredient(i.name(),
                i.quantity() != null ? BigDecimal.valueOf(i.quantity()) : BigDecimal.ZERO,
                i.uom(),
                i.notes())).toList(),
            sr.instructions()))
        .forEach(recipeCatalogService::createFullRecipe);
  }

}
