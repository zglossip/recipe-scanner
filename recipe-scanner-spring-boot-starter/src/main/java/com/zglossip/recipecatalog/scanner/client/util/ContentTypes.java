package com.zglossip.recipecatalog.scanner.client.util;

import java.util.Locale;

public final class ContentTypes {
  private ContentTypes() {
  }

  public static String normalize(String contentType) {
    if (contentType == null) {
      return null;
    }
    String normalized = contentType.trim().toLowerCase(Locale.ROOT);
    int separator = normalized.indexOf(';');
    if (separator >= 0) {
      return normalized.substring(0, separator).trim();
    }
    return normalized;
  }
}
