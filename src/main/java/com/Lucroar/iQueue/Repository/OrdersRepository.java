package com.Lucroar.iQueue.Repository;

import com.Lucroar.iQueue.Entity.Orders;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends MongoRepository<com.Lucroar.iQueue.Entity.Orders, String> {
    Optional<Orders> findByCustomer_customerId(String customerId);
    Optional<Orders> findByCustomer_username(String username);
    Optional<Orders> findByCustomer_UsernameAndTakeOut(String username, boolean isTakeOut);
    Orders findByTableNumber(int tableNumber);
    List<Orders> findAllByOrderByCreatedAtAsc();
}
