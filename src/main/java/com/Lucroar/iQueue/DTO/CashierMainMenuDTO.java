package com.Lucroar.iQueue.DTO;

import com.Lucroar.iQueue.Entity.Status;
import lombok.Data;

@Data
public class CashierMainMenuDTO {
    private int tableNumber;
    private int size;
    private Status status;
}
