package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.Entity.Customer;
import com.Lucroar.iQueue.Otp.EmailSender;
import com.Lucroar.iQueue.Otp.OtpStore;
import com.Lucroar.iQueue.Otp.OtpUtil;
import com.Lucroar.iQueue.Repository.CustomerRepository;
import jakarta.mail.MessagingException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class ChangePasswordService {
    private final CustomerRepository customerRepository;
    private final EmailSender emailSender;
    private final BCryptPasswordEncoder passwordEncoder;

    public ChangePasswordService(CustomerRepository customerRepository,
                                 EmailSender emailSender,
                                 BCryptPasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.emailSender = emailSender;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean sendOtpForCustomer(String email){
        Optional<Customer> customer = customerRepository.findByEmail(email);
        if(customer.isEmpty()){
            return false;
        }
        String otp = OtpUtil.generateOtp();
        Instant expirationTime = Instant.now().plus(5, ChronoUnit.MINUTES);
        OtpStore.storeOtp(email, otp, expirationTime);
        try {
            emailSender.sendMail(email, otp);
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean verifyOtp(String emailAddress, String otp) {
        String storedOtp = OtpStore.getOtp(emailAddress);
        if (storedOtp != null && storedOtp.equals(otp)) {
            OtpStore.removeOtp(emailAddress);
            return true;
        }
        return false;
    }

    public boolean changePassword(String id, String newPassword) {
        Optional<Customer> optCustomer = customerRepository.findById(id);
        if(optCustomer.isPresent()){
            Customer customer = optCustomer.get();
            if (passwordEncoder.matches(newPassword, customer.getPassword())) return false;
            customer.setPassword(passwordEncoder.encode(newPassword));
            customerRepository.save(customer);
            return true;
        }
        return false;
    }
}
