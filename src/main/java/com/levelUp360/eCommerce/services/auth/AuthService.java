package com.levelUp360.eCommerce.services.auth;

import com.levelUp360.eCommerce.dto.SignupRequest;
import com.levelUp360.eCommerce.dto.UserDto;

public interface AuthService {

    UserDto createUser(SignupRequest signupRequest);

    Boolean hasUserWithEmail(String email);
}
