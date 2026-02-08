package com.zglossip.recipescanner.api;

import com.zglossip.recipescanner.service.RecipeScanService;
import com.zglossip.recipescanner.validation.UploadedFileValidator;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/recipes")
public class RecipeScanController {
	private final RecipeScanService recipeScanService;
	private final UploadedFileValidator uploadedFileValidator;

	public RecipeScanController(
			RecipeScanService recipeScanService,
			UploadedFileValidator uploadedFileValidator
	) {
		this.recipeScanService = recipeScanService;
		this.uploadedFileValidator = uploadedFileValidator;
	}

	@PostMapping(path = "/scan", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<RecipeScanResponse> scan(@RequestPart("file") MultipartFile file) {
		uploadedFileValidator.validateForScan(file);
		RecipeScanResponse response = recipeScanService.scan(file);
		return ResponseEntity.ok(response);
	}
}
