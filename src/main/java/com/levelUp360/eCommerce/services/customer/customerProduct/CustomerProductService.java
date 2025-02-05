package com.levelUp360.eCommerce.services.customer.customerProduct;

import com.levelUp360.eCommerce.dto.ProductDto;

import java.util.List;

public interface CustomerProductService {

    List<ProductDto> getAllProducts();

    List<ProductDto> getSearchProductsByTitle(String name);
}
