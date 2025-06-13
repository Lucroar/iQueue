package com.Lucroar.iQueue.DTO;

import com.Lucroar.iQueue.Entity.Order;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TableOrderDTO {
    private String id;
    private String username;
    private int tableNumber;
    private boolean isTakeOut;
    private List<Order> orders;
}
