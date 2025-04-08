package com.Lucroar.iQueue.Controller;

import com.Lucroar.iQueue.Entity.Cashier;
import com.Lucroar.iQueue.Entity.Customer;
import com.Lucroar.iQueue.Service.TokenService;
import com.Lucroar.iQueue.Service.UserDetailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("customer")
public class CustomerController {
    private final TokenService tokenService;
    private final DaoAuthenticationProvider authProvider;
    private final UserDetailService userDetailService;

    public CustomerController(TokenService tokenService, DaoAuthenticationProvider authProvider, UserDetailService userDetailService) {
        this.tokenService = tokenService;
        this.authProvider = authProvider;
        this.userDetailService = userDetailService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Customer customer) {
        try {
            Authentication auth = authProvider.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(
                    customer.getUsername(), customer.getPassword()
            ));
            Customer customer1 = (Customer) userDetailService.loadUserByUsername(customer.getUsername());
            String token = tokenService.generateToken(auth).get("token");
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
//            response.put("customerId", customer1.getCustomerId());
//            response.put("role", customer1.getRole());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("msg", "Invalid username or password"));
        }
    }

    @PostMapping("/new-customer")
    public ResponseEntity<?> newCustomer(@RequestBody Customer customer) {
        userDetailService.createUser(customer);
        return ResponseEntity.ok(customer);
    }
}
