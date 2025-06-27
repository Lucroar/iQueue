package com.Lucroar.iQueue.DTO;

import com.Lucroar.iQueue.Entity.Order;
import com.Lucroar.iQueue.Entity.OrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TableOrderDTO {
    private String id;
    private String username;
    private int tableNumber;
    private boolean isTakeOut;
    private LocalDateTime orderTime;
    private OrderStatus status;
    private List<Order> orders;
    private String description;

    public TableOrderDTO(String id, String username, int tableNumber, boolean isTakeOut, LocalDateTime orderTime, OrderStatus status, List<Order> orders) {
        this.id = id;
        this.username = username;
        this.tableNumber = tableNumber;
        this.isTakeOut = isTakeOut;
        this.orderTime = orderTime;
        this.status = status;
        this.orders = orders;
    }

}
