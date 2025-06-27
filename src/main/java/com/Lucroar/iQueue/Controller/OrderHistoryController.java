package com.Lucroar.iQueue.Controller;

import com.Lucroar.iQueue.DTO.OrdersHistoryDTO;
import com.Lucroar.iQueue.Entity.Customer;
import com.Lucroar.iQueue.Service.OrderHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

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
        List<OrdersHistoryDTO> dto = orderHistoryService.viewCurrentOrder(customer);
        if (dto == null) {
            return ResponseEntity.status(409).body(Collections.singletonMap("msg", "No Current Order"));
        }
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> viewOrderById(@PathVariable String id) {
        return ResponseEntity.ok(orderHistoryService.viewById(id));
    }

    @PostMapping("/return")
    public ResponseEntity<?> returnOrder(@RequestBody OrdersHistoryDTO dto) {
        OrdersHistoryDTO order = orderHistoryService.returnOrder(dto.getId(), dto.getDescription());
        if (order == null) {
            return ResponseEntity.status(409).body(Collections.singletonMap("msg", "Order not found"));
        }
        return ResponseEntity.ok(order);
    }
}
