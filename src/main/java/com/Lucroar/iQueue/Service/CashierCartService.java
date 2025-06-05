package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.CashierOrderDTO;
import com.Lucroar.iQueue.DTO.CustomerDTO;
import com.Lucroar.iQueue.Entity.*;
import com.Lucroar.iQueue.Repository.MenuRepository;
import com.Lucroar.iQueue.Repository.OrdersRepository;
import com.Lucroar.iQueue.Repository.QueueRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CashierCartService {
    private final OrdersRepository ordersRepository;
    private final QueueRepository queueRepository;
    private final MenuRepository menuRepository;

    public CashierCartService(OrdersRepository ordersRepository, QueueRepository queueRepository, MenuRepository menuRepository) {
        this.ordersRepository = ordersRepository;
        this.queueRepository = queueRepository;
        this.menuRepository = menuRepository;
    }

    public Orders createOrder(CashierOrderDTO order){
        Optional<QueueEntry> queueEntry = queueRepository.findByCustomer_Username(order.getUsername());
        if(queueEntry.isPresent()){
            Orders orders = new Orders();
            CustomerDTO customer = new CustomerDTO();
            customer.setUsername(order.getUsername());
            customer.setGuest(true);
            orders.setCustomer(customer);
            orders.setTableNumber(queueEntry.get().getTable_number());
            orders.setOrders(order.getOrders());

            for (Order orderEntity : order.getOrders()) {
                Menu menu = menuRepository.findById(orderEntity.getProduct_id()).get();
                orderEntity.setPrice(menu.getPrice());
                orderEntity.setName(menu.getName());
                orders.setTotal(orders.getTotal() + (orderEntity.getPrice() * orderEntity.getQuantity()));
            }
            return ordersRepository.save(orders);
        }
        return null;
    }

    public int checkTableNumber(String username) {
        Optional<QueueEntry> queueEntry = queueRepository.findByCustomer_Username(username);
        return queueEntry.map(QueueEntry::getTable_number).orElse(0);
    }
}
