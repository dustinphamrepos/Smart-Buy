package com.project.smartbuy.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;


@Entity
@Table(name = "order_details")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  @Column(name = "price", nullable = false)
  @Min(value = 0, message = "Total money must be >= 0.")
  private Float price;

  @Column(name = "number_of_products", nullable = false)
  @Min(value = 0, message = "number of products must be > 0.")
  private Integer numberOfProducts;

  @Column(name = "total_money", nullable = false)
  @Min(value = 0, message = "Total money must be >= 0.")
  private Integer totalMoney;

  private String color;
}
