package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.CustomerDTO;
import com.Lucroar.iQueue.Entity.Customer;
import com.Lucroar.iQueue.Repository.CustomerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, BCryptPasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Customer createCustomer(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        return customerRepository.save(customer);
    }

    public Customer findCustomerById(String customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        return customer.orElse(null);
    }

    public Customer findCustomerByEmail(String email) {
        Optional<Customer> customer = customerRepository.findByEmail(email);
        return customer.orElse(null);
    }

    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(customerDTO.getCustomerId()).orElse(null);
        if (customer != null) {
            customer.setId(customerDTO.getCustomerId());
            customer.setUsername(customerDTO.getUsername());
            customer.setEmail(customerDTO.getEmail());
            customer.setFirst_name(customerDTO.getFirst_Name());
            customer.setLast_name(customerDTO.getLast_Name());
            customer.setMobileNumber(customerDTO.getMobileNumber());
            customerRepository.save(customer);
        }
        return customerDTO;
    }

    public boolean existingUsername(String username) {
        return customerRepository.findByUsername(username).isPresent();
    }

    public boolean existingEmail(String email) {
        return customerRepository.findByEmail(email).isPresent();
    }

    public boolean existingUsernameIgnoreId(String username, String id) {
        return customerRepository.findByUsernameAndIdNot(username, id).isPresent();
    }

    public boolean existingEmailIgnoreId(String email, String id) {
        return customerRepository.findByEmailAndIdNot(email, id).isPresent();
    }
}
