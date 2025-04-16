package com.Lucroar.iQueue.DTO;

import com.Lucroar.iQueue.Entity.Customer;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerDTO {
    private String customer_id;
    private String username;
    private String first_Name;
    private String last_Name;
    private String email;
    private String password;

    public CustomerDTO(Customer customer) {
        this.customer_id = customer.getCustomer_id();
        this.username = customer.getUsername();
        this.first_Name = customer.getFirst_name();
        this.last_Name = customer.getLast_name();
        this.email = customer.getEmail();
    }
}
