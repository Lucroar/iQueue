package com.Lucroar.iQueue.DTO;

import com.Lucroar.iQueue.Entity.Order;
import lombok.Data;

import java.util.List;

@Data
public class CashierOrderDTO {
    private String username;
    private boolean isTakeOut;
    private List<Order> orders;
}
