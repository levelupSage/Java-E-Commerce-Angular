package com.levelUp360.eCommerce.services.admin.category;

import com.levelUp360.eCommerce.dto.CategoryDto;
import com.levelUp360.eCommerce.entity.Category;
import org.springframework.stereotype.Service;

public interface CategoryService {

    Category creatCategory(CategoryDto categoryDto);

}
