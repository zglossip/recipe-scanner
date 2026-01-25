package com.zglossip.recipescanner.extract;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageOcrTextExtractor implements TextExtractor {
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
		// TODO: Run OCR over image uploads (e.g., JPG/PNG) and return extracted text.
		return "";
	}
}
