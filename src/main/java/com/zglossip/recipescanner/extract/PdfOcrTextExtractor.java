package com.zglossip.recipescanner.extract;

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
	@Override
	public boolean supports(MultipartFile file) {
		return file != null
				&& "application/pdf".equalsIgnoreCase(file.getContentType());
	}

	@Override
	public String extract(MultipartFile file) {
		try (PDDocument document = Loader.loadPDF(file.getBytes())) {
			if(document.getNumberOfPages() == 0) {
				return "";
			}

			PDFRenderer renderer = new PDFRenderer(document);
			BufferedImage page1 = renderer.renderImageWithDPI(0, 300, ImageType.RGB);

			return "page1=" + page1.getWidth() + "x" + page1.getHeight();
		} catch (IOException e) {
			throw new UncheckedIOException("Failed to read PDF", e);
		}
	}
}
