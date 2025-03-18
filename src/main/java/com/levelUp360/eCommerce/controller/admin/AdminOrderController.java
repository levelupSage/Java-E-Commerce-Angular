package com.levelUp360.eCommerce.controller.admin;

import com.levelUp360.eCommerce.dto.OrderDto;
import com.levelUp360.eCommerce.services.adminOrder.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
public class AdminOrderController {

    static Logger logger = LoggerFactory.getLogger(AdminOrderController.class);

    private final AdminOrderService adminOrderService;

    @GetMapping("/placeOrders")
    public ResponseEntity<List<OrderDto>> getAllPlaceOrders() {
        try {
            return ResponseEntity.ok(adminOrderService.getAllPlaceOrders());
        } catch (Exception ex) {
            logger.error("Exception Occurred : " + ex.getMessage());
            return null;
        }
    }

    @GetMapping("/order/{orderId}/{status}")
    public ResponseEntity<?> changeOrderStatus(@PathVariable Long orderId, @PathVariable String status) {
        try {
            OrderDto orderDto = adminOrderService.changeOrderStatus(orderId, status);
            if (orderDto == null) {
                return new ResponseEntity<>("Something went wrong...!!", HttpStatus.BAD_REQUEST);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(orderDto);
            }
        } catch (Exception e) {
            logger.error("Exception Occurred : " + e.getMessage());
            return null;
        }

    }
}
