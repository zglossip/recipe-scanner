# recipe-scanner
Reads files containing recipes such as PDFs and images, and sends it to the food-history-api

## Upload limits
Multipart uploads are capped at 50MB by default. Adjust these in `src/main/resources/application.yaml`:

```yaml
spring:
  servlet:
    multipart:
      max-file-size: 50MB
      max-request-size: 50MB
```

## Configuration
Set the downstream API base URL and OCR language in `src/main/resources/application.yaml`:

```yaml
food-history-api:
  base-url: "http://localhost:8080"

ocr:
  language: "eng"
```
