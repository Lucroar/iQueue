package com.Lucroar.iQueue.Repository;

import com.Lucroar.iQueue.Entity.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {
    Optional<Cart> findByCustomer_customerId(String customerId);
}
