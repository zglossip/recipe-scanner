package com.zglossip.recipescanner.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "ocr")
@Validated
public record OcrProperties(@NotBlank String language) {
}
