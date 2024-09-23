package com.levelUp360.eCommerce.entity;

import com.levelUp360.eCommerce.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name="users", schema = "ecommerce")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    private UserRole role;

    @Lob
    @Column(name = "img", columnDefinition = "bytea")
    private byte[] img;


}

