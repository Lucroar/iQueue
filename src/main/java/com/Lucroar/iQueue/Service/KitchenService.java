package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.Entity.Orders;
import com.Lucroar.iQueue.Repository.OrdersRepository;
import org.springframework.stereotype.Service;

@Service
public class KitchenService {
    private final OrdersRepository ordersRepository;

    public KitchenService(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    //Put the orderHistory creation here instead of the OrdersHistoryService
    public Orders orderServed(int tableNumber){
        Orders orders = ordersRepository.findByTableNumber(tableNumber);
        ordersRepository.delete(orders);
        return orders;
    }
}
