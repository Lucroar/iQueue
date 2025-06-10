package com.Lucroar.iQueue.DTO;

import com.Lucroar.iQueue.Entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableOrderDTO {
    private String id;
    private int tableNumber;
    private List<Order> orders;
}
