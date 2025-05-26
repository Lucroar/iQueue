package com.Lucroar.iQueue.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "LastSeated")
@Data
@NoArgsConstructor
public class LastSeated {
    @Id
    private String id;
    private int tableNumber;
    private String queueingNumber;
    private int tier;

    public LastSeated(int tableNumber, String queueingNumber, int tier) {
        this.tableNumber = tableNumber;
        this.queueingNumber = queueingNumber;
        this.tier = tier;
    }
}
