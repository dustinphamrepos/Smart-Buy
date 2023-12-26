package com.project.smartbuy.controllers;

import com.project.smartbuy.dtos.OrderDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {

  @PostMapping("")
  public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO) {
    return ResponseEntity.ok("Create order detail here.");
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") Long id) {
    return ResponseEntity.ok(String.format("Get order detail with id = %d", id));
  }

  @GetMapping("/order/{orderId}")
  public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("orderId") Long orderId) {
    return ResponseEntity.ok(String.format("Get order detail with Id = %d", orderId));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateOrderDetail(@Valid @PathVariable("id") Long id,
                                             @RequestBody OrderDetailDTO newOrderDetailData) {
    return ResponseEntity.ok(String.format("update order detail with id = %d and new data is %s", id, newOrderDetailData));
  }

  @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderDetail(@Valid @PathVariable("id") Long id) {
    return ResponseEntity.noContent().build();
  }
}
