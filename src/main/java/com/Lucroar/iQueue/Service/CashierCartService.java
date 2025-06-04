package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.CashierOrderDTO;
import com.Lucroar.iQueue.DTO.CustomerDTO;
import com.Lucroar.iQueue.Entity.*;
import com.Lucroar.iQueue.Repository.CartRepository;
import com.Lucroar.iQueue.Repository.MenuRepository;
import com.Lucroar.iQueue.Repository.OrdersRepository;
import com.Lucroar.iQueue.Repository.QueueRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CashierCartService {
    private final OrdersRepository ordersRepository;
    private final QueueRepository queueRepository;

    public CashierCartService(OrdersRepository ordersRepository, QueueRepository queueRepository) {
        this.ordersRepository = ordersRepository;
        this.queueRepository = queueRepository;
    }

    public Orders createOrder(CashierOrderDTO order){
        Optional<QueueEntry> queueEntry = queueRepository.findByCustomerUsername(order.getUsername());
        if(queueEntry.isPresent()){
            Orders orders = new Orders();
            CustomerDTO customer = new CustomerDTO();
            customer.setUsername(order.getUsername());
            orders.setCustomer(customer);
            orders.setTableNumber(queueEntry.get().getTable_number());
            orders.setOrders(order.getOrders());

            for (Order orderEntity : order.getOrders()) {
                orders.setTotal(orders.getTotal() + (orderEntity.getPrice() * orderEntity.getQuantity()));
            }
            ordersRepository.save(orders);
        }
        return null;
    }
}
