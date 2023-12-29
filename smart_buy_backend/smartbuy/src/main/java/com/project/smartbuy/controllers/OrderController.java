package com.project.smartbuy.controllers;


import com.project.smartbuy.dtos.OrderDTO;
import com.project.smartbuy.exceptions.DataNotFoundException;
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
  // POST http://localhost:8088/apic/orders
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

  @GetMapping("/user/{user_id}")
  // GET http://localhost:8088/api/v1/orders/user/6
  public ResponseEntity<?> getOrdersByUserId(@Valid @PathVariable("user_id") Long user_id) {
    try {
      List<OrderResponse> orderResponses = orderService.findByUserId(user_id);
      return ResponseEntity.ok(orderResponses);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/{id}")
  // GET http://localhost:8088/api/v1/orders/2
  public ResponseEntity<?> getOrder(@Valid @PathVariable("id") Long orderId) {
    try {
      OrderResponse orderResponse = orderService.getOrder(orderId);
      return ResponseEntity.ok(orderResponse);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PutMapping("/{id}")
  // PUT http://localhost:8088/api/v1/orders/6
  public ResponseEntity<?> updateOrder (@Valid @PathVariable Long id, @Valid @RequestBody OrderDTO orderDTO) {
    try {
      OrderResponse orderResponse = orderService.updateOrder(id, orderDTO);
      return ResponseEntity.ok(orderResponse);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  // DELETE http://localhost:8088/api/v1/orders/6
  public ResponseEntity<?> deleteOrder (@Valid @PathVariable Long id) throws DataNotFoundException {
    orderService.deleteOrder(id);
    return ResponseEntity.ok(String.format("Order with ID = %d deleted successfully.", id));
  }
}
