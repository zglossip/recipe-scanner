package com.zglossip.recipescanner.validation;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;
import java.util.Set;

@Component
public class UploadedFileValidator {
	private static final Set<String> IMAGE_EXTENSIONS = Set.of(
			"bmp",
			"gif",
			"heic",
			"heif",
			"jpg",
			"jpeg",
			"png",
			"tif",
			"tiff",
			"webp"
	);

	public void validateForScan(MultipartFile file) {
		if (file == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is required.");
		}
		if (file.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty.");
		}

		String contentType = normalizeContentType(file.getContentType());
		if (contentType == null || contentType.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Content type is required.");
		}

		String filename = file.getOriginalFilename();
		if (filename == null || filename.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Filename is required.");
		}
		String extension = extractExtension(filename);
		if (extension == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Filename must include an extension.");
		}

		validateExtensionForContentType(contentType, extension);
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

	private String extractExtension(String filename) {
		int lastDot = filename.lastIndexOf('.');
		if (lastDot < 0 || lastDot == filename.length() - 1) {
			return null;
		}
		return filename.substring(lastDot + 1).toLowerCase(Locale.ROOT);
	}

	private void validateExtensionForContentType(String contentType, String extension) {
		if ("application/pdf".equals(contentType)) {
			if (!"pdf".equals(extension)) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File extension does not match content type.");
			}
			return;
		}

		if (contentType.startsWith("image/")) {
			if (!IMAGE_EXTENSIONS.contains(extension)) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported image file extension.");
			}
			return;
		}

		throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported file type.");
	}
}
