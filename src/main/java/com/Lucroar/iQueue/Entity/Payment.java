package com.Lucroar.iQueue.Entity;

import com.Lucroar.iQueue.DTO.CustomerDTO;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Payment")
@Data
public class Payment {
    @Id
    private String id;
    private CustomerDTO customer;
    private String orderHistoryId;
    private double cashAmount;
    private double totalAmount;
    private double vatableSale;
    private double change;
    private double vat;
    private PaymentMethod paymentMethod;
}
