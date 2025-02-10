package com.levelUp360.eCommerce.repository;

import com.levelUp360.eCommerce.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Boolean existsByCode(String code);
}
