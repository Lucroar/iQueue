package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.Entity.Cashier;
import com.Lucroar.iQueue.Entity.Customer;
import com.Lucroar.iQueue.Entity.KitchenStaff;
import com.Lucroar.iQueue.Repository.CashierRepository;
import com.Lucroar.iQueue.Repository.CustomerRepository;
import com.Lucroar.iQueue.Repository.KitchenRepository;
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
    private final KitchenRepository kitchenRepository;

    public UserDetailService(BCryptPasswordEncoder passwordEncoder, CashierRepository cashierRepository, CustomerRepository customerRepository, KitchenRepository kitchenRepository) {
        this.passwordEncoder = passwordEncoder;
        this.cashierRepository = cashierRepository;
        this.customerRepository = customerRepository;
        this.kitchenRepository = kitchenRepository;
    }

    @Override
    public void createUser(UserDetails user) {
        String username = user.getUsername();

        // Check if username exists in either staff collection
        boolean usernameTaken =
                cashierRepository.findByUsername(username).isPresent() ||
                        kitchenRepository.findByUsername(username).isPresent();

        if (usernameTaken) {
            throw new IllegalArgumentException("Username already taken by another staff member.");
        }

        switch (user) {
            case Cashier cashier -> {
                cashier.setPassword(passwordEncoder.encode(cashier.getPassword()));
                cashierRepository.save(cashier);
            }
            case Customer customer -> {
                // No username uniqueness enforcement for customers
                customer.setPassword(passwordEncoder.encode(customer.getPassword()));
                customerRepository.save(customer);
            }
            case KitchenStaff kitchenStaff -> {
                kitchenStaff.setPassword(passwordEncoder.encode(kitchenStaff.getPassword()));
                kitchenRepository.save(kitchenStaff);
            }
            default -> throw new IllegalArgumentException("Unsupported user type.");
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

        Optional<KitchenStaff> kitchenStaff = kitchenRepository.findByUsername(username);
        if (kitchenStaff.isPresent()) {
            return kitchenStaff.get();
        }

        Optional<Cashier> cashier = cashierRepository.findByUsername(username);
        if (cashier.isPresent()) {
            return cashier.get();
        }

        throw new UsernameNotFoundException("User with username '" + username + "' not found.");
    }
}
