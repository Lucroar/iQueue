package com.Lucroar.iQueue.Config;

import com.Lucroar.iQueue.Entity.Cashier;
import com.Lucroar.iQueue.Entity.Customer;
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
    @Override
    public UsernamePasswordAuthenticationToken convert(Jwt source) {
        List<String> roles = source.getClaimAsStringList("roles");

        // Convert roles to GrantedAuthority
        Set<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        // Determine user type based on roles
        if (roles.contains("ROLE_CASHIER")) {
            Cashier cashier = new Cashier();
            cashier.setUsername(source.getClaimAsString("sub"));
            cashier.setCashier_id(source.getClaimAsString("userId"));
            return new UsernamePasswordAuthenticationToken(cashier, source, authorities);
        } else {
            Customer customer = new Customer();
            customer.setUsername(source.getClaimAsString("sub"));
            customer.setCustomerId(source.getClaimAsString("userId"));
            return new UsernamePasswordAuthenticationToken(customer, source, authorities);
        }
    }
}
