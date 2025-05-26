package com.Lucroar.iQueue.Entity;

import lombok.Data;

@Data
public class Order {
    private String product_id;
    private String name;
    private int price;
    private int quantity;
}
