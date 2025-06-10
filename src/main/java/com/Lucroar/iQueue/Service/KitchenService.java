package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.TableOrderDTO;
import com.Lucroar.iQueue.Entity.Order;
import com.Lucroar.iQueue.Entity.OrderStatus;
import com.Lucroar.iQueue.Entity.Orders;
import com.Lucroar.iQueue.Entity.OrdersHistory;
import com.Lucroar.iQueue.Repository.OrdersHistoryRepository;
import com.Lucroar.iQueue.Repository.OrdersRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class KitchenService {
    private final OrdersRepository ordersRepository;
    private final OrdersHistoryRepository ordersHistoryRepository;

    public KitchenService(OrdersRepository ordersRepository, OrdersHistoryRepository ordersHistoryRepository) {
        this.ordersRepository = ordersRepository;
        this.ordersHistoryRepository = ordersHistoryRepository;
    }

    public OrdersHistory orderServed(int tableNumber){
        Orders orders = ordersRepository.findByTableNumber(tableNumber);
        OrdersHistory ordersHistory = ordersHistoryRepository.findByCustomer_CustomerIdAndStatus(orders.getCustomer().getCustomerId(), OrderStatus.ORDERING)
                .orElse(new OrdersHistory(orders.getCustomer(), new ArrayList<>(), OrderStatus.ORDERING, LocalDateTime.now(), tableNumber));

        List<Order> newOrders = orders.getOrders();
        List<Order> existingOrders = ordersHistory.getOrders();

        for (Order order : newOrders) {
            boolean found = false;
            for (Order existingOrder : existingOrders) {
                if (existingOrder.getProduct_id().equals(order.getProduct_id())){
                    ordersHistory.setTotal(ordersHistory.getTotal() + (order.getPrice() * order.getQuantity()));
                    existingOrder.setQuantity(existingOrder.getQuantity() + order.getQuantity());
                    found = true;
                    break;
                }
            }

            if(!found){
                existingOrders.add(order);
                ordersHistory.setTotal(ordersHistory.getTotal() + (order.getPrice() * order.getQuantity()));
            }
        }
        ordersRepository.delete(orders);
        return ordersHistoryRepository.save(ordersHistory);
    }

    public List<TableOrderDTO> viewAllOrders(){
        List<Orders> tableOrders = ordersRepository.findAll();
        return tableOrders.stream()
                .map(orders ->{
                    TableOrderDTO dto = new TableOrderDTO();
                    dto.setTableNumber(orders.getTableNumber());
                    dto.setOrders(orders.getOrders());
                    return dto;
                }).toList();
    }
}
