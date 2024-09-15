package com.levelUp360.eCommerce.dto;

import com.levelUp360.eCommerce.enums.UserRole;
import lombok.Data;

@Data
public class UserDto {

    private Long id;

    private String email;

    private String name;

    private UserRole userRole;

}
