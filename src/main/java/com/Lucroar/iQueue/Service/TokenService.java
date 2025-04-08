package com.Lucroar.iQueue.Service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TokenService {
    private final JwtEncoder encoder;

    public TokenService(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    public Map<String, String> generateToken(Authentication authentication) {
        Instant now = Instant.now();

        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(role -> role.equals("ROLE_CASHIER") || role.equals("ROLE_CUSTOMER"))
                .collect(Collectors.toSet());

        String userType = roles.contains("ROLE_CASHIER") ? "Cashier" : "Customer";

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(5, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("roles", roles)
                .claim("userType", userType)
                .build();

        return Collections.singletonMap("token", this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue());
    }
}
