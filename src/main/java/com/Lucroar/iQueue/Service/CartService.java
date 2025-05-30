package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.CartDTO;
import com.Lucroar.iQueue.DTO.CartOrdersDTO;
import com.Lucroar.iQueue.DTO.CustomerDTO;
import com.Lucroar.iQueue.Entity.Cart;
import com.Lucroar.iQueue.Entity.Customer;
import com.Lucroar.iQueue.Entity.Menu;
import com.Lucroar.iQueue.Entity.Order;
import com.Lucroar.iQueue.Repository.CartRepository;
import com.Lucroar.iQueue.Repository.MenuRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final MenuRepository menuRepository;

    public CartService(CartRepository cartRepository, MenuRepository menuRepository) {
        this.cartRepository = cartRepository;
        this.menuRepository = menuRepository;
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
            Menu menuOpt = menuRepository.findById(newOrder.getProduct_id()).get();
            newOrder.setName(menuOpt.getName());
            newOrder.setPrice(menuOpt.getPrice());
            boolean found = false;
            for (Order existingOrder : existingOrders) {
                if (existingOrder.getProduct_id().equals(newOrder.getProduct_id())) {
                    existingOrder.setQuantity(existingOrder.getQuantity() + newOrder.getQuantity());
                    cart.setTotal(cart.getTotal() + menuOpt.getPrice()*newOrder.getQuantity());
                    found = true;
                    break;
                }
            }
            if (!found) {
                cart.setTotal(cart.getTotal() + menuOpt.getPrice()*newOrder.getQuantity());
                existingOrders.add(newOrder);
            }
        }

        return cartRepository.save(cart);
    }

    public CartOrdersDTO viewOrder(Customer customer){
        CartOrdersDTO cartOrdersDTO = new CartOrdersDTO();
        Optional<Cart> cartOpt = Optional.of(cartRepository.findByCustomer_customerId(customer.getId()).orElse(new Cart()));
        Cart cart = cartOpt.get();
        cartOrdersDTO.setOrders(cart.getOrders());
        cartOrdersDTO.setPrice(cart.getTotal());
        return cartOrdersDTO;
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
                    case "add" -> {
                        order.setQuantity(order.getQuantity() + 1);
                        cart.setTotal(cart.getTotal() + order.getPrice());
                    }
                    case "deduct" -> {
                        int newQuantity = order.getQuantity() - 1;
                        cart.setTotal(cart.getTotal() - order.getPrice());
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
        Order orderToRemove = null;
        for (Order order : cart.getOrders()) {
            if (order.getProduct_id().equals(cartDTO.getMenuId())) {
                orderToRemove = order;
                break;
            }
        }

        if (orderToRemove != null) {
            int deduction = orderToRemove.getPrice() * orderToRemove.getQuantity();
            cart.getOrders().remove(orderToRemove);
            cart.setTotal(cart.getTotal() - deduction);
        }

        return cartRepository.save(cart);
    }

    public List<Order> checkout(Customer customer){
        Optional<Cart> cartOpt = cartRepository.findByCustomer_customerId(customer.getId());
        if (cartOpt.isEmpty()) {
            throw new RuntimeException("Cart not found for customer ID: " + customer.getId());
        }

        Cart cart = cartOpt.get();
        List<Order> ordersToCheckout = new ArrayList<>(cart.getOrders());
        cart.setTotal(0);

        // Clear the cart
        cart.setOrders(new ArrayList<>());
        cartRepository.save(cart);

        return ordersToCheckout;
    }
}
