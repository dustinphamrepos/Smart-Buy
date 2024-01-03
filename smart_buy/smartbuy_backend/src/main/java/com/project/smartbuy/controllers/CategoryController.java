package com.project.smartbuy.controllers;

import com.project.smartbuy.dtos.CategoryDTO;
import com.project.smartbuy.models.Category;
import com.project.smartbuy.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
//@Validated
public class CategoryController {

  private final CategoryService categoryService;

  @PostMapping("")
  public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult result) {
    try {
      if (result.hasErrors()) {
        List<String> errorMessages = result.getFieldErrors()
          .stream()
          .map(FieldError::getDefaultMessage)
          .toList();
        return ResponseEntity.badRequest().body(errorMessages);
      }
      categoryService.createCategory(categoryDTO);
      return ResponseEntity.ok(String.format("Category '%s' created successfully.", categoryDTO.getName()));
    }
    catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("")
  public ResponseEntity<List<Category>> getAllCategories(
    @RequestParam("page") int page,
    @RequestParam("limit") int limit
  ) {
    List<Category> categories = categoryService.getAllCategories();
    return ResponseEntity.ok(categories);
  }

  @PutMapping("/{id}")
  public ResponseEntity<String> updateCategory(
    @PathVariable Long id,
    @RequestBody CategoryDTO categoryDTO
  ) {
    categoryService.updateCategory(id, categoryDTO);
    return ResponseEntity.ok(String.format("Updated category with ID = %d successfully", id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
    categoryService.deleteCategory(id);
    return ResponseEntity.ok(String.format("Deleted category with ID = %d successfully", id));
  }
}
