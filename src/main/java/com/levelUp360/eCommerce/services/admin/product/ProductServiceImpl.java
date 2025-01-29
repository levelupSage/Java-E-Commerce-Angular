package com.levelUp360.eCommerce.services.admin.product;

import com.levelUp360.eCommerce.dto.ProductDto;
import com.levelUp360.eCommerce.entity.Category;
import com.levelUp360.eCommerce.entity.Product;
import com.levelUp360.eCommerce.repository.CategoryRepository;
import com.levelUp360.eCommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl  implements ProductService{

    static Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    @Override
    public ProductDto addProduct(ProductDto productDto) throws IOException {
        try {
            Product product = new Product();
            product.setName(productDto.getName());
            product.setPrice(productDto.getPrice());
            product.setDescription(productDto.getDescription());
            product.setImg(productDto.getFile().getBytes());

            Category category = categoryRepository.findById(productDto.getCategoryId()).orElseThrow();
            product.setCategory(category);

            return productRepository.save(product).getDto();

        } catch (Exception e) {
            logger.error("Exception Occured : ", e.getMessage(), e);
            return null;
        }
    }

    public List<ProductDto> getAllProducts(){
        try{
            List<Product> product = productRepository.findAll();
            return product.stream().map(Product::getDto).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Something Went Wrong.....Please check your All Product" + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<ProductDto> getAllProductByName(String name) {
        try {
            List<Product> obj = productRepository.findAllByNameContaining(name);
            return obj.stream().map(Product::getDto).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Something Went Wrong.....Please check your All Product By Name" + e.getMessage());
            return null;
        }
    }
}
