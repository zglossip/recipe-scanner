package com.zglossip.recipescanner.extract;

import com.zglossip.recipescanner.config.OcrProperties;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class TesseractFactory {
	private static final String DEFAULT_TESSDATA_PATH = "/usr/share/tesseract-ocr/5/tessdata";

	private final OcrProperties ocrProperties;

	public TesseractFactory(OcrProperties ocrProperties) {
		this.ocrProperties = ocrProperties;
	}

	public Tesseract create() {
		Tesseract tesseract = new Tesseract();
		tesseract.setLanguage(ocrProperties.language());
		if (Files.isDirectory(Path.of(DEFAULT_TESSDATA_PATH))) {
			tesseract.setDatapath(DEFAULT_TESSDATA_PATH);
		}
		return tesseract;
	}
}
