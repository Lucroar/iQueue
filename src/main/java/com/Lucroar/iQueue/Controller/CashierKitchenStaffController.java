package com.Lucroar.iQueue.Controller;

import com.Lucroar.iQueue.DTO.LoginRequest;
import com.Lucroar.iQueue.Entity.KitchenStaff;
import com.Lucroar.iQueue.Service.TokenService;
import com.Lucroar.iQueue.Service.UserDetailService;
import com.Lucroar.iQueue.Entity.Cashier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/staff")
public class CashierKitchenStaffController {
    private final TokenService tokenService;
    private final DaoAuthenticationProvider authProvider;
    private final UserDetailService userDetailService;

    public CashierKitchenStaffController(TokenService tokenService,
                                         DaoAuthenticationProvider authProvider,
                                         UserDetailService userDetailService) {
        this.tokenService = tokenService;
        this.authProvider = authProvider;
        this.userDetailService = userDetailService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Authenticate using Spring Security
            Authentication auth = authProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            // Determine user type after authentication
            Object principal = auth.getPrincipal();
            Map<String, Object> response = new HashMap<>();
            response.put("token", tokenService.generateToken(auth).get("token"));

            if (principal instanceof Cashier cashier) {
                response.put("userType", "Cashier");
                response.put("userId", cashier.getCashier_id());
            } else if (principal instanceof KitchenStaff kitchenStaff) {
                response.put("userType", "KitchenStaff");
                response.put("userId", kitchenStaff.getKitchenStaff_id());
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("msg", "Invalid username or password"));
        }
    }

    @PostMapping("/new-cashier")
    public ResponseEntity<?> newCashier(@RequestBody Cashier cashier) {
        try {
            userDetailService.createUser(cashier);
            return ResponseEntity.ok(cashier);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("msg", e.getMessage()));
        }
    }

    @PostMapping("/new-kitchen-staff")
    public ResponseEntity<?> newKitchenStaff(@RequestBody KitchenStaff kitchenStaff) {
        try {
            userDetailService.createUser(kitchenStaff);
            return ResponseEntity.ok(kitchenStaff);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("msg", e.getMessage()));
        }
    }

}
