package com.project.smartbuy.controllers;


import com.project.smartbuy.dtos.OrderDTO;
import com.project.smartbuy.responses.OrderResponse;
import com.project.smartbuy.services.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
  private final IOrderService orderService;
  @PostMapping("")
  public ResponseEntity<?> createOrder(@RequestBody @Valid OrderDTO orderDTO, BindingResult result) {
    try {
      if (result.hasErrors()) {
        List<String> errorMessages = result.getFieldErrors()
          .stream()
          .map(FieldError::getDefaultMessage)
          .toList();
        return ResponseEntity.badRequest().body(errorMessages);
      }
      OrderResponse orderResponse = orderService.createOrder(orderDTO);
      return ResponseEntity.ok(orderResponse);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/{user_id}")
  public ResponseEntity<?> getOrders(@Valid @PathVariable("user_id") Long user_id) {
    try {
      return ResponseEntity.ok(String.format("Danh sach order cua user co ID la: %d", user_id));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateOrder (@Valid @PathVariable Long id, @Valid @RequestBody OrderDTO orderDTO) {
    return ResponseEntity.ok("Cap nhat thong tin don hang.");
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteOrder (@Valid @PathVariable Long id) {
    return ResponseEntity.ok("Order deleted successfully.");
  }
}
