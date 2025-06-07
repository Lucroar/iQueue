package com.Lucroar.iQueue.DTO;

import com.Lucroar.iQueue.Entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CashierMainMenuDTO {
    private int tableNumber;
    private String username;
    private int size;
    private Status status;
}
