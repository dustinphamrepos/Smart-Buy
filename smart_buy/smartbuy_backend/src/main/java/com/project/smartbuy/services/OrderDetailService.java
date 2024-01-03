package com.project.smartbuy.services;

import com.project.smartbuy.dtos.OrderDTO;
import com.project.smartbuy.dtos.OrderDetailDTO;
import com.project.smartbuy.exceptions.DataNotFoundException;
import com.project.smartbuy.models.Order;
import com.project.smartbuy.models.OrderDetail;
import com.project.smartbuy.models.Product;
import com.project.smartbuy.repositories.OrderDetailRepository;
import com.project.smartbuy.repositories.OrderRepository;
import com.project.smartbuy.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService{
  private final OrderRepository orderRepository;
  private final OrderDetailRepository orderDetailRepository;
  private  final ProductRepository productRepository;
  private final ModelMapper modelMapper;
  @Override
  public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
    //Find if order exists
    Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
      .orElseThrow(() -> new DataNotFoundException(
        String.format("Cannot find order with ID = %d.", orderDetailDTO.getOrderId())));
    //Find if product exists
    Product existingProduct = productRepository.findById(orderDetailDTO.getProductId())
      .orElseThrow(() -> new DataNotFoundException(
        String.format("Cannot find product with ID = %d.", orderDetailDTO.getProductId())));

    OrderDetail orderDetail = OrderDetail.builder()
      .order(existingOrder)
      .product(existingProduct)
      .price(orderDetailDTO.getPrice())
      .numberOfProducts(orderDetailDTO.getNumberOfProducts())
      .totalMoney(orderDetailDTO.getTotalMoney())
      .color(orderDetailDTO.getColor())
      .build();
    return orderDetailRepository.save(orderDetail);
  }

  @Override
  public OrderDetail getOrderDetail(Long id) throws Exception {
      return orderDetailRepository.findById(id)
        .orElseThrow(() -> new DataNotFoundException(
          String.format("Cannot find order with ID = %d.", id)));
  }

  @Override
  public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws Exception {
    // Check for null value of orderDetailDTO
    Objects.requireNonNull(orderDetailDTO, "OrderDetailDTO cannot be null");
    //Find if orderDetail exists
    OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
      .orElseThrow(() -> new DataNotFoundException(
        String.format("Cannot find orderDetail with ID = %d.", id)));
    //Find if order exists
    Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
      .orElseThrow(() -> new DataNotFoundException(
        String.format("Cannot find order with ID = %d.", orderDetailDTO.getOrderId())));
    //Find if product exists
    Product existingProduct = productRepository.findById(orderDetailDTO.getProductId())
      .orElseThrow(() -> new DataNotFoundException(
        String.format("Cannot find product with ID = %d.", orderDetailDTO.getProductId())));
    //convert orderDTO => order: Use the ModelMapper library for speed
    //mapping controller
    // way 1 false because OrderDetail's setId is a field, mapping is inevitable
//    modelMapper.typeMap(OrderDetailDTO.class, OrderDetail.class)
//      .addMappings(mapper -> mapper.skip(OrderDetail::setId));
//    modelMapper.map(orderDetailDTO, existingOrderDetail);
//    existingOrderDetail.setOrder(existingOrder);
//    existingOrderDetail.setProduct(existingProduct);

    // way 2:
    //Check and assign value from orderDetailDTO to existingOrderDetail
    if (orderDetailDTO.getPrice() != null) {
      existingOrderDetail.setPrice(orderDetailDTO.getPrice());
    }

    //No need to check because the data type is int
    existingOrderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());

    if (orderDetailDTO.getTotalMoney() != null) {
      existingOrderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
    }

    if (orderDetailDTO.getColor() != null) {
      existingOrderDetail.setColor(orderDetailDTO.getColor());
    }

    // assign order và product
    existingOrderDetail.setOrder(existingOrder);
    existingOrderDetail.setProduct(existingProduct);

    return orderDetailRepository.save(existingOrderDetail);
  }

  @Override
  public void deleteOrderDetail(Long id) {
    orderDetailRepository.deleteById(id);
  }

  @Override
  public List<OrderDetail> getOrderDetailsByOrderId(Long orderId) {
    return orderDetailRepository.findByOrderId(orderId);
  }
}
