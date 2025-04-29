package com.levelUp360.eCommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FAQDto {

    private Long id;

    private String question;

    private String answer;

    private Long product;
}
