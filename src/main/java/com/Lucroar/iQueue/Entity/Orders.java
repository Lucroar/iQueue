package com.Lucroar.iQueue.Entity;

import com.Lucroar.iQueue.DTO.CustomerDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "Orders")
@Data
@NoArgsConstructor
public class Orders {
    @Id
    private String id;
    private CustomerDTO customer;
    private int tableNumber;
    private boolean takeOut;
    private List<Order> orders;
    private int total;
    @CreatedDate
    private LocalDateTime createdAt;
}