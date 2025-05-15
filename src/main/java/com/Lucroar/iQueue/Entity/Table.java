package com.Lucroar.iQueue.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Table")
public class Table {
    @Id
    private String table_id;
    private int size;
    private Status status;
}
