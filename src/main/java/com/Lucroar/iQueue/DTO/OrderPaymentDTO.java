package com.Lucroar.iQueue.DTO;

import com.Lucroar.iQueue.Entity.Order;
import com.Lucroar.iQueue.Entity.PaymentMethod;
import lombok.Data;

import java.util.List;

@Data
public class OrderPaymentDTO {
    private String username;
    private PaymentMethod paymentMethod;
    private List<Order> orders;
    private double totalAmount;
    private double cashAmount;
    private double vatableSale;
    private double vat;
    private boolean isGuest;
}
