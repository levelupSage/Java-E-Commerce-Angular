package com.levelUp360.eCommerce.controller;

import com.levelUp360.eCommerce.dto.AuthenticationRequest;
import com.levelUp360.eCommerce.dto.SignupRequest;
import com.levelUp360.eCommerce.dto.UserDto;
import com.levelUp360.eCommerce.entity.User;
import com.levelUp360.eCommerce.repository.UserRepository;
import com.levelUp360.eCommerce.services.auth.AuthService;
import com.levelUp360.eCommerce.services.jwt.UserDetailsServiceImpl;
import com.levelUp360.eCommerce.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsServiceImpl userDetailsService;

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    @Autowired
    private AuthService authService;

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    @PostMapping("/authenticate")
    public void createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse repoResponse) throws IOException, JSONException {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()));
        }catch (BadCredentialsException e){
            throw new BadCredentialsException("Incorrect Username and Password.");
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());
        final String jwt = jwtUtil.generationToken(userDetails.getUsername());
        if(optionalUser.isPresent()){
            repoResponse.getWriter().write(new JSONObject()
                    .put("UserId", optionalUser.get().getId())
                    .put("role", optionalUser.get().getRole())
                    .put("token", jwt)
                    .toString());
        }

        repoResponse.addHeader("Access-Control-Expose-Headers", "Authorization");
        repoResponse.addHeader("Access-Control-Allow-Headers", "Authorization, X-PINGOTHER" +
                "X-Requested-With, Content-Type, Accept, X-Custom-header");
        repoResponse.addHeader(HEADER_STRING, TOKEN_PREFIX + jwt);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest){
        if(authService.hasUserWithEmail(signupRequest.getEmail())){
            return new ResponseEntity<>("User already exists", HttpStatus.NOT_ACCEPTABLE);
        }
        UserDto userDto = authService.createUser(signupRequest);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
