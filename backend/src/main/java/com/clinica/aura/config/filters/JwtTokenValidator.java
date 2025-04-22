package com.clinica.aura.config.filters;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.clinica.aura.config.jwt.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

//public class JwtTokenValidator extends OncePerRequestFilter {
//    private final JwtUtils jwtUtils;
//
//    public JwtTokenValidator(JwtUtils jwtUtils) {
//        this.jwtUtils = jwtUtils;
//    }
//    @Override
//    protected void doFilterInternal(@NonNull HttpServletRequest request,
//                                    @NonNull HttpServletResponse response,
//                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
//        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
//
//
//        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
//            jwtToken = jwtToken.substring(7);
//
//
//            try {
//                DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken);
//
//
//                String username = jwtUtils.extractUsername(decodedJWT);
//
//                String stringAuthorities = jwtUtils.getSpecificClaim(decodedJWT, "authorities").asString();
//
//
//                Collection<? extends GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(stringAuthorities);
//
//
//                SecurityContext securityContext = SecurityContextHolder.getContext();
//                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
//                securityContext.setAuthentication(authentication);
//                SecurityContextHolder.setContext(securityContext);
//
//            } catch (Exception e) {
//
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return;
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}

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

    private void processJwtToken(String jwtToken) {
        DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken);

        String username = jwtUtils.extractUsername(decodedJWT);
        String authorities = jwtUtils.getSpecificClaim(decodedJWT, "authorities").asString();

        setSecurityContext(username, authorities);
    }

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