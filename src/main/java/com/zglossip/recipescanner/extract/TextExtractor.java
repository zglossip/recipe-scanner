package com.zglossip.recipescanner.extract;

import org.springframework.web.multipart.MultipartFile;

public interface TextExtractor {
	boolean supports(MultipartFile file);

	String extract(MultipartFile file);
}
