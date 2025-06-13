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
    private LocalDateTime orderDate;
    private CustomerDTO customer;
    private OrderStatus status;
    private int tableNumber;
    private List<Order> orders;
    private int total;

    public OrdersHistoryDTO(LocalDateTime orderDate, List<Order> orders, int total) {
        this.orderDate = orderDate;
        this.orders = orders;
        this.total = total;
    }
}
