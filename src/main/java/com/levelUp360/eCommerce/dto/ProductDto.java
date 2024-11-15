package com.levelUp360.eCommerce.dto;

import com.levelUp360.eCommerce.entity.Category;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductDto {

    private Long id;

    private String name;

    private Long  price;

    private String description;

    private byte[] img;

    private Long categoryId;

    private MultipartFile file;
}
