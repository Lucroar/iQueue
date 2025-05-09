package com.Lucroar.iQueue.Repository;

import com.Lucroar.iQueue.Entity.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {
    Optional<Customer> findByUsername(String username);
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByUsernameAndCustomerIdNot(String username, String customerId);
    Optional<Customer> findByEmailAndCustomerIdNot(String email, String customerId);
}
