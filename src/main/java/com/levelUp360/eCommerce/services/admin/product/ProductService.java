package com.levelUp360.eCommerce.services.admin.product;

import com.levelUp360.eCommerce.dto.ProductDto;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    ProductDto addProduct(ProductDto productDto) throws IOException;

    List<ProductDto> getAllProducts();

    List<ProductDto> getAllProductsByName(String name);

   Boolean deleteProduct(Long id);
}
