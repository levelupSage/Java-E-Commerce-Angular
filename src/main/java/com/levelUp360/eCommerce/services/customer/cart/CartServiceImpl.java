package com.levelUp360.eCommerce.services.customer.cart;

import com.levelUp360.eCommerce.Exception.ValidationException;
import com.levelUp360.eCommerce.dto.AddProductInCartDto;
import com.levelUp360.eCommerce.dto.CartItemsDto;
import com.levelUp360.eCommerce.dto.OrderDto;
import com.levelUp360.eCommerce.dto.PlaceOrderDto;
import com.levelUp360.eCommerce.entity.*;
import com.levelUp360.eCommerce.enums.OrderStatus;
import com.levelUp360.eCommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    static Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemsRepository cartItemsRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CouponRepository couponRepository;


    @Override
    public ResponseEntity<?> addProductToCart(AddProductInCartDto addProductInCartDto) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(), OrderStatus.Pending);
        if (activeOrder == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Active order not found for user.");
        }

        Optional<CartItems> optionalCartItems = cartItemsRepository.findByProductIdAndUserIdAndOrderId(addProductInCartDto.getProductId(),
                activeOrder.getId(), addProductInCartDto.getUserId());

        if (optionalCartItems.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } else {
            Optional<Product> optionalProduct = productRepository.findById(addProductInCartDto.getProductId());
            Optional<User> optionalUser = userRepository.findById(addProductInCartDto.getUserId());

            if (optionalProduct.isPresent() && optionalUser.isPresent()) {
                CartItems cartItems = new CartItems();
                cartItems.setProduct(optionalProduct.get());
                cartItems.setPrice(optionalProduct.get().getPrice());
                cartItems.setQuantity(1L);
                cartItems.setUser(optionalUser.get());
                cartItems.setOrder(activeOrder);

                //CartItems updatedCart = cartItemsRepository.save(cartItems);
                cartItemsRepository.save(cartItems);

                activeOrder.setTotalAmount((null != activeOrder.getTotalAmount() ? activeOrder.getTotalAmount() : 0) + cartItems.getPrice());
                activeOrder.setAmount((null != activeOrder.getAmount() ? activeOrder.getAmount() : 0) + cartItems.getPrice());
                activeOrder.getCartItems().add(cartItems);

                orderRepository.save(activeOrder);

                return ResponseEntity.status(HttpStatus.CREATED).body(cartItems.getId());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or Product Not Found");
            }
        }
    }

    @Override
    public OrderDto getCartByUserId(Long userId) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.Pending);
        List<CartItemsDto> cartItemsDtoList = (activeOrder.getCartItems() != null)
                ? activeOrder.getCartItems().stream().map(CartItems::getCartDto).collect(Collectors.toList())
                : List.of();

        OrderDto orderDto = new OrderDto();
        orderDto.setAmount(activeOrder.getAmount() != null ? activeOrder.getAmount() : 0);
        orderDto.setId(activeOrder.getId());
        orderDto.setOrderStatus(activeOrder.getOrderStatus());
        orderDto.setDiscount(activeOrder.getDiscount() != null ? activeOrder.getDiscount() : 0);
        orderDto.setTotalAmount(activeOrder.getTotalAmount() != null ? activeOrder.getTotalAmount() : 0);
        orderDto.setCartItemsDto(cartItemsDtoList);
        if (null != activeOrder.getCoupon()) {
            orderDto.setCouponName(activeOrder.getCoupon().getName());
        }

        return orderDto;
    }

    @Override
    public OrderDto applyCoupon(Long userId, String code) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.Pending);
        Coupon coupon = couponRepository.findByCode(code).orElseThrow(() -> new ValidationException("Coupon not found"));
        if (couponIsExpired(coupon)) {
            throw new ValidationException("Coupon is Expired.");
        }
        double discountAmount = ((coupon.getDiscount() / 100.0) * (null != activeOrder.getTotalAmount() ? activeOrder.getAmount() : 0));
        double netAmount = (null != activeOrder.getTotalAmount() ? activeOrder.getAmount() : 0) - discountAmount;

        activeOrder.setAmount((long) netAmount);
        activeOrder.setDiscount((long) discountAmount);
        activeOrder.setCoupon(coupon);

        orderRepository.save(activeOrder);
        return activeOrder.getOrderDto();
    }

    private Boolean couponIsExpired(Coupon coupon) {
        Date curreDate = new Date();
        Date expirationDate = coupon.getExpirationDate();

        return expirationDate != null && curreDate.after(expirationDate);
    }

    @Override
    public OrderDto increaseProductQuantity(AddProductInCartDto addProductInCartDto) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(), OrderStatus.Pending);
        Optional<Product> optionalProduct = productRepository.findById(addProductInCartDto.getProductId());

