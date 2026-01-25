package com.zglossip.recipescanner.extract;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageOcrTextExtractor implements TextExtractor {
	@Override
	public String extract(MultipartFile file) {
		// TODO: Run OCR over image uploads (e.g., JPG/PNG) and return extracted text.
		return "";
	}
}
