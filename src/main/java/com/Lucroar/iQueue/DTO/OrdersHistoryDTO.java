package com.Lucroar.iQueue.DTO;

import com.Lucroar.iQueue.Entity.OrderStatus;
import com.Lucroar.iQueue.Entity.PaymentMethod;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrdersHistoryDTO {
    private LocalDateTime orderDate;
    private CustomerDTO customer;
    private OrderStatus status;
    private int tableNumber;
    private int total;
    private PaymentMethod paymentMethod;
}
