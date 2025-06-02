package com.Lucroar.iQueue.Entity;

import com.Lucroar.iQueue.DTO.CustomerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "OrdersHistory")
@Data
public class OrdersHistory {
    @Id
    private String id;
    private LocalDateTime orderDate;
    private CustomerDTO customer;
    private int tableNumber;
    private List<Order> orders;
    private OrderStatus status;
    private int total;

    public OrdersHistory(CustomerDTO customer, List<Order> orders, OrderStatus status, LocalDateTime orderDate, int tableNumber) {
        this.customer = customer;
        this.orders = orders;
        this.status = status;
        this.orderDate = orderDate;
        this.tableNumber = tableNumber;
    }
}
