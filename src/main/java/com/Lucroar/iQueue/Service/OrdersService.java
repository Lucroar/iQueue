package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.CustomerDTO;
import com.Lucroar.iQueue.DTO.QueueDTO;
import com.Lucroar.iQueue.DTO.TableOrderDTO;
import com.Lucroar.iQueue.Entity.*;
import com.Lucroar.iQueue.Repository.MenuRepository;
import com.Lucroar.iQueue.Repository.OrdersRepository;
import com.Lucroar.iQueue.Repository.QueueRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final QueueRepository queueRepository;
    private final CartService cartService;
    private final WebSocketPublisher webSocketPublisher;

    public OrdersService(OrdersRepository ordersRepository, QueueRepository queueRepository, CartService cartService, WebSocketPublisher webSocketPublisher) {
        this.ordersRepository = ordersRepository;
        this.queueRepository = queueRepository;
        this.cartService = cartService;
        this.webSocketPublisher = webSocketPublisher;
    }

    public Orders checkoutOrders(Customer customer) {
        List<Order> cartOrders = cartService.checkout(customer);
        Orders newOrders = new Orders();
        newOrders.setCustomer(new CustomerDTO(customer));
        newOrders.setOrders(cartOrders);
        newOrders.setTakeOut(false);
        newOrders.setCreatedAt(LocalDateTime.now());
        for (Order order : cartOrders) {
            newOrders.setTotal(order.getPrice() * order.getQuantity());
        }
        QueueEntry queue = queueRepository.findByCustomerUsernameAndStatusIn(customer.getUsername(), List.of(Status.SEATED)).get();
        newOrders.setTableNumber(queue.getTable_number());
        webSocketPublisher.sendTableOrders(new TableOrderDTO(queue.getQueue_id(), queue.getTable_number(), cartOrders));
//        Removed to test logic in stacking in orderHistory
//        Orders orders = ordersRepository.findByCustomer_customerId(customer.getId())
//                .orElseGet(() -> {
//                    Orders newOrders = new Orders();
//                    newOrders.setCustomer(new CustomerDTO(customer));
//                    newOrders.setOrders(new ArrayList<>());
//                    return newOrders;
//                });
//        QueueEntry queue = queueRepository.findByCustomerUsernameAndStatusIn(customer.getUsername(), List.of(Status.SEATED)).get();

//        List<Order> existingOrders = orders.getOrders();
//        for (Order cartOrder : cartOrders) {
//            boolean found = false;
//            Menu menuOpt = menuRepository.findById(cartOrder.getProduct_id()).get();
//
//            for (Order existingOrder : existingOrders) {
//                if (existingOrder.getProduct_id().equals(cartOrder.getProduct_id())) {
//                    orders.setTotal(orders.getTotal() + (menuOpt.getPrice() * cartOrder.getQuantity()));
//                    existingOrder.setQuantity(existingOrder.getQuantity() + cartOrder.getQuantity());
//                    found = true;
//                    break;
//                }
//            }
//
//            if (!found) {
//                existingOrders.add(cartOrder);
//                orders.setTotal(orders.getTotal() + (menuOpt.getPrice()*cartOrder.getQuantity()));
//            }
//        }
//        orders.setTableNumber(queue.getTable_number());

        return ordersRepository.save(newOrders);
    }

}
