package com.zglossip.recipescanner.extract;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

@Component
public class PdfOcrTextExtractor implements TextExtractor {
	private final TesseractFactory tesseractFactory;

	public PdfOcrTextExtractor(TesseractFactory tesseractFactory) {
		this.tesseractFactory = tesseractFactory;
	}

	@Override
	public boolean supports(MultipartFile file) {
		return "application/pdf".equals(normalizeContentType(file == null ? null : file.getContentType()));
	}

	@Override
	public String extract(MultipartFile file) {
		Path tempPdf = null;
		try {
			tempPdf = Files.createTempFile("recipe-scan-", ".pdf");
			try (var inputStream = file.getInputStream()) {
				Files.copy(inputStream, tempPdf, StandardCopyOption.REPLACE_EXISTING);
			}

			try (RandomAccessReadBufferedFile randomAccessRead = new RandomAccessReadBufferedFile(tempPdf);
				 PDDocument document = Loader.loadPDF(randomAccessRead)) {
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
			}
		} catch (IOException e) {
			throw new UncheckedIOException("Failed to read PDF", e);
		} catch (TesseractException e) {
			throw new IllegalStateException("Failed to OCR rendered PDF page", e);
		} finally {
			if (tempPdf != null) {
				try {
					Files.deleteIfExists(tempPdf);
				} catch (IOException ignored) {
					// Cleanup best effort only; OCR result path should not fail because temp deletion failed.
				}
			}
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
