package com.levelUp360.eCommerce.services.admin.coupon;

import com.levelUp360.eCommerce.Exception.ValidationException;
import com.levelUp360.eCommerce.entity.Coupon;
import com.levelUp360.eCommerce.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCouponServiceImpl implements AdminCouponService{

    private final CouponRepository couponRepository;

    @Override
    public Coupon createCoupon(Coupon coupon){
        if(couponRepository.existsByCode(coupon.getCode())){
            throw new ValidationException("Coupon code already exists.");
        }
        return couponRepository.save(coupon);
    }

    @Override
    public List<Coupon> getAllCoupon(){
        return couponRepository.findAll();
    }
}
