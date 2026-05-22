package com.zglossip.recipecatalog.scanner.client.extract;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

class ImagePreprocessor {
  private ImagePreprocessor() {
  }

  static BufferedImage toBlackAndWhite(BufferedImage source) {
    BufferedImage gray = toGrayscale(source);
    BufferedImage contrasted = enhanceContrast(gray);
    return binarize(contrasted);
  }

  private static BufferedImage toGrayscale(BufferedImage image) {
    BufferedImage gray = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
    Graphics2D g = gray.createGraphics();
    g.drawImage(image, 0, 0, null);
    g.dispose();
    return gray;
  }

  private static BufferedImage enhanceContrast(BufferedImage image) {
    return new RescaleOp(1.5f, -20f, null).filter(image, null);
  }

  private static BufferedImage binarize(BufferedImage image) {
    int width = image.getWidth();
    int height = image.getHeight();

    int[] histogram = new int[256];
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        histogram[image.getRaster().getSample(x, y, 0)]++;
      }
    }

    int total = width * height;
    float sum = 0;
    for (int i = 0; i < 256; i++) sum += i * histogram[i];

    float sumB = 0, maxVariance = 0;
    int wB = 0, threshold = 128;
    for (int i = 0; i < 256; i++) {
      wB += histogram[i];
      if (wB == 0) continue;
      int wF = total - wB;
      if (wF == 0) break;
      sumB += i * histogram[i];
      float mB = sumB / wB;
      float mF = (sum - sumB) / wF;
      float variance = (float) wB * wF * (mB - mF) * (mB - mF);
      if (variance > maxVariance) {
        maxVariance = variance;
        threshold = i;
      }
    }

    BufferedImage binary = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        binary.getRaster().setSample(x, y, 0, image.getRaster().getSample(x, y, 0) > threshold ? 255 : 0);
      }
    }
    return binary;
  }
}
