package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.CashierOrderDTO;
import com.Lucroar.iQueue.DTO.CustomerDTO;
import com.Lucroar.iQueue.DTO.TableOrderDTO;
import com.Lucroar.iQueue.Entity.*;
import com.Lucroar.iQueue.Repository.MenuRepository;
import com.Lucroar.iQueue.Repository.OrdersHistoryRepository;
import com.Lucroar.iQueue.Repository.OrdersRepository;
import com.Lucroar.iQueue.Repository.QueueRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CashierCartService {
    private final OrdersRepository ordersRepository;
    private final QueueRepository queueRepository;
    private final MenuRepository menuRepository;
    private final OrdersHistoryRepository ordersHistoryRepository;
    private final WebSocketPublisher webSocketPublisher;

    public CashierCartService(OrdersRepository ordersRepository, QueueRepository queueRepository, MenuRepository menuRepository, OrdersHistoryRepository ordersHistoryRepository, WebSocketPublisher webSocketPublisher) {
        this.ordersRepository = ordersRepository;
        this.queueRepository = queueRepository;
        this.menuRepository = menuRepository;
        this.ordersHistoryRepository = ordersHistoryRepository;
        this.webSocketPublisher = webSocketPublisher;
    }

    public Orders createOrder(CashierOrderDTO order){
        Optional<QueueEntry> queueEntry = queueRepository.findByCustomer_Username(order.getUsername());
        if(queueEntry.isPresent() || order.isTakeOut()){
            Orders orders = new Orders();
            CustomerDTO customer = new CustomerDTO();
            customer.setUsername(order.getUsername());
            customer.setGuest(true);
            orders.setCustomer(customer);
            if(!order.isTakeOut())orders.setTableNumber(queueEntry.get().getTable_number());
            orders.setOrders(order.getOrders());
            orders.setCreatedAt(LocalDateTime.now());
            orders.setTakeOut(order.isTakeOut());

            for (Order orderEntity : order.getOrders()) {
                Menu menu = menuRepository.findById(orderEntity.getProduct_id()).get();
                orderEntity.setPrice(menu.getPrice());
                orderEntity.setName(menu.getName());
                orders.setTotal(orders.getTotal() + (orderEntity.getPrice() * orderEntity.getQuantity()));
            }

            OrdersHistory ordersHistory = new OrdersHistory();
            ordersHistory.setCustomer(orders.getCustomer());
            ordersHistory.setTakeOut(orders.isTakeOut());
            ordersHistory.setOrders(orders.getOrders());
            ordersHistory.setStatus(OrderStatus.UNPAID);
            ordersHistory.setTotal(orders.getTotal());
            ordersHistory.setOrderCreation(LocalDateTime.now());
            ordersHistory.setTotal(orders.getTotal());
            ordersHistory.setOrders(orders.getOrders());
            if(!order.isTakeOut()) ordersHistory.setTableNumber(queueEntry.get().getTable_number());
            OrdersHistory newHistory = ordersHistoryRepository.save(ordersHistory);

            webSocketPublisher.sendTableOrders(
                    new TableOrderDTO(
                            orders.getId(),
                        orders.getCustomer().getUsername(),
                        orders.getTableNumber(),
                        orders.isTakeOut(),
                        orders.getCreatedAt(),
                        OrderStatus.ORDERING,
                        orders.getOrders()));
            return ordersRepository.save(orders);
        }
        return null;
    }

    public int checkTableNumber(String username) {
        Optional<QueueEntry> queueEntry = queueRepository.findByCustomer_Username(username);
        return queueEntry.map(QueueEntry::getTable_number).orElse(0);
    }
}
