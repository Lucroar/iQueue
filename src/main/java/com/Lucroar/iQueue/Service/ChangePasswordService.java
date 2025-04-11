package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.Entity.Customer;
import com.Lucroar.iQueue.Otp.EmailSender;
import com.Lucroar.iQueue.Otp.OtpDTO;
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
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ChangePasswordService(CustomerRepository customerRepository,
                                 EmailSender emailSender,
                                 BCryptPasswordEncoder passwordEncoder,
                                 BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.customerRepository = customerRepository;
        this.emailSender = emailSender;
        this.passwordEncoder = passwordEncoder;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public boolean sendOtpForCustomer(OtpDTO otpDto){
        Optional<Customer> customer = customerRepository.findByEmail(otpDto.getEmail());
        if(customer.isEmpty()){
            return false;
        }
        String otp = OtpUtil.generateOtp();
        Instant expirationTime = Instant.now().plus(5, ChronoUnit.MINUTES);
        OtpStore.storeOtp(otpDto.getEmail(), otp, expirationTime);
        try {
            emailSender.sendMail(otpDto.getEmail(), otp);
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

    public boolean changePassword(String email, String newPassword) {
        Optional<Customer> optCustomer = customerRepository.findByEmail(email);
        if(optCustomer.isPresent()){
            Customer customer = optCustomer.get();
            if (bCryptPasswordEncoder.matches(newPassword, customer.getPassword())) return false;
            customer.setPassword(passwordEncoder.encode(newPassword));
            customerRepository.save(customer);
            return true;
        }
        return false;
    }
}
