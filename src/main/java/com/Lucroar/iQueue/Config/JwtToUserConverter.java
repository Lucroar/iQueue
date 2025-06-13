package com.Lucroar.iQueue.Config;

import com.Lucroar.iQueue.Entity.Cashier;
import com.Lucroar.iQueue.Entity.Customer;
import com.Lucroar.iQueue.Entity.KitchenStaff;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtToUserConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {
    public UsernamePasswordAuthenticationToken convert(Jwt source) {
        List<String> roles = source.getClaimAsStringList("roles");
        String userType = source.getClaimAsString("userType");
        String username = source.getSubject();
        String userId = source.getClaimAsString("userId");

        Set<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        switch (userType) {
            case "Cashier" -> {
                Cashier cashier = new Cashier();
                cashier.setUsername(username);
                cashier.setCashier_id(userId);
                return new UsernamePasswordAuthenticationToken(cashier, source, authorities);
            }
            case "Customer" -> {
                Customer customer = new Customer();
                customer.setUsername(username);
                customer.setId(userId);
                return new UsernamePasswordAuthenticationToken(customer, source, authorities);
            }
            case "KitchenStaff" -> {
                KitchenStaff kitchenStaff = new KitchenStaff();
                kitchenStaff.setUsername(username);
                kitchenStaff.setKitchenStaff_id(userId); // Consider renaming this field in the future
                return new UsernamePasswordAuthenticationToken(kitchenStaff, source, authorities);
            }
            default -> throw new IllegalStateException("Unknown userType in token: " + userType);
        }
    }
}
