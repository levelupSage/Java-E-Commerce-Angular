package com.levelUp360.eCommerce.repository;

import com.levelUp360.eCommerce.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Long> {

    Optional<CartItems> findByProductIdAndUserIdAndOrderId(Long productId, long orderId, Long userId);

}
