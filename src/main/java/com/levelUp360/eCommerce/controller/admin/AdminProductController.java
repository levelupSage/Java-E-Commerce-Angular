package com.levelUp360.eCommerce.controller.admin;

import com.levelUp360.eCommerce.dto.FAQDto;
import com.levelUp360.eCommerce.dto.ProductDto;
import com.levelUp360.eCommerce.services.admin.faq.FAQService;
import com.levelUp360.eCommerce.services.admin.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
public class AdminProductController {

    static Logger logger = LoggerFactory.getLogger(AdminProductController.class);

    private final ProductService productService;

    private final FAQService faqService;

    @PostMapping("/product")
    public ResponseEntity<ProductDto> addProduct(@ModelAttribute ProductDto productDto) throws IOException {
        try {
            ProductDto productDto1 = productService.addProduct(productDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(productDto1);
        } catch (Exception e) {
            logger.error("Exception Occurd : " + e.getMessage());
        }
        return null;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        try {
            List<ProductDto> productDto = productService.getAllProducts();
            return ResponseEntity.ok(productDto);
        } catch (Exception e) {
            logger.error("Exception Occurd : " + e.getMessage());
        }
        return null;
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<List<ProductDto>> getAllProductByName(@PathVariable String name) {
        try {
            List<ProductDto> productDto = productService.getAllProductsByName(name);
            return ResponseEntity.ok(productDto);
        } catch (Exception e) {
            logger.error("Exception Occurd : " + e.getMessage());
        }
        return null;
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        try {
            Boolean obj = productService.deleteProduct(id);
            if (obj) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Exception Occurd : " + e.getMessage());
            return null;
        }
    }

    @PostMapping("/faq/{productId}")
    public ResponseEntity<FAQDto> postFAQ(@PathVariable Long productId, @RequestBody FAQDto faqDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(faqService.postFAQ(productId, faqDto));
        } catch (Exception e) {
            logger.error("Exception Occurd : " + e.getMessage());
        }
        return null;
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId) {
        try {
            ProductDto productDto = productService.getProductId(productId);
            if (null != productDto) {
                return ResponseEntity.ok(productDto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Exception Occurd : " + e.getMessage());
            return null;
        }
    }

    @PutMapping("/product/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long productId, @ModelAttribute ProductDto productDto) {
        try {
            ProductDto updateProductDto = productService.updateProduct(productId, productDto);
            if (updateProductDto != null) {
                return ResponseEntity.ok(productDto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Exception Occurd : " + e.getMessage());
            return null;
        }
    }
}
