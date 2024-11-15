package com.levelUp360.eCommerce.services.admin.product;

import com.levelUp360.eCommerce.dto.ProductDto;
import com.levelUp360.eCommerce.entity.Category;
import com.levelUp360.eCommerce.entity.Product;
import com.levelUp360.eCommerce.repository.CategoryRepository;
import com.levelUp360.eCommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl  implements ProductService{

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    @Override
    public ProductDto addProduct(ProductDto productDto) throws IOException {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setImg(productDto.getFile().getBytes());

        Category category = categoryRepository.findById(productDto.getCategoryId()).orElseThrow();
        product.setCategory(category);

        return productRepository.save(product).getDto();
    }

    public List<ProductDto> getAllProducts(){
       List<Product> product = productRepository.findAll();
       return product.stream().map(Product::getDto).collect(Collectors.toList());
    }
}
