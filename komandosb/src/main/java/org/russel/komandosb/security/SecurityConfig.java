package org.russel.komandosb.security;

import org.russel.komandosb.data.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomUserDetailsService customUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new CustomUserDetailsService(userRepository, passwordEncoder);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService); // pass userDetailsService
        provider.setPasswordEncoder(passwordEncoder); // set password encoder
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, DaoAuthenticationProvider authProvider) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authProvider)
                .build();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Authorize requests
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // Open endpoint
                        .requestMatchers("/api/test/**").permitAll() // Open endpoint
                        .requestMatchers(HttpMethod.GET, "/api/task/**").authenticated() // Only GET tasks require authentication
                        .anyRequest().authenticated()// All other API endpoints require authentication
                )
                .httpBasic(httpBasic -> {})// Use Basic Authentication
                .csrf(csrf -> csrf.disable()); // disable CSRF for REST APIs;

        return http.build();
    }

}
