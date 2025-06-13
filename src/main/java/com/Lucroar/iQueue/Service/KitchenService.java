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
import java.util.Optional;

@Service
public class KitchenService {
    private final OrdersRepository ordersRepository;
    private final OrdersHistoryRepository ordersHistoryRepository;

    public KitchenService(OrdersRepository ordersRepository, OrdersHistoryRepository ordersHistoryRepository) {
        this.ordersRepository = ordersRepository;
        this.ordersHistoryRepository = ordersHistoryRepository;
    }

    //TODO dont save to ordersHistory if takeOut or guestOrder
    public OrdersHistory orderServed(String orderId){
        Optional<Orders> ordersDTO = ordersRepository.findById(orderId);
        if(ordersDTO.isPresent()){
            Orders orders = ordersDTO.get();

            //TODO just return the orderId if the orders is paid

            OrdersHistory ordersHistory = ordersHistoryRepository.findByCustomer_CustomerIdAndStatus(orders.getCustomer().getCustomerId(), OrderStatus.ORDERING)
                    .orElse(new OrdersHistory(orders.getCustomer(), new ArrayList<>(), OrderStatus.ORDERING, LocalDateTime.now(), orders.getTableNumber()));

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
        } else return null;
    }

    public List<TableOrderDTO> viewAllOrders(){
        List<Orders> tableOrders = ordersRepository.findAllByOrderByCreatedAtAsc();
        return tableOrders.stream()
                .map(orders ->{
                    TableOrderDTO dto = new TableOrderDTO();
                    dto.setId(orders.getId());
                    dto.setTableNumber(orders.getTableNumber());
                    dto.setOrders(trimOrders(orders.getOrders()));
                    return dto;
                }).toList();
    }

    private List<Order> trimOrders(List<Order> orders){
        return orders.stream()
                .map(order -> {
                    Order trimmed = new Order();
                    trimmed.setName(order.getName());
                    trimmed.setQuantity(order.getQuantity());
                    return trimmed;
                })
                .toList();
    }
}
