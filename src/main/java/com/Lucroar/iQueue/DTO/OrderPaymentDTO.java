package com.Lucroar.iQueue.DTO;

import com.Lucroar.iQueue.Entity.Order;
import com.Lucroar.iQueue.Entity.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude (JsonInclude.Include.NON_NULL)
public class OrderPaymentDTO {
    private String username;
    private PaymentMethod paymentMethod;
    private List<Order> orders;
    private double totalAmount;
    private double cashAmount;
    private double vatableSale;
    private double vat;
    private double change;
    private boolean isGuest;
}
