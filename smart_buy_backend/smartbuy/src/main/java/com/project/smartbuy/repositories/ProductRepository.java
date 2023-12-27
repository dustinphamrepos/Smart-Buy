package com.project.smartbuy.repositories;

import com.project.smartbuy.models.Product;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
  boolean existsByName(String name);
  Page<Product> findAll(Pageable pageable);
}
