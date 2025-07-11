package com.Lucroar.iQueue.Entity;

import com.Lucroar.iQueue.DTO.CustomerDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "OrdersHistory")
@Data
@NoArgsConstructor
public class OrdersHistory {
    @Id
    private String id;
    @CreatedDate
    private LocalDateTime orderCreation;
    private CustomerDTO customer;
    private int tableNumber;
    private boolean isTakeOut;
    private List<Order> orders;
    private OrderStatus status;
    private String description;
    private double total;

    public OrdersHistory(CustomerDTO customer, List<Order> orders, OrderStatus status, LocalDateTime orderCreation, int tableNumber) {
        this.customer = customer;
        this.orders = orders;
        this.status = status;
        this.orderCreation = orderCreation;
        this.tableNumber = tableNumber;
    }

    public boolean isTakeOut() {
        return isTakeOut;
    }

    public void setTakeOut(boolean takeOut) {
        isTakeOut = takeOut;
    }
}
