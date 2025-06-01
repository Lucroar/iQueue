package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.Entity.Order;
import com.Lucroar.iQueue.Entity.OrderStatus;
import com.Lucroar.iQueue.Entity.Orders;
import com.Lucroar.iQueue.Entity.OrdersHistory;
import com.Lucroar.iQueue.Repository.OrdersHistoryRepository;
import com.Lucroar.iQueue.Repository.OrdersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KitchenService {
    private final OrdersRepository ordersRepository;
    private final OrdersHistoryRepository ordersHistoryRepository;

    public KitchenService(OrdersRepository ordersRepository, OrdersHistoryRepository ordersHistoryRepository) {
        this.ordersRepository = ordersRepository;
        this.ordersHistoryRepository = ordersHistoryRepository;
    }

    //Put the orderHistory creation here instead of the OrdersHistoryService
    public OrdersHistory orderServed(int tableNumber){
        Orders orders = ordersRepository.findByTableNumber(tableNumber);
        OrdersHistory ordersHistory = ordersHistoryRepository.findByCustomer_CustomerIdAndStatus(orders.getCustomer().getCustomerId(), OrderStatus.ORDERING)
                .orElse(new OrdersHistory(orders.getCustomer(), orders.getOrders(),orders.getTotal()));

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
}
