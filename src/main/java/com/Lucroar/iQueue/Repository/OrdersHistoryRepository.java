package com.Lucroar.iQueue.Repository;

import com.Lucroar.iQueue.Entity.OrderStatus;
import com.Lucroar.iQueue.Entity.OrdersHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdersHistoryRepository extends MongoRepository<OrdersHistory, String> {
    Optional<OrdersHistory> findByCustomer_CustomerIdAndStatus(String customer, OrderStatus status);
    Optional<OrdersHistory> findByCustomer_UsernameAndStatus(String username, OrderStatus status);
    Optional<OrdersHistory> findByCustomer_usernameAndStatus(String username, OrderStatus status);
    List<OrdersHistory> findByStatus(OrderStatus status);
}
