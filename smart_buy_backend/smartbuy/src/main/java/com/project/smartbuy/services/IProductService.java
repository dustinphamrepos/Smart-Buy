package com.project.smartbuy.services;

import com.project.smartbuy.dtos.ProductDTO;
import com.project.smartbuy.dtos.ProductImageDTO;
import com.project.smartbuy.exceptions.DataNotFoundException;
import com.project.smartbuy.models.Product;
import com.project.smartbuy.models.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductService {
  Product createProduct(ProductDTO productDTO) throws Exception;
  Product getProductById(Long id) throws Exception;
  Page<Product> getAllProducts(PageRequest pageRequest);
  Product updateProduct(Long id, ProductDTO productDTO) throws Exception;
  void deleteProduct(Long id);
  boolean existsByName(String name);
  ProductImage createProductImage(Long productId,
                                         ProductImageDTO productImageDTO
  ) throws Exception;
}
