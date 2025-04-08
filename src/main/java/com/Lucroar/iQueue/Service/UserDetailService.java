package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.Entity.Cashier;
import com.Lucroar.iQueue.Entity.Customer;
import com.Lucroar.iQueue.Repository.CashierRepository;
import com.Lucroar.iQueue.Repository.CustomerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailService implements UserDetailsManager {
    private final BCryptPasswordEncoder passwordEncoder;
    private final CashierRepository cashierRepository;
    private final CustomerRepository customerRepository;

    public UserDetailService(BCryptPasswordEncoder passwordEncoder, CashierRepository cashierRepository, CustomerRepository customerRepository) {
        this.passwordEncoder = passwordEncoder;
        this.cashierRepository = cashierRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void createUser(UserDetails user) {
        if (user instanceof Cashier cashier) {
            cashier.setPassword(passwordEncoder.encode(cashier.getPassword()));
            cashierRepository.save(cashier);
        } else if (user instanceof Customer customer) {
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
            customerRepository.save(customer);
        }
    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Customer> customer = customerRepository.findByUsername(username);
        if (customer.isPresent()) {
            return customer.get();
        }
        Optional<Cashier> cashier = cashierRepository.findByUsername(username);
        return cashier.orElse(null);
    }
}
