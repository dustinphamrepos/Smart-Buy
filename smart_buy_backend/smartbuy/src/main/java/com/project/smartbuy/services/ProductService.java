package com.project.smartbuy.services;

import com.project.smartbuy.dtos.ProductDTO;
import com.project.smartbuy.dtos.ProductImageDTO;
import com.project.smartbuy.exceptions.DataNotFoundException;
import com.project.smartbuy.exceptions.InvalidParamException;
import com.project.smartbuy.models.Category;
import com.project.smartbuy.models.Product;
import com.project.smartbuy.models.ProductImage;
import com.project.smartbuy.repositories.CategoryRepository;
import com.project.smartbuy.repositories.ProductImageRepository;
import com.project.smartbuy.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final ProductImageRepository productImageRepository;
  @Override
  public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
    Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
      .orElseThrow(() ->
        new DataNotFoundException(String.format("Can not find category with id = %d", productDTO.getCategoryId())));
    Product newProduct = Product.builder()
      .name(productDTO.getName())
      .price(productDTO.getPrice())
      .thumbnail(productDTO.getThumbnail())
      .category(existingCategory)
      .build();
    return productRepository.save(newProduct);
  }

  @Override
  public Product getProductById(Long id) throws Exception {
    return productRepository.findById(id)
      .orElseThrow(() -> new DataNotFoundException(String.format("Can not find product with id = %d", id)));
  }

  @Override
  public Page<Product> getAllProducts(PageRequest pageRequest) {
    //get product list by page and limit
    return productRepository.findAll(pageRequest);
  }

  @Override
  public Product updateProduct(Long id, ProductDTO productDTO) throws Exception {
    Product existingProduct = getProductById(id);
    if (existingProduct != null) {
      Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
        .orElseThrow(() ->
          new DataNotFoundException(String.format("Can not find category with id = %d", productDTO.getCategoryId())));
      existingProduct.setName(productDTO.getName());
      existingProduct.setCategory(existingCategory);
      existingProduct.setPrice(productDTO.getPrice());
      existingProduct.setDescription(productDTO.getDescription());
      existingProduct.setThumbnail(productDTO.getThumbnail());
      return productRepository.save(existingProduct);
    }
    return null;
  }

  @Override
  public void deleteProduct(Long id) {
    Optional<Product> optionalProduct = productRepository.findById(id);
      optionalProduct.ifPresent(productRepository::delete);
  }

  @Override
  public boolean existsByName(String name) {
    return productRepository.existsByName(name);
  }

  @Override
  public ProductImage createProductImage(Long productId,
                                          ProductImageDTO productImageDTO
  ) throws Exception {
    Product existingProduct = productRepository.findById(productId)
      .orElseThrow(() ->
        new DataNotFoundException(String
          .format("Can not find category with id = %d", productImageDTO.getProductId())));

    ProductImage newProductImage = ProductImage.builder()
      .product(existingProduct)
      .imageUrl(productImageDTO.getImageUrl())
      .build();
    //Do not allow more than 5 images to be inserted per product
    int size = productImageRepository.findByProductId(productId).size();

    if (size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
      throw new InvalidParamException(String.format("Number of images must be <= %d", ProductImage.MAXIMUM_IMAGES_PER_PRODUCT));
    }
    return productImageRepository.save(newProductImage);
  }
}
