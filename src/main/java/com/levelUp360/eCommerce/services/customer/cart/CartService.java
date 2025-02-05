package com.levelUp360.eCommerce.services.customer.cart;

import com.levelUp360.eCommerce.dto.AddProductInCartDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

public interface CartService {

    ResponseEntity<?> addProductToCart(AddProductInCartDto addProductInCartDto);
}
