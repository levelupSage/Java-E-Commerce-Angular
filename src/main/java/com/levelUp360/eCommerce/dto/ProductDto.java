package com.levelUp360.eCommerce.dto;

import com.levelUp360.eCommerce.entity.Category;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;

    private String name;

    private Long  price;

    private String description;

    private byte[] img;

    private Long categoryId;

    private MultipartFile file;
}
