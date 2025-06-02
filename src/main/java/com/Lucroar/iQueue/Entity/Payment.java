package com.Lucroar.iQueue.Entity;

import com.Lucroar.iQueue.DTO.CustomerDTO;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "Payment")
@Data
public class Payment {
    @Id
    private String id;
    private CustomerDTO customer;
    private String orderHistoryId;
    private List<Order> orders;
    private int amount;
    private PaymentMethod paymentMethod;
}
