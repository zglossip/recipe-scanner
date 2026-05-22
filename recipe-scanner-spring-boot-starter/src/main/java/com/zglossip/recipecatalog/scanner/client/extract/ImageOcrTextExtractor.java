package com.zglossip.recipecatalog.scanner.client.extract;

import com.zglossip.recipecatalog.scanner.client.util.ContentTypes;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;

public class ImageOcrTextExtractor implements TextExtractor {
  private final TesseractFactory tesseractFactory = new TesseractFactory();

  @Override
  public boolean supports(MultipartFile file) {
    String contentType = ContentTypes.normalize(file == null ? null : file.getContentType());
    return contentType != null && contentType.startsWith("image/");
  }

  @Override
  public String extract(MultipartFile file) {
    try {
      BufferedImage image = ImageIO.read(file.getInputStream());
      if (image == null) {
        return "";
      }
      return tesseractFactory.create().doOCR(ImagePreprocessor.toBlackAndWhite(image));
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to read image", e);
    } catch (TesseractException e) {
      throw new IllegalStateException("Failed to OCR image file", e);
    }
  }

}
