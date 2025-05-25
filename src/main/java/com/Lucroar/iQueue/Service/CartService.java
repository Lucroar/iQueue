package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.CartDTO;
import com.Lucroar.iQueue.DTO.CustomerDTO;
import com.Lucroar.iQueue.Entity.Cart;
import com.Lucroar.iQueue.Entity.Customer;
import com.Lucroar.iQueue.Entity.Order;
import com.Lucroar.iQueue.Repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    //Update for menu parameter
    public Cart addToCart(Customer customer, List<Order> orderList){
        Optional<Cart> cartOpt = cartRepository.findByCustomer_customerId(customer.getId());

        Cart cart = cartOpt.orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setCustomer(new CustomerDTO(customer));
            newCart.setOrders(new ArrayList<>());
            return newCart;
        });

        List<Order> existingOrders = cart.getOrders();

        for (Order newOrder : orderList) {
            boolean found = false;
            for (Order existingOrder : existingOrders) {
                if (existingOrder.getProduct_id().equals(newOrder.getProduct_id())) {
                    existingOrder.setQuantity(existingOrder.getQuantity() + newOrder.getQuantity());
                    found = true;
                    break;
                }
            }
            if (!found) {
                existingOrders.add(newOrder);
            }
        }

        return cartRepository.save(cart);
    }

    public List<Order> viewOrder(Customer customer){
        return cartRepository.findByCustomer_customerId(customer.getId())
                .map(Cart::getOrders)
                .orElse(Collections.emptyList());
    }

    public Cart updateCartQuantity(Customer customer, CartDTO cartDTO){
        Optional<Cart> cartOpt = cartRepository.findByCustomer_customerId(customer.getId());

        if (cartOpt.isEmpty()) {
            throw new RuntimeException("Cart not found for customer ID: " + customer.getId());
        }

        Cart cart = cartOpt.get();

        Iterator<Order> iterator = cart.getOrders().iterator();
        while (iterator.hasNext()) {
            Order order = iterator.next();
            if (order.getProduct_id().equals(cartDTO.getMenuId())) {
                switch (cartDTO.getAction()) {
                    case "add" -> order.setQuantity(order.getQuantity() + 1);
                    case "deduct" -> {
                        int newQuantity = order.getQuantity() - 1;
                        if (newQuantity <= 0) {
                            iterator.remove(); // Removes order from cart
                        } else {
                            order.setQuantity(newQuantity);
                        }
                    }
                }
                break;
            }
        }

        return cartRepository.save(cart);
    }

    public Cart deleteOrder(Customer customer, CartDTO cartDTO){
        Optional<Cart> cartOpt = cartRepository.findByCustomer_customerId(customer.getId());
        if (cartOpt.isEmpty()) {
            throw new RuntimeException("Cart not found for customer ID: " + customer.getId());
        }

        Cart cart = cartOpt.get();
        cart.getOrders().removeIf(order -> order.getProduct_id().equals(cartDTO.getMenuId()));

        return cartRepository.save(cart);
    }

    public List<Order> checkout(Customer customer){
        Optional<Cart> cartOpt = cartRepository.findByCustomer_customerId(customer.getId());
        if (cartOpt.isEmpty()) {
            throw new RuntimeException("Cart not found for customer ID: " + customer.getId());
        }

        Cart cart = cartOpt.get();
        List<Order> ordersToCheckout = new ArrayList<>(cart.getOrders());

        // Clear the cart
        cart.setOrders(new ArrayList<>());
        cartRepository.save(cart);

        return ordersToCheckout;
    }
}
