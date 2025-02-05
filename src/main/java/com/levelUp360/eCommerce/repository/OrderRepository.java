package com.levelUp360.eCommerce.repository;

import com.levelUp360.eCommerce.entity.Order;
import com.levelUp360.eCommerce.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByUserIdAndOrderStatus(Long userId, OrderStatus orderStatus);
}
