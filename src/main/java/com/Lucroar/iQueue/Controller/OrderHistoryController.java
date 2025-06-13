package com.Lucroar.iQueue.Controller;

import com.Lucroar.iQueue.DTO.OrdersHistoryDTO;
import com.Lucroar.iQueue.Entity.Customer;
import com.Lucroar.iQueue.Service.OrderHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/customer/order-history")
public class OrderHistoryController {
    private final OrderHistoryService orderHistoryService;

    public OrderHistoryController(OrderHistoryService orderHistoryService) {
        this.orderHistoryService = orderHistoryService;
    }

    @GetMapping("/paid")
    public ResponseEntity<?> viewOrderHistory(@AuthenticationPrincipal Customer customer) {
        return ResponseEntity.ok(orderHistoryService.viewCustomerHistory(customer));
    }

    @GetMapping("/orders")
    public ResponseEntity<?> viewOrders(@AuthenticationPrincipal Customer customer) {
        OrdersHistoryDTO dto = orderHistoryService.viewCurrentOrder(customer);
        if (dto == null) {
            return ResponseEntity.status(409).body(Collections.singletonMap("msg", "No Current Order"));
        }
        return ResponseEntity.ok(dto);
    }
}
