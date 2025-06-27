package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.OrdersHistoryDTO;
import com.Lucroar.iQueue.Entity.Customer;
import com.Lucroar.iQueue.Entity.OrderStatus;
import com.Lucroar.iQueue.Entity.OrdersHistory;
import com.Lucroar.iQueue.Repository.OrdersHistoryRepository;
import org.springframework.stereotype.Service;

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
                .findByCustomer_UsernameAndStatusIn(customer.getUsername(), List.of(OrderStatus.PAID, OrderStatus.RETURNED));
        return ordersHistory.stream()
                .map(orderHistory -> new OrdersHistoryDTO(
                        orderHistory.getId(),
                        orderHistory.getOrderCreation(),
                        orderHistory.getOrders(),
                        orderHistory.getTotal()))
                .toList();
    }

    public List<OrdersHistoryDTO> viewCurrentOrder(Customer customer) {
        Optional<List<OrdersHistory>> ordersHistory = ordersHistoryRepository.findByCustomer_UsernameAndStatus(
                customer.getUsername(), OrderStatus.ORDERING);

        if (ordersHistory.isPresent()) {
            List<OrdersHistory> orderHistoryList = ordersHistory.get();
            return orderHistoryList.stream()
                    .map(
                    orderHistory -> {
                        OrdersHistoryDTO dto = new OrdersHistoryDTO();
                        dto.setId(orderHistory.getId());
                        dto.setOrderDate(orderHistory.getOrderCreation());
                        dto.setOrders(orderHistory.getOrders());
                        dto.setTotal(orderHistory.getTotal());
                        return dto;
                    }).toList();
        }
        return null;
    }

    public OrdersHistory viewById(String id){
        Optional<OrdersHistory> ordersHistory = ordersHistoryRepository.findById(id);
        return ordersHistory.orElse(null);
    }

    public OrdersHistoryDTO returnOrder(String orderId, String description){
        Optional<OrdersHistory> orderOpt = ordersHistoryRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            OrdersHistory ordersHistory = orderOpt.get();
            ordersHistory.setStatus(OrderStatus.RETURNING);
            ordersHistory.setDescription(description);
            ordersHistoryRepository.save(ordersHistory);
            OrdersHistoryDTO dto = new OrdersHistoryDTO();
            dto.setId(ordersHistory.getId());
            dto.setOrderDate(ordersHistory.getOrderCreation());
            dto.setOrders(ordersHistory.getOrders());
            dto.setTotal(ordersHistory.getTotal());
            dto.setDescription(description);
            return dto;
        }
        return null;
    }
}
