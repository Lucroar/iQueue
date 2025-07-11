package com.Lucroar.iQueue.Controller;

import com.Lucroar.iQueue.DTO.ChangePasswordDTO;
import com.Lucroar.iQueue.DTO.CustomerDTO;
import com.Lucroar.iQueue.Entity.Customer;
import com.Lucroar.iQueue.Otp.OtpDTO;
import com.Lucroar.iQueue.Service.ChangePasswordService;
import com.Lucroar.iQueue.Service.CustomerService;
import com.Lucroar.iQueue.Service.TokenService;
import com.Lucroar.iQueue.Service.UserDetailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final TokenService tokenService;
    private final DaoAuthenticationProvider authProvider;
    private final UserDetailService userDetailService;
    private final ChangePasswordService changePasswordService;
    private final CustomerService customerService;

    public CustomerController(TokenService tokenService,
                              DaoAuthenticationProvider authProvider,
                              UserDetailService userDetailService,
                              ChangePasswordService changePasswordService,
                              CustomerService customerService) {
        this.tokenService = tokenService;
        this.authProvider = authProvider;
        this.userDetailService = userDetailService;
        this.changePasswordService = changePasswordService;
        this.customerService = customerService;
    }

    @GetMapping("/view-profile")
    public ResponseEntity<CustomerDTO> getProfile(@AuthenticationPrincipal Customer customer) {
        Customer customerCont = customerService.findCustomerById(customer.getId());
        CustomerDTO customerDTO = new CustomerDTO(customerCont);
        return ResponseEntity.ok(customerDTO);
    }

    @PatchMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal Customer customer, @RequestBody CustomerDTO customerDTO) {
        customerDTO.setCustomerId(customer.getId());
        if (customerService.existingEmailIgnoreId(customerDTO.getEmail(), customerDTO.getCustomerId())) {
            return ResponseEntity.status(409).body(Collections.singletonMap("msg", "Email already in use"));
        }

        if (customerService.existingUsernameIgnoreId(customerDTO.getUsername(), customerDTO.getCustomerId())) {
            return ResponseEntity.status(409).body(Collections.singletonMap("msg", "Username already in use"));
        }
        return ResponseEntity.ok(customerService.updateCustomer(customerDTO));
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

    @PostMapping("/register")
    public ResponseEntity<?> newCustomer(@RequestBody Customer customer) {
        if (customerService.existingEmail(customer.getEmail())) {
            return ResponseEntity.status(409).body(Collections.singletonMap("msg", "Email already in use"));
        }
        if (customerService.existingUsername(customer.getUsername())) {
            return ResponseEntity.status(409).body(Collections.singletonMap("msg", "Username already in use"));
        }
        userDetailService.createUser(customer);
        return ResponseEntity.ok(customer);
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> changePassword(@RequestBody OtpDTO otpDTO) {
        boolean success = changePasswordService.sendOtpForCustomer(otpDTO.getEmail());
        if (success) {
            return ResponseEntity.ok(Collections.singletonMap("msg", "OTP sent successfully"));
        } else {
            return new ResponseEntity<>(Collections.singletonMap("msg", "No account associated with this email address."), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpDTO otpDTO) {
        boolean success = changePasswordService.verifyOtp(otpDTO.getEmail(), otpDTO.getOtp());
        if (success) {
            Customer customer = customerService.findCustomerByEmail(otpDTO.getEmail());
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    customer, null, customer.getAuthorities());
            String token = tokenService.generateToken(auth).get("token");
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        }else {
            return new ResponseEntity<>(Collections.singletonMap("msg", "Invalid OTP, please try again."), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal Customer customer, @RequestBody ChangePasswordDTO request){
        boolean success = changePasswordService.changePassword(customer.getId(), request.getNewPassword());
        if (success) {
            return new ResponseEntity<>(Collections.singletonMap("msg", "Password changed successfully!"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Collections.singletonMap("msg", "Password cannot be changed to current password."), HttpStatus.BAD_REQUEST);
        }
    }
}
