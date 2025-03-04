package com.levelUp360.eCommerce.services.adminOrder;

import com.levelUp360.eCommerce.dto.OrderDto;
import com.levelUp360.eCommerce.entity.Order;
import com.levelUp360.eCommerce.enums.OrderStatus;
import com.levelUp360.eCommerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
