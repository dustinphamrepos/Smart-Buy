package com.project.smartbuy.controllers;

import com.github.javafaker.Faker;
import com.project.smartbuy.components.LocalizationUtils;
import com.project.smartbuy.dtos.ProductDTO;
import com.project.smartbuy.dtos.ProductImageDTO;
import com.project.smartbuy.models.Product;
import com.project.smartbuy.models.ProductImage;
import com.project.smartbuy.responses.ProductListResponse;
import com.project.smartbuy.responses.ProductResponse;
import com.project.smartbuy.services.IProductService;
import com.project.smartbuy.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
  private final LocalizationUtils localizationUtils;
  @GetMapping("")
  public ResponseEntity<ProductListResponse> getProducts(
    @RequestParam("page") int page,
    @RequestParam("limit") int limit
  ) {
    //create pageRequest from page and limit
    PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").descending());
    Page<ProductResponse> productPage = productService.getAllProducts(pageRequest);

    //get the total number of pages
    int totalPages = productPage.getTotalPages();
    List<ProductResponse> products = productPage.getContent();
    return ResponseEntity.ok(ProductListResponse.builder()
      .products(products)
      .totalPages(totalPages)
      .build());
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getProductById(@PathVariable("id") Long productId) {
    try {
      Product existingProduct = productService.getProductById(productId);
      return ResponseEntity.ok(ProductResponse.fromProduct(existingProduct));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("")
  public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO productDTO, BindingResult result) {
    try {
      if (result.hasErrors()) {
        List<String> errorMessages = result.getFieldErrors()
          .stream()
          .map(FieldError::getDefaultMessage)
          .toList();
        return ResponseEntity.badRequest().body(errorMessages);
      }

      Product newProduct = productService.createProduct(productDTO);
      return ResponseEntity.ok(newProduct);
    }
    catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping(value = "uploads/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> uploadImages(
    @PathVariable("id") Long productId,
    @ModelAttribute("files") List<MultipartFile> files
  ) throws Exception {
    Product existingProduct = productService.getProductById(productId);
    files = files == null ? new ArrayList<MultipartFile>() : files;
    if (files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
      return ResponseEntity.badRequest()
              .body(localizationUtils.getLocalizedMessage(
                      MessageKeys.UPLOAD_IMAGES_MAX_X, ProductImage.MAXIMUM_IMAGES_PER_PRODUCT
              ));
    }
    List<ProductImage> productImages = new ArrayList<>();
    for (MultipartFile file : files) {
      if (file.getSize() == 0) {
        continue;
      }
      if (file.getSize() > 10 * 1024 * 1024) {
        //throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "File is too large!");
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(localizationUtils.getLocalizedMessage(MessageKeys.UPLOAD_IMAGES_FILE_LARGE));
      }

      String contentType = file.getContentType();
      if (contentType == null || !contentType.startsWith("image/")) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(localizationUtils.getLocalizedMessage(MessageKeys.UPLOAD_IMAGES_FILE_MUST_BE_IMAGE));
      }

      String filename = storeFile(file);
      ProductImage newProductImage = productService.createProductImage(productId, ProductImageDTO.builder()
        .imageUrl(filename)
        .build());
      productImages.add(newProductImage);
    }
    return ResponseEntity.ok().body(productImages);
  }

  private boolean isImageFile(MultipartFile file) {
    String contentType = file.getContentType();
    return contentType != null && contentType.startsWith("image/");
  }

  private String storeFile(MultipartFile file) throws IOException {
    if (!isImageFile(file) || file.getOriginalFilename() == null) {
      throw new IOException("Invalid image format.");
    }
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

  @PutMapping("/{id}")
  public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
    try {
      Product updatedProduct = productService.updateProduct(id, productDTO);
      return ResponseEntity.ok(ProductResponse.fromProduct(updatedProduct));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  public  ResponseEntity<String> deleteProduct(@PathVariable("id") Long productId) {
    try {
      productService.deleteProduct(productId);
      return ResponseEntity.ok()
              .body(localizationUtils.getLocalizedMessage(MessageKeys.DELETE_PRODUCT_SUCCESSFULLY, productId));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  //@PostMapping("/generateFakeProducts")
  private ResponseEntity<String> generateFakeProducts() {
    Faker faker = new Faker();
    for (int i = 0; i < 10000; i++) {
      String productName = faker.commerce().productName();
      if (productService.existsByName(productName)) {
        continue;
      }
      ProductDTO productDTO = ProductDTO.builder()
        .name(productName)
        .price((float)faker.number().numberBetween(10, 90000000))
        .description(faker.lorem().sentence())
        .thumbnail("")
        .categoryId((long)faker.number().numberBetween(4, 7))
        .build();
      try {
        productService.createProduct(productDTO);
      } catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
      }
    }
    return ResponseEntity.ok().body("Fake products created successfully");
  }

}
