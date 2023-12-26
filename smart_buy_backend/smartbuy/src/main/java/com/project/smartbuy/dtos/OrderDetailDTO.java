package com.project.smartbuy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailDTO {

  @JsonProperty("order_id")
  @Min(value = 0, message = "Order's ID must be > 0.")
  private Long orderId;

  @JsonProperty("product_id")
  @Min(value = 0, message = "Product's ID must be > 0.")
  private Long productId;

  @Min(value = 0, message = "Price must be >= 0.")
  private Long price;

  @Min(value = 0, message = "Number of products must be >= 1.")
  @JsonProperty("number_of_products")
  private int numberOfProducts;

  @JsonProperty("total_money")
  @Min(value = 0, message = "Total money must be > 0.")
  private int totalMoney;

  private String color;
}