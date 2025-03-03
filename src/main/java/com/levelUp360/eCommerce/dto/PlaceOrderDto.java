package com.levelUp360.eCommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderDto {

    private Long userId;

    private String address;

    private String orderDescription;
}
