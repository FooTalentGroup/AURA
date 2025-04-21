package com.clinica.aura.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    @Value("${jwt.secret.user.key}")
    private String SECRET_USER_KEY;

    @Value("${jwt.expiration.time}")
    private long EXPIRATION_TIME;

    // Generar un token de acceso
    public String generateJwtToken(Authentication authentication) {


        Algorithm algorithm = Algorithm.HMAC256(this.SECRET_KEY);

        String username = authentication.getPrincipal().toString();

        String authorities = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // Extraer los roles (authorities que empiezan con ROLE_)
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(role -> role.replace("ROLE_", ""))
                .toList();


        try {
            return JWT.create()
                    .withIssuer(this.SECRET_USER_KEY)
                    .withSubject(username)
                    .withClaim("authorities", authorities)
                    .withClaim("roles", roles)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .withJWTId(UUID.randomUUID().toString())
                    .withNotBefore(new Date(System.currentTimeMillis()))
                    .sign(algorithm);
        } catch (Exception e) {
            throw new RuntimeException("Error generating JWT token", e);
        }
    }


    //Decodificar el token y validarlo
    public DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.SECRET_USER_KEY)
                    .build();
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("Token Invalid, not Authorized");
        }
    }

    // Obtener el username del token
    public String extractUsername(DecodedJWT token) {
        return token.getSubject();
    }

    // Obtener un solo Claim del token
    public Claim getSpecificClaim(DecodedJWT token, String claimName) {
        return token.getClaim(claimName);
    }

    // Obtener todos los claims del token
    public Map<String, Claim> extractAllClaims(DecodedJWT token) {
        return token.getClaims();
    }

    // Obtener el tiempo de expiración del token
    public Date extractExpiration(DecodedJWT token) {
        return token.getExpiresAt();
    }

    public int getExpirationTime() {
        return (int) (EXPIRATION_TIME / 1000);
    }
}
