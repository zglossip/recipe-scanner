package com.zglossip.recipescanner.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "food-history-api")
public record FoodHistoryApiProperties(String baseUrl) {
}
