package com.Lucroar.iQueue.Entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Order {
    private String product_id;
    private String name;
    private double price;
    private int quantity;

    public Order(Order order){
        this.product_id = order.getProduct_id();
        this.name = order.getName();
        this.price = order.getPrice();
        this.quantity = order.getQuantity();
    }
}
