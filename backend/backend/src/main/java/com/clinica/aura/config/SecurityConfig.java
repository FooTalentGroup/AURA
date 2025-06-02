package com.clinica.aura.config;

import com.clinica.aura.config.filters.JwtTokenValidator;
import com.clinica.aura.config.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfig {

    private final JwtUtils jwtUtils;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint entryPoint;


    /**
     * Configura CORS (Cross-Origin Resource Sharing) para permitir solicitudes desde cualquier origen.
     *
     * @return una configuración de CORS que:
     *         - Permite credenciales en las solicitudes
     *         - Acepta cualquier origen ("*")
     *         - Acepta cualquier encabezado ("*")
     *         - Acepta cualquier método HTTP ("*")
     *         - Se aplica a todas las rutas ("/**")
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**
     * Configura la cadena de seguridad de Spring Security.
     *
     * @param http el objeto HttpSecurity para configurar
     * @return la cadena de filtros de seguridad configurada
     *
     * Configuración detallada:
     * 1. CORS: Utiliza la configuración definida en corsConfigurationSource()
     * 2. CSRF: Deshabilitado (apropiado para APIs REST)
     * 3. Rutas públicas permitidas:
     *    - POST /auth/login: Autenticación de usuarios
     *    - POST /auth/professional/register: Registro de profesionales
     *    - POST /auth/receptionist/register: Registro de recepcionistas
     *    - Swagger UI y documentación: Acceso público para documentación API
     * 4. Rutas protegidas: Todas las demás rutas requieren autenticación
     * 5. Manejo de excepciones:
     *    - authenticationEntryPoint: Manejador de errores de autenticación
     *    - accessDeniedHandler: Manejador de errores de acceso denegado
     * 6. Gestión de sesiones: Stateless (sin sesión)
     * 7. JWT: Se agrega el filtro JwtTokenValidator antes del filtro básico
     * 8. Seguridad de encabezados: Configuración de frameOptions para permitir H2 Console
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST,"/auth/login").permitAll()
                                .requestMatchers(HttpMethod.POST,"/auth/professional/register").permitAll()
                                .requestMatchers("/swagger-ui.html",
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/swagger-resources/**",
                                        "/swagger-resources",
                                        "/webjars/**"
                                ).permitAll()
                                .requestMatchers(HttpMethod.POST,"/auth/receptionist/register").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(entryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        return http.build();
    }



    /**
     * Configura y proporciona el AuthenticationManager de Spring Security.
     *
     * @param authenticationConfiguration la configuración de autenticación de Spring Security
     * @return el AuthenticationManager configurado para manejar la autenticación en la aplicación
     * @throws Exception si ocurre un error durante la configuración
     *
     * Este método es necesario para:
     * 1. Configurar la autenticación en la aplicación
     * 2. Permitir la inyección del AuthenticationManager en otros componentes
     * 3. Proporcionar el punto de entrada para la autenticación de usuarios
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configura el proveedor de autenticación para Spring Security.
     *
     * @param userDetailsService el servicio que proporciona detalles de los usuarios
     * @return un DaoAuthenticationProvider configurado para:
     *         - Usar el servicio de detalles de usuario proporcionado
     *         - Encriptar contraseñas con el passwordEncoder configurado
     *
     * Este método es crucial para:
     * 1. Autenticar usuarios contra la base de datos
     * 2. Verificar contraseñas encriptadas
     * 3. Proporcionar los detalles de usuario necesarios para la seguridad
     */
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }


    /**
     * Configura el codificador de contraseñas para la aplicación.
     *
     * @return un PasswordEncoder que utiliza el algoritmo BCrypt
     *
     * Características del codificador:
     * 1. Utiliza el algoritmo BCrypt para encriptar contraseñas
     * 2. Proporciona seguridad mediante hash y salting
     * 3. Es compatible con el DaoAuthenticationProvider para la autenticación
     * 4. Permite comparación segura de contraseñas en la autenticación
     *
     * Nota: BCrypt es un algoritmo recomendado por su resistencia a ataques de fuerza bruta
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
