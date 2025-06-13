package com.Lucroar.iQueue.DTO;

import com.Lucroar.iQueue.Entity.Orders;
import com.Lucroar.iQueue.Entity.PaymentMethod;
import lombok.Data;

@Data
public class OrderPaymentDTO {
    private String username;
    private PaymentMethod paymentMethod;
    private boolean isGuest;
}
