package com.project.smartbuy.controllers;

import com.project.smartbuy.components.LocalizationUtils;
import com.project.smartbuy.dtos.CategoryDTO;
import com.project.smartbuy.models.Category;
import com.project.smartbuy.services.CategoryService;
import com.project.smartbuy.utils.MessageKeys;
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
  private final LocalizationUtils localizationUtils;

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
      return ResponseEntity.ok()
              .body(localizationUtils
                      .getLocalizedMessage(MessageKeys.CREATE_CATEGORY_SUCCESSFULLY, categoryDTO.getName())
              );
    }
    catch (Exception e) {
      return ResponseEntity.badRequest()
              .body(localizationUtils
                      .getLocalizedMessage(MessageKeys.CREATE_CATEGORY_FAILED)
              );
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
    return ResponseEntity.ok().body(localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_CATEGORY_SUCCESSFULLY, id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
    categoryService.deleteCategory(id);
    return ResponseEntity.ok().body(localizationUtils.getLocalizedMessage(MessageKeys.DELETE_CATEGORY_SUCCESSFULLY, id));
  }
}
