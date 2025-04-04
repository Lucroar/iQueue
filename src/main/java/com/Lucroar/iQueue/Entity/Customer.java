package com.Lucroar.iQueue.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document (collection = "Customer")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Customer{
    @Id
    private Id customer_id;
    private String first_name;
    private String last_name;
    private String email;
    private String username;
    private String password;
}
