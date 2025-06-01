package com.Lucroar.iQueue.Repository;

import com.Lucroar.iQueue.Entity.Cart;
import com.Lucroar.iQueue.Entity.Orders;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OrdersRepository extends MongoRepository<com.Lucroar.iQueue.Entity.Orders, String> {
    Optional<Orders> findByCustomer_customerId(String customerId);
    Orders findByTableNumber(int tableNumber);
}
