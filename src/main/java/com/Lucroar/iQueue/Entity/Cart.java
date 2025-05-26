package com.Lucroar.iQueue.Entity;

import com.Lucroar.iQueue.DTO.CustomerDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "Cart")
@Data
@NoArgsConstructor
public class Cart {
    @Id
    private String cart_id;
    private CustomerDTO customer;
    private List<Order> orders;
    private int total;
}
