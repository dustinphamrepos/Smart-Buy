package com.project.smartbuy.controllers;

import com.project.smartbuy.dtos.ProductDTO;
import com.project.smartbuy.dtos.ProductImageDTO;
import com.project.smartbuy.models.Product;
import com.project.smartbuy.models.ProductImage;
import com.project.smartbuy.services.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.UUID.randomUUID;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {

  private final IProductService productService;
  @GetMapping("")
  public ResponseEntity<String> getProducts(
    @RequestParam("page") int page,
    @RequestParam("limit") int limit
  ) {
    return ResponseEntity.ok("getProducts here" + page + limit);
  }

  @GetMapping("/{id}")
  public ResponseEntity<String> getProductById(@PathVariable("id") String productId) {
    return ResponseEntity.ok("Product with ID: " + productId);
  }

  @PostMapping(value = "", consumes = MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> createProduct(
    @Valid @ModelAttribute ProductDTO productDTO,
    BindingResult result) {
    try {
      if (result.hasErrors()) {
        List<String> errorMessages = result.getFieldErrors()
          .stream()
          .map(FieldError::getDefaultMessage)
          .toList();
        return ResponseEntity.badRequest().body(errorMessages);
      }

      Product newProduct = productService.createProduct(productDTO);

      List<MultipartFile> files = productDTO.getFiles();
      files = files == null ? new ArrayList<MultipartFile>() : files;
      for (MultipartFile file : files) {
        if (file.getSize() == 0) {
          continue;
        }
        if (file.getSize() > 10 * 1024 * 1024) {
          //throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "File is too large!");
          return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File is too large! Maximum size is 10MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
          return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an image!");
        }

        String filename = storeFile(file);
        ProductImage newProductImage = productService.createProductImage(newProduct.getId(), ProductImageDTO.builder()
          .imageUrl(filename)
          .build());
      }
      return ResponseEntity.ok("Product created successfully.");
    }
    catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  private String storeFile(MultipartFile file) throws IOException {
    String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
    String uniqueFilename = randomUUID().toString() + "_" + filename;
    Path uploadDir = Paths.get("uploads");
    if (!Files.exists(uploadDir)) {
      Files.createDirectories(uploadDir);
    }
    Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
    Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
    return uniqueFilename;
  }

//  {
//    "name": "ipad 2025",
//    "price": 999,
//    "thumbnail": "",
//    "description": "This is the best new gen ipad.",
//    "category_id": "1"
//  }

  @DeleteMapping("/{id}")
  public  ResponseEntity<String> deleteProduct(@PathVariable("id") Long productId) {
    return ResponseEntity.ok(String.format("Product with id = %d deleted successfully.", productId));
  }

}
