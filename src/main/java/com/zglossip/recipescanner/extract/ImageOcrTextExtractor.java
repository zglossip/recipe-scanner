package com.zglossip.recipescanner.extract;

import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;

@Component
public class ImageOcrTextExtractor implements TextExtractor {
	private final TesseractFactory tesseractFactory;

	public ImageOcrTextExtractor(TesseractFactory tesseractFactory) {
		this.tesseractFactory = tesseractFactory;
	}

	@Override
	public boolean supports(MultipartFile file) {
		String contentType = file == null ? null : file.getContentType();
		if (contentType == null) {
			return false;
		}
		return contentType.startsWith("image/");
	}

	@Override
	public String extract(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new IllegalArgumentException("File is required");
		}

		try {
			BufferedImage image = ImageIO.read(file.getInputStream());
			if (image == null) {
				return "";
			}
			return tesseractFactory.create().doOCR(image);
		} catch (IOException e) {
			throw new UncheckedIOException("Failed to read image", e);
		} catch (TesseractException e) {
			throw new IllegalStateException("Failed to OCR image file", e);
		}
	}
}
