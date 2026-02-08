package com.zglossip.recipescanner.extract;

import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Locale;

@Component
public class ImageOcrTextExtractor implements TextExtractor {
	private final TesseractFactory tesseractFactory;

	public ImageOcrTextExtractor(TesseractFactory tesseractFactory) {
		this.tesseractFactory = tesseractFactory;
	}

	@Override
	public boolean supports(MultipartFile file) {
		String contentType = normalizeContentType(file == null ? null : file.getContentType());
		return contentType != null && contentType.startsWith("image/");
	}

	@Override
	public String extract(MultipartFile file) {
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

	private String normalizeContentType(String contentType) {
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
