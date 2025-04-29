package com.levelUp360.eCommerce.entity;

import com.levelUp360.eCommerce.dto.FAQDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Queries")
public class FAQ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    private String answer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;


    public FAQDto getFAQDto(){
        FAQDto faqDto = new FAQDto();
        faqDto.setId(id);
        faqDto.setQuestion(question);
        faqDto.setAnswer(answer);
        faqDto.setProduct(product.getId());

        return faqDto;
    }
}
