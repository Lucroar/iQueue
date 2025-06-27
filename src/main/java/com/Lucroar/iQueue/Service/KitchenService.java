package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.OrdersHistoryDTO;
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
import java.util.stream.Collectors;

@Service
public class KitchenService {
    private final OrdersRepository ordersRepository;
    private final OrdersHistoryRepository ordersHistoryRepository;

    public KitchenService(OrdersRepository ordersRepository, OrdersHistoryRepository ordersHistoryRepository) {
        this.ordersRepository = ordersRepository;
        this.ordersHistoryRepository = ordersHistoryRepository;
    }

    //Changed to create a new document that will be combined if the customer is done ordering
    public OrdersHistory orderServed(TableOrderDTO tableOrderDTO) {
        Optional<Orders> ordersDTO = ordersRepository.findById(tableOrderDTO.getId());
        if (ordersDTO.isPresent()) {
            Orders orders = ordersDTO.get();

            if (!orders.getCustomer().isGuest() || !orders.isTakeOut()) {
                List<Order> newOrders = orders.getOrders();
                double total = 0;

                for (Order order : newOrders) {
                    total += order.getPrice() * order.getQuantity();
                }

                OrdersHistory newHistory = new OrdersHistory(
                        orders.getCustomer(),
                        new ArrayList<>(newOrders), // Create a new list to avoid reference issues
                        OrderStatus.ORDERING,
                        LocalDateTime.now(),
                        orders.getTableNumber()
                );
                newHistory.setTotal(total);

                ordersRepository.delete(orders);
                return ordersHistoryRepository.save(newHistory);
            }

            ordersRepository.delete(orders);
        }
        return null;
    }


    public List<TableOrderDTO> viewAllOrders() {
        // Get both types
        List<OrdersHistory> ordersHistory = ordersHistoryRepository.findByStatus(OrderStatus.RETURNING);
        List<Orders> tableOrders = ordersRepository.findAllByOrderByCreatedAtAsc();

        // Convert Orders to DTO
        List<TableOrderDTO> orderDTOs = tableOrders.stream()
                .map(orders -> {
                    TableOrderDTO dto = new TableOrderDTO();
                    dto.setId(orders.getId());
                    dto.setUsername(orders.getCustomer().getUsername());
                    dto.setTakeOut(orders.isTakeOut());
                    dto.setTableNumber(orders.getTableNumber());
                    dto.setOrderTime(orders.getCreatedAt());
                    dto.setStatus(OrderStatus.ORDERING);
                    dto.setOrders(trimOrders(orders.getOrders()));
                    return dto;
                }).collect(Collectors.toList());

        // Convert OrdersHistory to DTO
        List<TableOrderDTO> historyDTOs = ordersHistory.stream()
                .map(history -> {
                    TableOrderDTO dto = new TableOrderDTO();
                    dto.setId(history.getId());
                    dto.setUsername(history.getCustomer().getUsername());
                    dto.setTakeOut(history.isTakeOut());
                    dto.setTableNumber(history.getTableNumber());
                    dto.setOrderTime(history.getOrderCreation());
                    dto.setStatus(history.getStatus());
                    dto.setDescription(history.getDescription());
                    dto.setOrders(trimOrders(history.getOrders()));
                    return dto;
                }).toList();

        // Merge both
        orderDTOs.addAll(historyDTOs);
        return orderDTOs;
    }


    //TODO accept returning order
    public List<OrdersHistory> viewReturningOrders() {
        return ordersHistoryRepository.findByStatus(OrderStatus.RETURNING);
    }

    public OrdersHistory resolveReturningOrder(String id, String action){
        Optional<OrdersHistory> ordersDTO = ordersHistoryRepository.findById(id);
        if (ordersDTO.isPresent()) {
            OrdersHistory ordersHistory = ordersDTO.get();
            if (action.equalsIgnoreCase("accept")){
                ordersHistory.setStatus(OrderStatus.RETURNED);
                return ordersHistoryRepository.save(ordersHistory);
            } else if (action.equalsIgnoreCase("decline")) {
                ordersHistory.setStatus(OrderStatus.ORDERING);
                return ordersHistoryRepository.save(ordersHistory);
            }
        }
        return null;
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
