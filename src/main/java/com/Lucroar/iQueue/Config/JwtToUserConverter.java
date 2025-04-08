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
        String username = source.getClaimAsString("sub"); // Subject field contains username
        List<String> roles = source.getClaimAsStringList("roles"); // Extract roles from JWT

        // Convert roles to GrantedAuthority
        Set<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        // Determine user type based on roles
        if (roles.contains("ROLE_CASHIER")) {
            Cashier cashier = new Cashier();
            cashier.setUsername(username);
            return new UsernamePasswordAuthenticationToken(cashier, source, authorities);
        } else {
            Customer customer = new Customer();
            customer.setUsername(username);
            return new UsernamePasswordAuthenticationToken(customer, source, authorities);
        }
    }
}
