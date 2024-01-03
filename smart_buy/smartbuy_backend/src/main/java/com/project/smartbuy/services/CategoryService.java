package com.project.smartbuy.services;

import com.project.smartbuy.dtos.CategoryDTO;
import com.project.smartbuy.models.Category;
import com.project.smartbuy.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{

  private final CategoryRepository categoryRepository;

  @Override
  public Category createCategory(CategoryDTO categoryDTO) {
    Category newCategory = Category.builder()
      .name(categoryDTO.getName())
      .build();
    return categoryRepository.save(newCategory);
  }

  @Override
  public Category getCategoryById(Long id) {
    return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Category with ID = %d not found.", id)));
  }

  @Override
  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  @Override
  public Category updateCategory(Long categoryId, @RequestBody CategoryDTO categoryDTO) {
    Category existingCategory = getCategoryById(categoryId);
    existingCategory.setName(categoryDTO.getName());
    categoryRepository.save(existingCategory);
    return existingCategory;
  }

  @Override
  public void deleteCategory(Long id) {
    categoryRepository.deleteById(id);
  }
}
