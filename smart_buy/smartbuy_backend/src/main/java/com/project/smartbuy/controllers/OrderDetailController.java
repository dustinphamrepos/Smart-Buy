package com.project.smartbuy.controllers;

import com.project.smartbuy.components.LocalizationUtils;
import com.project.smartbuy.dtos.OrderDetailDTO;
import com.project.smartbuy.models.OrderDetail;
import com.project.smartbuy.responses.OrderDetailListResponse;
import com.project.smartbuy.responses.OrderDetailResponse;
import com.project.smartbuy.responses.ProductListResponse;
import com.project.smartbuy.responses.ProductResponse;
import com.project.smartbuy.services.OrderDetailService;
import com.project.smartbuy.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailController {

  private final OrderDetailService orderDetailService;
  private final LocalizationUtils localizationUtils;

  @PostMapping("")
  // POST http://localhost:8088/api/v1/orders_details
  public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO) {
    try {
      OrderDetail newOrderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
      return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(newOrderDetail));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/{id}")
  // GET http://localhost:8088/api/v1/orders_details/1
  public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") Long id) {
    try {
      OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
      return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetail));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/order/{orderId}")
  // GET http://localhost:8088/api/v1/orders_details/order/1
  public ResponseEntity<OrderDetailListResponse> getOrderDetailsByOrderId(@Valid @PathVariable("orderId") Long orderId) {
    List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(orderId);
    List<OrderDetailResponse> orderDetailResponses = orderDetails
      .stream().map(OrderDetailResponse::fromOrderDetail)
      .collect(Collectors.toList());
    return ResponseEntity.ok(OrderDetailListResponse.builder()
      .orderDetails(orderDetailResponses)
      .build());
  // way 2:
  //    List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
  //    List<OrderDetailResponse> orderDetailResponses = orderDetails.stream()
  //      .map(OrderDetailResponse::fromOrderDetail)
  //      .collect(Collectors.toList());
  //
  //    return ResponseEntity.ok(orderDetailResponses);
  }

  @PutMapping("/{id}")
  // PUT http://localhost:8088/api/v1/orders_details/1
  public ResponseEntity<?> updateOrderDetail(@Valid @PathVariable("id") Long id,
                                             @RequestBody OrderDetailDTO orderDetailDTO) {
    try {
      OrderDetail updatedOrderDetail = orderDetailService.updateOrderDetail(id, orderDetailDTO);
      return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(updatedOrderDetail));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  // DELETE http://localhost:8088/api/v1/orders_details/1
    public ResponseEntity<String> deleteOrderDetail(@Valid @PathVariable("id") Long id) {
    orderDetailService.deleteOrderDetail(id);
    return ResponseEntity.ok().body(localizationUtils.getLocalizedMessage(MessageKeys.DELETE_ORDER_DETAIL_SUCCESSFULLY, id));
  }
}
