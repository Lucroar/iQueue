package com.Lucroar.iQueue.DTO;

import com.Lucroar.iQueue.Entity.Order;
import com.Lucroar.iQueue.Entity.OrderStatus;
import com.Lucroar.iQueue.Entity.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude (JsonInclude.Include.NON_NULL)
public class OrdersHistoryDTO {
    private String id;
    private LocalDateTime orderDate;
    private CustomerDTO customer;
    private OrderStatus status;
    private int tableNumber;
    private List<Order> orders;
    private String description;
    private double total;

    public OrdersHistoryDTO(String id, LocalDateTime orderDate, List<Order> orders, double total, OrderStatus status, String description) {
        this.id = id;
        this.orderDate = orderDate;
        this.orders = orders;
        this.total = total;
        this.status = status;
        this.description = description;
    }
}
