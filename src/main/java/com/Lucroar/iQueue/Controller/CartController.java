package com.Lucroar.iQueue.Controller;

import com.Lucroar.iQueue.Entity.Customer;
import com.Lucroar.iQueue.Entity.Order;
import com.Lucroar.iQueue.Service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@AuthenticationPrincipal Customer customer, @RequestBody List<Order> orderList) {
        return ResponseEntity.ok(cartService.addToCart(customer, orderList));
    }

    @GetMapping("/view")
    public ResponseEntity<?> viewCart(@AuthenticationPrincipal Customer customer) {
        return ResponseEntity.ok(cartService.viewOrder( customer ));
    }

    @PatchMapping("/update/{menuId}/{action}")
    public ResponseEntity<?> updateOrderQuantity(@AuthenticationPrincipal Customer customer, @PathVariable String menuId,
                                                 @PathVariable String action) {
        return ResponseEntity.ok(cartService.updateCartQuantity(customer, menuId, action));
    }
}
