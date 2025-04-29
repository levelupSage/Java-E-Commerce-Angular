package com.levelUp360.eCommerce.services.adminOrder;

import com.levelUp360.eCommerce.dto.OrderDto;

import java.util.List;

public interface AdminOrderService {

    List<OrderDto> getAllPlaceOrders();

    OrderDto changeOrderStatus(Long orderId, String status);
}
