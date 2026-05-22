package com.zglossip.recipecatalog.scanner.api;

import com.zglossip.recipecatalog.scanner.client.domain.RecipeScanResponse;
import com.zglossip.recipecatalog.scanner.client.domain.ScannedRecipe;
import com.zglossip.recipecatalog.scanner.client.parse.ParsedRecipesResult;
import com.zglossip.recipecatalog.scanner.client.service.RecipeScanService;
import com.zglossip.recipecatalog.scanner.service.SubmissionService;
import com.zglossip.recipecatalog.scanner.validation.UploadedFileValidator;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeScanController {
  private final RecipeScanService recipeScanService;
  private final SubmissionService submissionService;
  private final UploadedFileValidator uploadedFileValidator;

  public RecipeScanController(
      RecipeScanService recipeScanService,
      SubmissionService submissionService,
      UploadedFileValidator uploadedFileValidator
  ) {
    this.recipeScanService = recipeScanService;
    this.submissionService = submissionService;
    this.uploadedFileValidator = uploadedFileValidator;
  }

  @PostMapping(path = "/scan", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<RecipeScanResponse> scan(@RequestPart("file") MultipartFile file) {
    uploadedFileValidator.validateForScan(file);
    return ResponseEntity.ok(recipeScanService.scan(file));
  }

  @PostMapping(path = "/scan/ocr", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<OcrResponse> scanOcr(@RequestPart("file") MultipartFile file) {
    uploadedFileValidator.validateForScan(file);
    return ResponseEntity.ok(new OcrResponse(recipeScanService.ocr(file)));
  }

  @PostMapping(path = "/scan/text", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ParsedRecipesResult> scanText(@RequestBody ScanTextRequest request) {
    return ResponseEntity.ok(recipeScanService.parseText(request.text()));
  }

  @PostMapping(path = "/submit", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> submit(@RequestBody List<ScannedRecipe> submission) {
    submissionService.submit(submission);
    return ResponseEntity.ok().build();
  }
}
