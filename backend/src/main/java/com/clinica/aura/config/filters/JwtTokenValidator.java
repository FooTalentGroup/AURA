package com.clinica.aura.config.filters;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.clinica.aura.config.jwt.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;


public class JwtTokenValidator extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;

    public JwtTokenValidator(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String jwtToken = extractTokenFromCookies(request);

        if (jwtToken != null) {
            try {
                processJwtToken(jwtToken);
            } catch (Exception e) {
                handleAuthenticationError(response, e);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extrae el token JWT de las cookies de la solicitud HTTP.
     *
     * @param request la solicitud HTTP que contiene las cookies
     * @return el valor del token JWT si se encuentra, o null si no existe
     */
    private String extractTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt_token".equalsIgnoreCase(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Procesa el token JWT extraído de las cookies.
     *
     * @param jwtToken el token JWT extraído
     */

    private void processJwtToken(String jwtToken) {
        DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken);

        String username = jwtUtils.extractUsername(decodedJWT);
        String authorities = jwtUtils.getSpecificClaim(decodedJWT, "authorities").asString();

        setSecurityContext(username, authorities);
    }

    /**
     * Establece el contexto de seguridad con la información del usuario autenticado.
     *
     * @param username el nombre de usuario autenticado
     * @param authorities las autoridades del usuario autenticado
     */

    private void setSecurityContext(String username, String authorities) {
        Collection<? extends GrantedAuthority> grantedAuthorities =
                AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                username,
                null,
                grantedAuthorities
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Maneja el error de autenticación.
     *
     * @param response la respuesta HTTP
     * @param e la excepción de autenticación
     */
    private void handleAuthenticationError(HttpServletResponse response, Exception e)
            throws IOException {

        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(
                "{ \"error\": \"Acceso no autorizado\", " +
                        "\"message\": \"" + e.getMessage() + "\" }"
        );
    }
}