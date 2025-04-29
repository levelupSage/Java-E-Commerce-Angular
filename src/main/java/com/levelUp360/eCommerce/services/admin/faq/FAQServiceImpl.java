package com.levelUp360.eCommerce.services.admin.faq;

import com.levelUp360.eCommerce.dto.FAQDto;
import com.levelUp360.eCommerce.entity.FAQ;
import com.levelUp360.eCommerce.entity.Product;
import com.levelUp360.eCommerce.repository.FAQRepository;
import com.levelUp360.eCommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FAQServiceImpl implements FAQService {

     static Logger logger = LoggerFactory.getLogger(FAQServiceImpl.class);

    private final FAQRepository faqRepository;

    private final ProductRepository productRepository;

    public FAQDto postFAQ(Long productId, FAQDto faqDto){
        try{
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if(optionalProduct.isPresent()){
                FAQ faq = new FAQ();

                faq.setQuestion(faqDto.getQuestion());
                faq.setAnswer(faqDto.getAnswer());
                faq.setProduct(optionalProduct.get());

                return faqRepository.save(faq).getFAQDto();
            }
        }catch (Exception e){
            logger.error("Exception Occurd : " + e.getMessage());
        }
        return null;
    }

}
