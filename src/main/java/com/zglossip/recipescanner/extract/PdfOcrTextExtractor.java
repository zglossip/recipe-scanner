package com.zglossip.recipescanner.extract;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;

@Component
public class PdfOcrTextExtractor implements TextExtractor {
	private final TesseractFactory tesseractFactory;

	public PdfOcrTextExtractor(TesseractFactory tesseractFactory) {
		this.tesseractFactory = tesseractFactory;
	}

	@Override
	public boolean supports(MultipartFile file) {
		if (file == null) {
			return false;
		}
		String contentType = file.getContentType();
		if (contentType == null) {
			return false;
		}
		int separator = contentType.indexOf(';');
		if (separator >= 0) {
			contentType = contentType.substring(0, separator);
		}
		return "application/pdf".equalsIgnoreCase(contentType.trim());
	}

	@Override
	public String extract(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new IllegalArgumentException("File is required");
		}

		try (PDDocument document = Loader.loadPDF(file.getBytes())) {
			int pageCount = document.getNumberOfPages();
			if (pageCount == 0) {
				return "";
			}

			PDFRenderer renderer = new PDFRenderer(document);
			Tesseract tesseract = tesseractFactory.create();

			StringBuilder extractedText = new StringBuilder();
			for (int pageIndex = 0; pageIndex < pageCount; pageIndex++) {
				BufferedImage page = renderer.renderImageWithDPI(pageIndex, 300, ImageType.RGB);
				extractedText.append(tesseract.doOCR(page));
				if (pageIndex < pageCount - 1) {
					extractedText.append("\n\n");
				}
			}

			return extractedText.toString();
		} catch (IOException e) {
			throw new UncheckedIOException("Failed to read PDF", e);
		} catch (TesseractException e) {
			throw new IllegalStateException("Failed to OCR rendered PDF page", e);
		}
	}
}
