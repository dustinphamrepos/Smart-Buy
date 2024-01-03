package com.project.smartbuy.repositories;

import com.project.smartbuy.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
