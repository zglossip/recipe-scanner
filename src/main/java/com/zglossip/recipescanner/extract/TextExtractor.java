package com.zglossip.recipescanner.extract;

import org.springframework.web.multipart.MultipartFile;

public interface TextExtractor {
	String extract(MultipartFile file);
}
