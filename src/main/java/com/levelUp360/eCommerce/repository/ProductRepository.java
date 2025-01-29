package com.levelUp360.eCommerce.repository;


import com.levelUp360.eCommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository  extends JpaRepository<Product, Long> {

    List<Product> findAllByNameContaining(String Name);
}
