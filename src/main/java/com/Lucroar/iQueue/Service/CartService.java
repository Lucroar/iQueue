package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.CustomerDTO;
import com.Lucroar.iQueue.Entity.Cart;
import com.Lucroar.iQueue.Entity.Customer;
import com.Lucroar.iQueue.Entity.Menu;
import com.Lucroar.iQueue.Entity.Order;
import com.Lucroar.iQueue.Repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    //Update for menu parameter
    public Cart addToCart(Customer customer, List<Order> orderList){
        Optional<Cart> cartOpt = cartRepository.findByCustomer_customerId(customer.getCustomer_id());

        Cart cart;
        if(cartOpt.isPresent()){
            cart = cartOpt.get();
        } else {
            cart = new Cart();
            cart.setCustomer(new CustomerDTO(customer));
            cart.setOrders(new ArrayList<>());
        }
        cart.getOrders().addAll(orderList);
        return cartRepository.save(cart);
    }
}
