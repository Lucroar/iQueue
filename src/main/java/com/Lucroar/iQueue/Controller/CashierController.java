package com.Lucroar.iQueue.Controller;

import com.Lucroar.iQueue.Service.TokenService;
import com.Lucroar.iQueue.Service.UserDetailService;
import com.Lucroar.iQueue.Entity.Cashier;
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
@RequestMapping("cashier")
public class CashierController {
    private final TokenService tokenService;
    private final DaoAuthenticationProvider authProvider;
    private final UserDetailService userDetailService;

    public CashierController(TokenService tokenService,
                             DaoAuthenticationProvider authProvider,
                             UserDetailService userDetailService) {
        this.tokenService = tokenService;
        this.authProvider = authProvider;
        this.userDetailService = userDetailService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Cashier cashier) {
        try {
            Authentication auth = authProvider.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(
                    cashier.getUsername(), cashier.getPassword()
            ));
            Cashier cashier1 = (Cashier) userDetailService.loadUserByUsername(cashier.getUsername());
            String token = tokenService.generateToken(auth).get("token");
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
//            response.put("cashierId", cashier1.getCashierId());
//            response.put("role", cashier1.getRole());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("msg", "Invalid username or password"));
        }
    }

    @PostMapping("/new-cashier")
    public ResponseEntity<?> newEmployee(@RequestBody Cashier cashier) {
        userDetailService.createUser(cashier);
        return ResponseEntity.ok(cashier);
    }
}
