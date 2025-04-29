package com.levelUp360.eCommerce.entity;

import com.levelUp360.eCommerce.dto.OrderDto;
import com.levelUp360.eCommerce.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderDescription;

    private Date date;

    private Long amount;

    private String address;

    private String payment;

    private OrderStatus orderStatus;

    private Long totalAmount;

    private Long discount;

    private UUID trackingId;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "coupon_id", referencedColumnName = "id")
    private Coupon coupon;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    private List<CartItems> cartItems;

    public OrderDto getOrderDto(){
        OrderDto orderDto = new OrderDto();
        orderDto.setId(id);
        orderDto.setOrderStatus(orderStatus);
        orderDto.setOrderDescription(orderDescription);
        orderDto.setAddress(address);
        orderDto.setTrackingId(trackingId);
        orderDto.setDate(date);
        orderDto.setUserName(user.getName());
        orderDto.setAmount(amount);
        if(null != coupon){
            orderDto.setCouponName(coupon.getName());
        }
        return orderDto;
    }
}
