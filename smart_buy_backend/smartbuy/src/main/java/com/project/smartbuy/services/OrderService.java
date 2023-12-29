package com.project.smartbuy.services;

import com.project.smartbuy.dtos.OrderDTO;
import com.project.smartbuy.exceptions.DataNotFoundException;
import com.project.smartbuy.models.Order;
import com.project.smartbuy.models.OrderStatus;
import com.project.smartbuy.models.User;
import com.project.smartbuy.repositories.OrderRepository;
import com.project.smartbuy.repositories.UserRepository;
import com.project.smartbuy.responses.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{

  private final OrderRepository orderRepository;
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;
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
    //modelMapper.typeMap(Order.class, OrderResponse.class);
    return modelMapper.map(order, OrderResponse.class);
  }

  @Override
  public OrderResponse getOrder(Long id) {
    return null;
  }

  @Override
  public OrderResponse updateOrder(Long id, OrderDTO orderDTO) {
    return null;
  }

  @Override
  public void deleteOrder(Long id) {

  }

  @Override
  public List<OrderResponse> getAllOrders(Long userId) {
    return null;
  }
}
