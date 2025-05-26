package com.Lucroar.iQueue.DTO;

import com.Lucroar.iQueue.Entity.Order;
import lombok.Data;

import java.util.List;

@Data
public class CartOrdersDTO {
    private List<Order> orders;
    private int price;
}
