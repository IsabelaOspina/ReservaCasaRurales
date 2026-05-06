package org.example.reservacasarurales.Config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
                "http://localhost:4200",
                "https://reserva-front-v2.vercel.app"
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/usuario/**").permitAll()
                        .requestMatchers("/uploads/**").permitAll()

                        .requestMatchers("/cliente/**")
                        .hasRole("CLIENTE")

                        .requestMatchers("/propietario/**")
                        .hasRole("PROPIETARIO")

                        .requestMatchers("/casa_rural/registrar")
                        .hasRole("PROPIETARIO")

                        .requestMatchers("/casa_rural/listar")
                        .hasAnyRole("CLIENTE","PROPIETARIO")

                        .requestMatchers("/casa_rural/buscar")
                        .hasRole("CLIENTE")

                        .requestMatchers("/casa_rural/cliente/codigo/**")
                        .hasRole("CLIENTE")

                        .requestMatchers("/*/cocinas/registrar")
                        .hasRole("PROPIETARIO")

                        .requestMatchers("/*/cocinas/listar")
                        .hasAnyRole("CLIENTE","PROPIETARIO")

                        .requestMatchers("/*/dormitorios/registrar")
                        .hasRole("PROPIETARIO")

                        .requestMatchers("/*/dormitorios/listar")
                        .hasAnyRole("CLIENTE","PROPIETARIO")

                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}