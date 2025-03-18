package com.levelUp360.eCommerce.services.admin.faq;

import com.levelUp360.eCommerce.dto.FAQDto;

public interface FAQService {

    FAQDto postFAQ(Long productId, FAQDto faqDto);
}
