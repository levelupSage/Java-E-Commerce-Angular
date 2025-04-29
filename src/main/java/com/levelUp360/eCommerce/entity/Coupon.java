package com.levelUp360.eCommerce.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;


@Entity
@Data
@Table(name="coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String code;

    private Long discount;

    private Date expirationDate;

}
