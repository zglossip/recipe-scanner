package com.zglossip.recipecatalog.scanner.client.extract;

import net.sourceforge.tess4j.Tesseract;

import java.nio.file.Files;
import java.nio.file.Path;

public class TesseractFactory {
  private static final String DEFAULT_TESSDATA_PATH = "/usr/share/tesseract-ocr/5/tessdata";

  public Tesseract create() {
    Tesseract tesseract = new Tesseract();
    if (Files.isDirectory(Path.of(DEFAULT_TESSDATA_PATH))) {
      tesseract.setDatapath(DEFAULT_TESSDATA_PATH);
    }
    return tesseract;
  }
}
