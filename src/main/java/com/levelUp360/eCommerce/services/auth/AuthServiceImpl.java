package com.levelUp360.eCommerce.services.auth;

import com.levelUp360.eCommerce.dto.SignupRequest;
import com.levelUp360.eCommerce.dto.UserDto;
import com.levelUp360.eCommerce.entity.Order;
import com.levelUp360.eCommerce.entity.User;
import com.levelUp360.eCommerce.enums.OrderStatus;
import com.levelUp360.eCommerce.enums.UserRole;
import com.levelUp360.eCommerce.repository.OrderRepository;
import com.levelUp360.eCommerce.repository.UserRepository;
import com.levelUp360.eCommerce.utils.JwtUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private OrderRepository orderRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    public UserDto createUser(SignupRequest signupRequest){

        //I am write this code one cart is automatic generated one user
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setName(signupRequest.getName());
        user.setPassword(bCryptPasswordEncoder.encode(signupRequest.getPassword()));
        user.setRole(UserRole.CUSTOMER);
        User createdUser = userRepository.save(user);

        //I am write this code one cart is automatic generated one order
        Order order = new Order();
        order.setAmount(0L);
        order.setTotalAmount(0L);
        order.setDiscount(0L);
        order.setUser(createdUser);
        order.setOrderStatus(OrderStatus.Pending);
        orderRepository.save(order);

        UserDto userDto = new UserDto();
        userDto.setId(createdUser.getId());
        return userDto;
    }

    public Boolean hasUserWithEmail(String email){
        return userRepository.findFirstByEmail(email).isPresent();
    }

    @PostConstruct
    public void createAdminAccount(){
        User adminAccount = userRepository.findByRole(UserRole.ADMIN);
        if(null == adminAccount){
            User user = new User();
            user.setEmail("admin@test.com");
            user.setName("admin");
            user.setRole(UserRole.ADMIN);
            user.setImg(null);
            user.setPassword(bCryptPasswordEncoder.encode("admin"));
            userRepository.save(user);
        }
    }

    public String verify(User users) {
        Authentication authentication =
                manager.authenticate(new UsernamePasswordAuthenticationToken(users.getName(), users.getPassword()));
        if(authentication.isAuthenticated()){
            return jwtUtil.generationToken(users.getName());
        }
        return "fail";
    }
}
