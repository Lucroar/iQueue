package com.Lucroar.iQueue.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "Table")
public class Table {
    @Id
    private String tableId;
    @Field("table_number")
    @JsonProperty("table_number")
    private int tableNumber;
    private int size;
    private Status status;
}
