package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.OrdersHistoryDTO;
import com.Lucroar.iQueue.Entity.Customer;
import com.Lucroar.iQueue.Entity.OrderStatus;
import com.Lucroar.iQueue.Entity.OrdersHistory;
import com.Lucroar.iQueue.Repository.OrdersHistoryRepository;
import com.Lucroar.iQueue.Repository.OrdersRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class OrderHistoryService {
    private final OrdersHistoryRepository ordersHistoryRepository;

    public OrderHistoryService(OrdersHistoryRepository ordersHistoryRepository) {
        this.ordersHistoryRepository = ordersHistoryRepository;
    }

    public OrdersHistory viewOrderHistoryById(String id) {
        Optional<OrdersHistory> ordersHistory = ordersHistoryRepository.findById(id);
        return ordersHistory.orElse(null);
    }

    public List<OrdersHistoryDTO> viewCustomerHistory(Customer customer) {
        List<OrdersHistory> ordersHistory = ordersHistoryRepository
                .findByCustomer_UsernameAndStatusIn(customer.getUsername(), List.of(OrderStatus.PAID));
        return ordersHistory.stream()
                .map(orderHistory -> new OrdersHistoryDTO(
                        orderHistory.getId(),
                        orderHistory.getOrderDate(),
                        orderHistory.getOrders(),
                        orderHistory.getTotal()))
                .toList();
    }

    public OrdersHistoryDTO viewCurrentOrder(Customer customer) {
        Optional<OrdersHistory> ordersHistory = ordersHistoryRepository.findByCustomer_UsernameAndStatus(
                customer.getUsername(), OrderStatus.ORDERING);
        if (ordersHistory.isPresent()) {
            OrdersHistory orderHistory = ordersHistory.get();
            return new OrdersHistoryDTO(orderHistory.getId(), orderHistory.getOrderDate(), orderHistory.getOrders(), orderHistory.getTotal());
        }
        return null;
    }

    public OrdersHistory viewById(String id){
        Optional<OrdersHistory> ordersHistory = ordersHistoryRepository.findById(id);
        return ordersHistory.orElse(null);
    }

}