//        System.err.println("***************************" + activeOrder.getId());
//        System.err.println("***************************" + addProductInCartDto.getProductId());
//        System.err.println("***************************" + addProductInCartDto.getUserId());
        Optional<CartItems> optionalCartItems = cartItemsRepository.findByProductIdAndUserIdAndOrderId(
                addProductInCartDto.getProductId(), addProductInCartDto.getUserId(), activeOrder.getId()
        );

        if (optionalProduct.isPresent() && optionalCartItems.isPresent()) {
            CartItems cartItems = optionalCartItems.get();
            Product product = optionalProduct.get();

            activeOrder.setAmount((null != activeOrder.getAmount() ? activeOrder.getTotalAmount() : 0) + product.getPrice());
            activeOrder.setTotalAmount((null != activeOrder.getAmount() ? activeOrder.getTotalAmount() : 0) + product.getPrice());

            cartItems.setQuantity(cartItems.getQuantity() + 1);
            if (null != activeOrder.getCoupon()) {
                double discountAmount = ((activeOrder.getCoupon().getDiscount() / 100.0) * (null != activeOrder.getTotalAmount() ? activeOrder.getTotalAmount() : 0));
                double netAmount = (null != activeOrder.getTotalAmount() ? activeOrder.getTotalAmount() : 0) - discountAmount;

                activeOrder.setAmount((long) netAmount);
                activeOrder.setDiscount((long) discountAmount);
            }
            cartItemsRepository.save(cartItems);
            orderRepository.save(activeOrder);
            return activeOrder.getOrderDto();
        }
        return null;
    }


    @Override
    public OrderDto decreaseProductQuantity(AddProductInCartDto addProductInCartDto) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(), OrderStatus.Pending);
        Optional<Product> optionalProduct = productRepository.findById(addProductInCartDto.getProductId());

//        System.err.println("***************************" + activeOrder.getId());
//        System.err.println("***************************" + addProductInCartDto.getProductId());
//        System.err.println("***************************" + addProductInCartDto.getUserId());
        Optional<CartItems> optionalCartItems = cartItemsRepository.findByProductIdAndUserIdAndOrderId(
                addProductInCartDto.getProductId(), addProductInCartDto.getUserId(), activeOrder.getId()
        );

        if (optionalProduct.isPresent() && optionalCartItems.isPresent()) {
            CartItems cartItems = optionalCartItems.get();
            Product product = optionalProduct.get();

            activeOrder.setAmount((null != activeOrder.getAmount() ? activeOrder.getTotalAmount() : 0) - product.getPrice());
            activeOrder.setTotalAmount((null != activeOrder.getAmount() ? activeOrder.getTotalAmount() : 0) - product.getPrice());

            cartItems.setQuantity(cartItems.getQuantity() - 1);
            if (null != activeOrder.getCoupon()) {
                double discountAmount = ((activeOrder.getCoupon().getDiscount() / 100.0) * (null != activeOrder.getTotalAmount() ? activeOrder.getTotalAmount() : 0));
                double netAmount = (null != activeOrder.getTotalAmount() ? activeOrder.getTotalAmount() : 0) - discountAmount;

                activeOrder.setAmount((long) netAmount);
                activeOrder.setDiscount((long) discountAmount);
            }
            cartItemsRepository.save(cartItems);
            orderRepository.save(activeOrder);
            return activeOrder.getOrderDto();
        }
        return null;
    }

    @Override
    public OrderDto placeOrder(PlaceOrderDto placeOrderDto) {
        try {
            Order activeOrder = orderRepository.findByUserIdAndOrderStatus(placeOrderDto.getUserId(), OrderStatus.Pending);
            Optional<User> optionalUser = userRepository.findById(placeOrderDto.getUserId());
            if (optionalUser.isPresent()) {
                activeOrder.setOrderDescription(placeOrderDto.getOrderDescription());
                activeOrder.setAddress(placeOrderDto.getAddress());
                activeOrder.setDate(new Date());
                activeOrder.setOrderStatus(OrderStatus.Placed);
                activeOrder.setTrackingId(UUID.randomUUID());

                orderRepository.save(activeOrder);

                Order order = new Order();
                order.setAmount(0L);
                order.setTotalAmount(0L);
                order.setDiscount(0L);
                order.setUser(optionalUser.get());
                order.setOrderStatus(OrderStatus.Pending);
                orderRepository.save(order);

                return activeOrder.getOrderDto();
            }

        } catch (Exception e) {
            logger.error("Exception Occurd" + e.getMessage());
        }
        return null;
    }

    public List<OrderDto> getMyPlacedOrders(Long userId){
        try{
            return orderRepository.findByUserIdAndOrderStatusIn(userId, List.of(OrderStatus.Placed, OrderStatus.Delivered,
                    OrderStatus.Shipped)).stream().map(Order::getOrderDto).collect(Collectors.toList());
        }catch (Exception e){
            logger.error("Exception Occurd" + e.getMessage());
            return null;
        }
    }
    //-----------------------------------------
//    @Override
//    public ResponseEntity<?> addProductToCart(AddProductInCartDto addProductInCartDto) {
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
}