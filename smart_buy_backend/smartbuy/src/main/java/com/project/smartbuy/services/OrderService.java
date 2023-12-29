package com.project.smartbuy.services;

import com.project.smartbuy.configurations.MapperConfiguration;
import com.project.smartbuy.dtos.OrderDTO;
import com.project.smartbuy.exceptions.DataNotFoundException;
import com.project.smartbuy.models.*;
import com.project.smartbuy.repositories.OrderRepository;
import com.project.smartbuy.repositories.UserRepository;
import com.project.smartbuy.responses.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{

  private final OrderRepository orderRepository;
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;
//  Method 2: If you want to import ModelMapper, then configurations.MapperConfiguration
//  @Service
//  public class OrderService implements IOrderService{
//
//    private final OrderRepository orderRepository;
//    private final UserRepository userRepository;
//    private final ModelMapper modelMapper;
//
//    @Autowired
//    public OrderService(OrderRepository orderRepository, UserRepository userRepository, MapperConfiguration mapperConfiguration) {
//      this.orderRepository = orderRepository;
//      this.userRepository = userRepository;
//      this.modelMapper = mapperConfiguration.modelMapper();
//    }
  @Override
  public OrderResponse createOrder(OrderDTO orderDTO) throws Exception {
    //find user_id exists?
    User user = userRepository.findById(orderDTO.getUserId())
      .orElseThrow(() -> new DataNotFoundException(
        String.format("Cannot find user with id = %d", orderDTO.getUserId())));
    //convert orderDTO => order: Use the ModelMapper library for speed
    //mapping controller
    modelMapper.typeMap(OrderDTO.class, Order.class)
      .addMappings(mapper -> mapper.skip(Order::setId));
    //Update order fields from orderDTO
    Order order = new Order();
    modelMapper.map(orderDTO, order);
    order.setUser(user);
    //get the current time
    order.setOrderDate(new Date());
    order.setStatus(OrderStatus.PENDING);
    //check shipping_date must be later than today
    LocalDate shippingDate = orderDTO.getShippingDate() == null
      ? LocalDate.now() : orderDTO.getShippingDate();
    if (shippingDate.isBefore(LocalDate.now())) {
      throw new DataNotFoundException("Shipping date must be at least today!");
    }
    order.setActive(true);
    order.setShippingDate(shippingDate);
    orderRepository.save(order);
    //There is no need because the fields between the two classes are all equivalent
    //modelMapper.typeMap(Order.class, OrderResponse.class);
    return modelMapper.map(order, OrderResponse.class);
  }

  @Override
  public OrderResponse getOrder(Long id) throws DataNotFoundException {
    Order existingOrder = orderRepository.findById(id)
      .orElseThrow(() -> new DataNotFoundException(String.format("Can not find order with id = %d", id)));
    return modelMapper.map(existingOrder, OrderResponse.class);
  }

  @Override
  public OrderResponse updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException {
      Order existingOrder = orderRepository.findById(id)
        .orElseThrow(() ->
          new DataNotFoundException(String.format("Can not find order with id = %d", id)));

      User existingUser = userRepository.findById(orderDTO.getUserId())
        .orElseThrow(() ->
          new DataNotFoundException(String.format("Can not find user with id = %d", orderDTO.getUserId())));
    //convert orderDTO => order: Use the ModelMapper library for speed
    //mapping controller
    modelMapper.typeMap(OrderDTO.class, Order.class)
      .addMappings(mapper -> mapper.skip(Order::setId));
    //Update order fields from orderDTO
    modelMapper.map(orderDTO, existingOrder);
    existingOrder.setUser(existingUser);
    orderRepository.save(existingOrder);
    //There is no need because the fields between the two classes are all equivalent
    //modelMapper.typeMap(Order.class, OrderResponse.class);
    return modelMapper.map(existingOrder, OrderResponse.class);
  }

  @Override
  public void deleteOrder(Long id) {
    Order existingOrder = orderRepository.findById(id).orElse(null);
    //no hard-delete ==> must soft-delete
    if (existingOrder != null) {
      existingOrder.setActive(false);
      orderRepository.save(existingOrder);
    }
  }

  @Override
  public List<OrderResponse> findByUserId(Long userId) {
    List<Order> orders = orderRepository.findByUserId(userId);
    return orders.stream()
      .map(order -> modelMapper.map(order, OrderResponse.class))
      .collect(Collectors.toList());
  }
}
