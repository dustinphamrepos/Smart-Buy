package com.project.smartbuy.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "orders")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "fullname", length = 100)
  private String fullName;

  @Column(name = "email", length = 100)
  private String email;

  @Column(name = "phone_number", length = 15, nullable = false)
  private String phoneNumber;

  @Column(name = "address", length = 200, nullable = false)
  private String address;

  @Column(name = "note", length = 100)
  private String note;

  @Column(name = "order_date")
  private Date orderDate;

  @Column(name = "status")
  private String status;

  @Column(name = "total_money")
  @Min(value = 0, message = "Total money must be >= 0.")
  private Float totalMoney;

  @Column(name = "shipping_method", length = 100)
  private String shippingMethod;

  @Column(name = "shipping_address", length = 200)
  private String shippingAddress;

  @Column(name = "shipping_date")
  private LocalDate shippingDate;

  @Column(name = "tracking_number", length = 100)
  private String trackingNumber;

  @Column(name = "payment_method", length = 100)
  private String paymentMethod;

  private Boolean active;
}
