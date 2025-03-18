package com.levelUp360.eCommerce.services.adminOrder;

import com.levelUp360.eCommerce.dto.OrderDto;
import com.levelUp360.eCommerce.entity.Order;
import com.levelUp360.eCommerce.enums.OrderStatus;
import com.levelUp360.eCommerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {

    private final OrderRepository orderRepository;

    @Override
    public List<OrderDto> getAllPlaceOrders(){
        List<Order> orderList = orderRepository
                .findAllByOrderStatusIn(List.of(OrderStatus.Placed, OrderStatus.Shipped, OrderStatus.Delivered));
        return orderList.stream().map(Order::getOrderDto).collect(Collectors.toList());

    }

    @Override
    public OrderDto changeOrderStatus(Long orderId, String status){
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if(optionalOrder.isPresent()){
            Order order = optionalOrder.get();

            if(Objects.equals(status, "Shipped")){
                order.setOrderStatus(OrderStatus.Shipped);
            }else if(Objects.equals(status, "Delivered")){
                order.setOrderStatus(OrderStatus.Delivered);
            }
            return orderRepository.save(order).getOrderDto();
        }
        return null;
    }
}
