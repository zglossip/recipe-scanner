package com.zglossip.recipescanner.extract;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class PdfOcrTextExtractor implements TextExtractor {
	@Override
	public String extract(MultipartFile file) {
		// TODO: Render scanned PDF pages to images (e.g., PDFBox) and run OCR (e.g., Tesseract).
		// TODO: Aggregate per-page OCR output into a single text payload.
		return "";
	}
}
