package com.levelUp360.eCommerce.services.admin.coupon;

import com.levelUp360.eCommerce.entity.Coupon;

import java.util.List;

public interface AdminCouponService {

    Coupon createCoupon(Coupon coupon);

    List<Coupon> getAllCoupon();
}
