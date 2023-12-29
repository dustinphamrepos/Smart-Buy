package com.project.smartbuy.services;

import com.project.smartbuy.dtos.OrderDTO;
import com.project.smartbuy.exceptions.DataNotFoundException;
import com.project.smartbuy.responses.OrderResponse;

import java.util.List;

public interface IOrderService {
  OrderResponse createOrder (OrderDTO orderDTO) throws Exception;
  OrderResponse getOrder(Long id) throws DataNotFoundException;
  OrderResponse updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException;
  void deleteOrder(Long id);
  List<OrderResponse> findByUserId(Long userId);
}
