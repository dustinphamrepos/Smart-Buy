package com.project.smartbuy.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.smartbuy.models.Product;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse extends BaseResponse {

  @JsonProperty("name")
  private String name;

  @JsonProperty("price")
  private Float price;

  @JsonProperty("thumbnail")
  private String thumbnail;

  @JsonProperty("description")
  private String description;

  @JsonProperty("category_id")
  private Long categoryId;

  public static ProductResponse fromProduct(Product product) {
    ProductResponse productResponse = ProductResponse.builder()
      .name(product.getName())
      .price(product.getPrice())
      .thumbnail(product.getThumbnail())
      .description(product.getDescription())
      .categoryId(product.getCategory().getId())
      .build();
    productResponse.setCreatedAt(product.getCreatedAt());
    productResponse.setUpdatedAt(product.getUpdatedAt());
    return productResponse;
  }

}
