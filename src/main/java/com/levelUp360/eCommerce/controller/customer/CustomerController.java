package com.levelUp360.eCommerce.controller.customer;

import com.levelUp360.eCommerce.dto.ProductDto;
import com.levelUp360.eCommerce.services.customer.customerProduct.CustomerProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerProductService customerProductService;

    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getAllProducts(){
        List<ProductDto> productDto = customerProductService.getAllProducts();
        return ResponseEntity.ok(productDto);
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<List<ProductDto>> getAllProductByName(@PathVariable String name) {
        List<ProductDto> productDto = customerProductService.getSearchProductsByTitle(name);
        return ResponseEntity.ok(productDto);
    }
}
