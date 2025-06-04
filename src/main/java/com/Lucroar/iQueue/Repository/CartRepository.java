package com.Lucroar.iQueue.Repository;

import com.Lucroar.iQueue.Entity.Cart;
import com.Lucroar.iQueue.Entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {
    Optional<Cart> findByCustomer_customerId(String customerId);
    Optional<Cart> findByCustomer_Username(String username);
}
