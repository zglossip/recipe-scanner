package com.zglossip.recipescanner.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ocr")
public record OcrProperties(String language) {
}
