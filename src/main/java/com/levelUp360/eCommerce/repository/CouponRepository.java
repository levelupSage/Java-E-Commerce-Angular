package com.levelUp360.eCommerce.repository;

import com.levelUp360.eCommerce.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Boolean existsByCode(String code);

    Optional<Coupon> findByCode(String code);
}
