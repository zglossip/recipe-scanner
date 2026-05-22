package com.zglossip.recipecatalog.scanner.client.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "ollama")
@Validated
public record OllamaProperties(@NotBlank String baseUrl, @NotBlank String model, int numCtx) {
}
