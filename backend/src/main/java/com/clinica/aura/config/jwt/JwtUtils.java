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
import org.springframework.security.core.userdetails.UserDetails;
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

    /**
     * Genera un token JWT con los detalles del usuario autenticado.
     *
     * @param authentication el objeto de autenticación que contiene los detalles del usuario
     * @return un token JWT con los siguientes claims:
     *         - issuer: clave secreta del usuario
     *         - subject: nombre de usuario
     *         - authorities: permisos del usuario (separados por comas)
     *         - roles: roles del usuario (sin el prefijo ROLE_)
     *         - issuedAt: fecha de emisión del token
     *         - expiresAt: fecha de expiración del token (basada en EXPIRATION_TIME)
     *         - jti: identificador único del token (UUID)
     *         - notBefore: fecha mínima de uso del token
     * @throws RuntimeException si ocurre un error durante la generación del token
     */
    public String generateJwtToken(Authentication authentication) {


        Algorithm algorithm = Algorithm.HMAC256(this.SECRET_KEY);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        String authorities = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

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

    /**
     * Valida y decodifica un token JWT.
     *
     * @param token el token JWT a validar
     * @return un objeto DecodedJWT con los detalles del token validado
     * @throws JWTVerificationException si el token no es valido o no cumple con las condiciones establecidas
     */
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

   /**
    * Extrae el nombre de usuario del token JWT.
    *
    * @param token el token JWT
    * @return el nombre de usuario
    */

    public String extractUsername(DecodedJWT token) {
        return token.getSubject();
    }

    /**
    * Extrae un claim específico del token JWT.
    *
    * @param token el token JWT
    * @param claimName el nombre del claim
    * @return el claim
    */

    public Claim getSpecificClaim(DecodedJWT token, String claimName) {
        return token.getClaim(claimName);
    }


    /**
    * Obtiene el tiempo de expiración del token JWT en segundos.
    *
    * @return el tiempo de expiración en segundos
    */
    public int getExpirationTime() {
        return (int) (EXPIRATION_TIME / 1000);
    }
}
