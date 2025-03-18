package com.levelUp360.eCommerce.repository;

import com.levelUp360.eCommerce.entity.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FAQRepository extends JpaRepository<FAQ, Long> {

}
