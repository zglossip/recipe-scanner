package com.zglossip.recipecatalog.scanner.client.parse;

import java.util.List;
import java.util.Map;

public record OllamaRequest(String model, List<OllamaMessage> messages, Object format, boolean stream,
                            Map<String, Object> options) {
}
