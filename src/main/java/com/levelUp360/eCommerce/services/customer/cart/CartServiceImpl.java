package com.levelUp360.eCommerce.services.customer.cart;

import com.levelUp360.eCommerce.dto.AddProductInCartDto;
import com.levelUp360.eCommerce.dto.CartItemsDto;
import com.levelUp360.eCommerce.dto.OrderDto;
import com.levelUp360.eCommerce.entity.CartItems;
import com.levelUp360.eCommerce.entity.Order;
import com.levelUp360.eCommerce.entity.Product;
import com.levelUp360.eCommerce.entity.User;
import com.levelUp360.eCommerce.enums.OrderStatus;
import com.levelUp360.eCommerce.repository.CartItemsRepository;
import com.levelUp360.eCommerce.repository.OrderRepository;
import com.levelUp360.eCommerce.repository.ProductRepository;
import com.levelUp360.eCommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemsRepository cartItemsRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    //  public ResponseEntity<?> addProductToCart(AddProductInCartDto addProductInCartDto) {
//        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(), OrderStatus.Pending);
//
//
//        Optional<CartItems> optionalCartItems = cartItemsRepository.findByProductIdAndUserIdAndOrderId
//                (addProductInCartDto.getProductId(), activeOrder.getId(), addProductInCartDto.getUserId());
//
//        if (optionalCartItems.isPresent()) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
//        } else {
//            Optional<Product> optionalProduct = productRepository.findById(addProductInCartDto.getProductId());
//            Optional<User> optionalUser = userRepository.findById(addProductInCartDto.getUserId());
//
//            if (optionalProduct.isPresent() && optionalUser.isPresent()) {
//                CartItems cartItems = new CartItems();
//                cartItems.setProduct(optionalProduct.get());
//                cartItems.setPrice(optionalProduct.get().getPrice());
//                cartItems.setQuantity(1L);
//                cartItems.setUser(optionalUser.get());
//                cartItems.setOrder(activeOrder);
//
//                CartItems updatedCart = cartItemsRepository.save(cartItems);
//
//                activeOrder.setTotalAmount(activeOrder.getTotalAmount() + cartItems.getPrice());
//                activeOrder.setAmount(activeOrder.getAmount() + cartItems.getPrice());
//                activeOrder.getCartItems().add(cartItems);
//
//                orderRepository.save(activeOrder);
//
//                return ResponseEntity.status(HttpStatus.CREATED).body(cartItems);
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or Product Not Found");
//            }
//
//        }
//    }
//
//    public OrderDto getCartByUserId(Long userId) {
//        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.Pending);
//        List<CartItemsDto> cartItemsDtoList = activeOrder.getCartItems().stream().map(CartItems::getCartDto).collect(Collectors.toList());
//        OrderDto orderDto = new OrderDto();
//        orderDto.setAmount(activeOrder.getAmount());
//        orderDto.setId(activeOrder.getId());
//        orderDto.setOrderStatus(activeOrder.getOrderStatus());
//        orderDto.setDiscount(activeOrder.getDiscount());
//        orderDto.setTotalAmount(activeOrder.getTotalAmount());
//        orderDto.setCartItemsDto(cartItemsDtoList);
//        return orderDto;
//    }
//}
    public ResponseEntity<?> addProductToCart(AddProductInCartDto addProductInCartDto) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(), OrderStatus.Pending);

        if (activeOrder == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Active order not found for user.");
        }

        Optional<CartItems> optionalCartItems = cartItemsRepository.findByProductIdAndUserIdAndOrderId(
                addProductInCartDto.getProductId(), activeOrder.getId(), addProductInCartDto.getUserId());

        if (optionalCartItems.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Product already in cart.");
        }

        Optional<Product> optionalProduct = productRepository.findById(addProductInCartDto.getProductId());
        Optional<User> optionalUser = userRepository.findById(addProductInCartDto.getUserId());

        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        // If all checks pass, proceed with adding product to cart
        CartItems cartItems = new CartItems();
        cartItems.setProduct(optionalProduct.get());
        cartItems.setPrice(optionalProduct.get().getPrice());
        cartItems.setQuantity(1L);
        cartItems.setUser(optionalUser.get());
        cartItems.setOrder(activeOrder);

        CartItems updatedCart = cartItemsRepository.save(cartItems);

        activeOrder.setTotalAmount(activeOrder.getTotalAmount() + cartItems.getPrice());
        activeOrder.setAmount(activeOrder.getAmount() + cartItems.getPrice());
        activeOrder.getCartItems().add(cartItems);

        orderRepository.save(activeOrder);

        return ResponseEntity.status(HttpStatus.CREATED).body(updatedCart);
    }

    public OrderDto getCartByUserId(Long userId) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.Pending);

        if (activeOrder == null) {
            return null; // Consider returning an empty `OrderDto` instead of null
        }

        List<CartItemsDto> cartItemsDtoList = (activeOrder.getCartItems() != null)
                ? activeOrder.getCartItems().stream().map(CartItems::getCartDto).collect(Collectors.toList())
                : List.of();

        OrderDto orderDto = new OrderDto();
        orderDto.setAmount(activeOrder.getAmount() != null ? activeOrder.getAmount() : (long) 0.0);
        orderDto.setId(activeOrder.getId());
        orderDto.setOrderStatus(activeOrder.getOrderStatus());
        orderDto.setDiscount(activeOrder.getDiscount() != null ? activeOrder.getDiscount() : (long) 0.0);
        orderDto.setTotalAmount(activeOrder.getTotalAmount() != null ? activeOrder.getTotalAmount() : (long) 0.0);
        orderDto.setCartItemsDto(cartItemsDtoList);

        return orderDto;
    }
}