package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.CustomerDTO;
import com.Lucroar.iQueue.Entity.Customer;
import com.Lucroar.iQueue.Entity.Order;
import com.Lucroar.iQueue.Entity.Orders;
import com.Lucroar.iQueue.Repository.OrdersRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrdersRepository ordersRepository;
    private final CartService cartService;

    public OrderService(OrdersRepository ordersRepository, CartService cartService) {
        this.ordersRepository = ordersRepository;
        this.cartService = cartService;
    }

    public Orders checkoutOrders(Customer customer) {
        Orders orders = ordersRepository.findByCustomer_customerId(customer.getId())
                .orElseGet(() -> {
                    Orders newOrders = new Orders();
                    newOrders.setCustomer(new CustomerDTO(customer));
                    newOrders.setOrders(new ArrayList<>());
                    return newOrders;
                });

        List<Order> cartOrders = cartService.checkout(customer);
        List<Order> existingOrders = orders.getOrders();

        for (Order cartOrder : cartOrders) {
            boolean found = false;

            for (Order existingOrder : existingOrders) {
                if (existingOrder.getProduct_id().equals(cartOrder.getProduct_id())) {
                    existingOrder.setQuantity(existingOrder.getQuantity() + cartOrder.getQuantity());
                    found = true;
                    break;
                }
            }

            if (!found) {
                existingOrders.add(cartOrder);
            }
        }

        return ordersRepository.save(orders);
    }

}
