package com.Lucroar.iQueue.DTO;

import com.Lucroar.iQueue.Entity.Customer;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude (JsonInclude.Include.NON_NULL)
public class CustomerDTO {
    private String customerId;
    private String username;
    private String first_Name;
    private String last_Name;
    private String email;
    private String password;

    public CustomerDTO(Customer customer) {
        this.customerId = customer.getId();
        this.username = customer.getUsername();
        this.first_Name = customer.getFirst_name();
        this.last_Name = customer.getLast_name();
        this.email = customer.getEmail();
    }
}
