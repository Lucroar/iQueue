package com.Lucroar.iQueue.Entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Order {
    private String product_id;
    private String name;
    private int price;
    private int quantity;
}
