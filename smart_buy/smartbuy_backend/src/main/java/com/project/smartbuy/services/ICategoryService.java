package com.project.smartbuy.services;

import com.project.smartbuy.dtos.CategoryDTO;
import com.project.smartbuy.models.Category;

import java.util.List;

public interface ICategoryService {
  Category createCategory(CategoryDTO categoryDTO);
  Category getCategoryById(Long id);

  List<Category> getAllCategories();
  Category updateCategory(Long categoryId, CategoryDTO categoryDTO);

  void deleteCategory(Long id);
}
