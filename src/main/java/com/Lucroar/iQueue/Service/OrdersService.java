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
        newOrders.setTakeOut(false);

        int total = 0;
        for (Order order : cartOrders) {
            total += order.getPrice() * order.getQuantity();
        }
        newOrders.setTotal(total);

        QueueEntry queue = queueRepository
                .findByCustomerUsernameAndStatusIn(customer.getUsername(), List.of(Status.SEATED))
                .orElseThrow(() -> new RuntimeException("No seated queue entry found for customer"));

        newOrders.setTableNumber(queue.getTable_number());

        webSocketPublisher.sendTableOrders(
                new TableOrderDTO(
                        queue.getQueue_id(),
                        queue.getCustomer().getUsername(),
                        queue.getTable_number(),
                        newOrders.isTakeOut(),
                        cartOrders
                )
        );
        return ordersRepository.save(newOrders);
    }

}
