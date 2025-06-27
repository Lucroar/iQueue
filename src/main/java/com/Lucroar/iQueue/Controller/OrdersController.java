package com.Lucroar.iQueue.Controller;

import com.Lucroar.iQueue.Entity.Customer;
import com.Lucroar.iQueue.Entity.Orders;
import com.Lucroar.iQueue.Service.OrdersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@AuthenticationPrincipal Customer customer) {
        Orders orders = ordersService.checkoutOrders(customer);
        if (orders == null) {
            return ResponseEntity.status(409).body(Collections.singletonMap("msg", "Cart Empty!"));
        }
        return ResponseEntity.ok().body(orders);
    }
}
