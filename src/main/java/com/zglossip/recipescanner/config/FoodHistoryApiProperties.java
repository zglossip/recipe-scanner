package com.zglossip.recipescanner.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "food-history-api")
@Validated
public record FoodHistoryApiProperties(@NotBlank String baseUrl) {
}
