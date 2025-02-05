package com.levelUp360.eCommerce.services.customer.customerProduct;

import com.levelUp360.eCommerce.dto.ProductDto;
import com.levelUp360.eCommerce.entity.Product;
import com.levelUp360.eCommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerProductServiceImpl implements CustomerProductService {

    static Logger logger = LoggerFactory.getLogger(CustomerProductServiceImpl.class);

    private final ProductRepository productRepository;

    public List<ProductDto> getAllProducts() {
        try {
            List<Product> product = productRepository.findAll();
            return product.stream().map(Product::getDto).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Something Went Wrong.....Please check your All Product" + e.getMessage(), e);
            return null;
        }
    }

    public List<ProductDto> getSearchProductsByTitle(String name) {
        try {
            List<Product> obj = productRepository.findAllByNameContaining(name);
            return obj.stream().map(Product::getDto).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Something Went Wrong.....Please check your All Product By Name" + e.getMessage());
            return null;
        }
    }

}
