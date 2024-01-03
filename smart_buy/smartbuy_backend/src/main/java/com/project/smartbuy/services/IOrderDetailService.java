package com.project.smartbuy.services;

import com.project.smartbuy.dtos.OrderDetailDTO;
import com.project.smartbuy.exceptions.DataNotFoundException;
import com.project.smartbuy.models.Order;
import com.project.smartbuy.models.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
  OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception;
  OrderDetail getOrderDetail(Long id) throws Exception;
  OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws Exception;
  void deleteOrderDetail(Long id);
  List<OrderDetail> getOrderDetailsByOrderId(Long orderId);
}
