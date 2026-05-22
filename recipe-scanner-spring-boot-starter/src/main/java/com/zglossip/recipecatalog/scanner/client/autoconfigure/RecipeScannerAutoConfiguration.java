package com.zglossip.recipecatalog.scanner.client.autoconfigure;

import com.zglossip.recipecatalog.scanner.client.config.OllamaProperties;
import com.zglossip.recipecatalog.scanner.client.service.RecipeScanService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(OllamaProperties.class)
public class RecipeScannerAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  RecipeScanService recipeScanService(final OllamaProperties ollamaProperties) {
    return new RecipeScanService(ollamaProperties.baseUrl(), ollamaProperties.model(), ollamaProperties.numCtx());
  }

}
